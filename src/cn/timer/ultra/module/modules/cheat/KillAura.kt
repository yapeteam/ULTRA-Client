/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.timer.ultra.module.modules.cheat

import cn.timer.ultra.Client
import cn.timer.ultra.event.EventManager
import cn.timer.ultra.event.EventTarget
import cn.timer.ultra.event.events.*
import cn.timer.ultra.module.Category
import cn.timer.ultra.module.CombatModule
import cn.timer.ultra.utils.fdp.*
import cn.timer.ultra.values.Booleans
import cn.timer.ultra.values.Mode
import cn.timer.ultra.values.Numbers
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.item.ItemAxe
import net.minecraft.item.ItemPickaxe
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.*
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.MathHelper
import net.minecraft.world.WorldSettings
import org.apache.commons.lang3.RandomUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.glu.Cylinder
import java.awt.Color
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


class KillAura : CombatModule("KillAura", Keyboard.KEY_NONE, Category.Cheat) {
    private val canBlock = true
    private val cancelRun = false

    /**
     * OPTIONS
     */

    // CPS - Attack speed
    private var maxCpsValue: Numbers<Int> = Numbers<Int>("MaxCPS", 1, 20, 8) { _, newValue ->
        if (minCpsValue != null && newValue.toInt() >= minCpsValue.getValue()) attackDelay =
            getAttackDelay(minCpsValue.getValue(), newValue.toInt())
    }

    private var minCpsValue: Numbers<Int> = Numbers<Int>("MinCPS", 1, 20, 20) { _, newValue ->
        if (maxCpsValue != null && newValue.toInt() <= maxCpsValue.getValue()) attackDelay =
            getAttackDelay(newValue.toInt(), maxCpsValue.getValue())
    }

    private val hurtTimeValue = Numbers("HurtTime", 0, 10, 10)
    private val combatDelayValue = Booleans("1.9CombatDelay", false)

    // Range
    private val rangeValue = Numbers("Range", 1f, 8f, 3.7f)
    private val throughWallsRangeValue = Numbers("ThroughWallsRange", 0f, 8f, 1.5f)
    private val swingRangeValue = Numbers("SwingRange", 0f, 15f, 5f)
    private val discoverRangeValue = Numbers("DiscoverRange", 0f, 15f, 6f)

    // Modes
    private val priorityValue = Mode(
        "Priority", arrayOf("Health", "Distance", "Fov", "LivingTime", "Armor", "HurtResistantTime"), "Distance"
    )
    private val targetModeValue = Mode("TargetMode", arrayOf("Single", "Switch", "Multi"), "Single")

    // Bypass
    private val swingValue = Mode("Swing", arrayOf("Normal", "Packet", "None"), "Normal")
    private val keepSprintValue = Booleans("KeepSprint", true)
    private val noBadPacketsValue = Booleans("NoBadPackets", false)

    // AutoBlock
    private val autoBlockValue = Mode("AutoBlock", arrayOf("Range", "Fake", "Off"), "Off")

    private val autoBlockRangeValue = Numbers("AutoBlockRange", 0f, 8f, 2.5f)

    private val autoBlockPacketValue = Mode(
        "AutoBlockPacket", arrayOf("AfterTick", "AfterAttack", "Vanilla"), "AfterTick"
    )
    private val interactAutoBlockValue = Booleans("InteractAutoBlock", true)
    private val blockRateValue = Numbers("BlockRate", 1, 100, 100)

    // Raycast
    private val raycastValue = Booleans("RayCast", true)
    private val raycastIgnoredValue = Booleans("RayCastIgnored", false)
    private val livingRaycastValue = Booleans("LivingRayCast", true)

    // Bypass
    private val aacValue = Booleans("AAC", true)
    // TODO: Divide AAC Opinion into three separated opinions

    // Rotations
    private val rotationModeValue = Mode(
        "RotationMode",
        arrayOf("None", "LiquidBounce", "ForceCenter", "SmoothCenter", "SmoothLiquid", "LockView", "OldMatrix"),
        "LiquidBounce"
    )
    // TODO: RotationMode Bypass Intave

    private val maxTurnSpeedValue = Numbers("MaxTurnSpeed", 1f, 180f, 180f)

    private val minTurnSpeedValue = Numbers("MinTurnSpeed", 1f, 180f, 180f)

    private val rotationSmoothModeValue =
        Mode("SmoothMode", arrayOf("Custom", "Line", "Quad", "Sine", "QuadSine"), "Custom")

    private val rotationSmoothValue = Numbers("CustomSmooth", 1f, 10f, 2f)

    private val randomCenterModeValue = Mode("RandomCenter", arrayOf("Off", "Cubic", "Horizonal", "Vertical"), "Off")
    private val randomCenRangeValue = Numbers("RandomRange", 0.0f, 1.2f, 0.0f)

    private val silentRotationValue = Booleans("SilentRotation", true)
    private val rotationStrafeValue = Mode(
        "Strafe", arrayOf("Off", "Strict", "Silent"), "Silent"
    )
    private val strafeOnlyGroundValue = Booleans(
        "StrafeOnlyGround", true
    )
    private val rotationRevValue = Booleans("RotationReverse", false)
    private val rotationRevTickValue = Numbers("RotationReverseTick", 1, 20, 5)
    private val keepDirectionValue = Booleans("KeepDirection", true)
    private val keepDirectionTickValue = Numbers("KeepDirectionTick", 1, 20, 15)
    private val hitableValue = Booleans("AlwaysHitable", true)
    private val fovValue = Numbers("FOV", 0f, 180f, 180f)

    // Predict
    private val predictValue = Booleans("Predict", true)

    private val maxPredictSizeValue = Numbers("MaxPredictSize", 0.1f, 5f, 1f)
    private val minPredictSizeValue = Numbers("MinPredictSize", 0.1f, 5f, 1f)

    // Bypass
    private val failRateValue = Numbers("FailRate", 0f, 100f, 0f)
    private val fakeSwingValue = Booleans("FakeSwing", true)
    private val noInventoryAttackValue = Mode("NoInvAttack", arrayOf("Spoof", "CancelRun", "Off"), "Off")

    private val noInventoryDelayValue = Numbers("NoInvDelay", 0, 500, 200)
    private val switchDelayValue = Numbers("SwitchDelay", 1, 2000, 300)
    private val limitedMultiTargetsValue = Numbers("LimitedMultiTargets", 0, 50, 0)

    // Visuals
    private val markValue = Mode("Mark", arrayOf("Liquid", "FDP", "Block", "Jello", "Sims", "None"), "FDP")
    private val circleValue = Booleans("Circle", false)
    private val circleRedValue = Numbers("CircleRed", 0, 255, 255)
    private val circleGreenValue = Numbers("CircleGreen", 0, 255, 255)
    private val circleBlueValue = Numbers("CircleBlue", 0, 255, 255)
    private val circleAlphaValue = Numbers("CircleAlpha", 0, 255, 255)
    private val circleThicknessValue = Numbers("CircleThickness", 1F, 5F, 2F)

    init {
        addValues(
            maxCpsValue,
            minCpsValue,
            hurtTimeValue,
            combatDelayValue,
            rangeValue,
            throughWallsRangeValue,
            swingRangeValue,
            discoverRangeValue,
            priorityValue,
            targetModeValue,
            swingValue,
            keepSprintValue,
            noBadPacketsValue,
            autoBlockValue,
            autoBlockRangeValue,
            autoBlockPacketValue,
            interactAutoBlockValue,
            blockRateValue,
            raycastValue,
            raycastIgnoredValue,
            livingRaycastValue,
            aacValue,
            rotationModeValue,
            maxTurnSpeedValue,
            minTurnSpeedValue,
            rotationSmoothModeValue,
            rotationSmoothValue,
            randomCenterModeValue,
            randomCenRangeValue,
            silentRotationValue,
            rotationStrafeValue,
            strafeOnlyGroundValue,
            rotationRevValue,
            rotationRevTickValue,
            keepDirectionValue,
            keepDirectionTickValue,
            hitableValue,
            fovValue,
            predictValue,
            maxPredictSizeValue,
            minPredictSizeValue,
            failRateValue,
            fakeSwingValue,
            noInventoryAttackValue,
            noInventoryDelayValue,
            switchDelayValue,
            limitedMultiTargetsValue,
            markValue,
            circleValue,
            circleRedValue,
            circleGreenValue,
            circleAlphaValue,
            circleThicknessValue
        )
    }

    /**
     * MODULE
     */

    // Target
    var target: EntityLivingBase? = null
    private var currentTarget: EntityLivingBase? = null
    private var hitable = false
    private var packetSent = false
    private val prevTargetEntities = mutableListOf<Int>()
    private val discoveredTargets = mutableListOf<EntityLivingBase>()
    private val inRangeDiscoveredTargets = mutableListOf<EntityLivingBase>()

    // Attack delay
    private val attackTimer = MSTimer()
    private val switchTimer = MSTimer()
    private var attackDelay = 0L
    private var clicks = 0

    // Container Delay
    private var containerOpen = -1L

    // Swing
    private val swingTimer = MSTimer()
    private var swingDelay = 0L
    private var canSwing = false

    // Fake block status
    private var blockingStatus = false

    /**
     * Enable kill aura module
     */
    override fun onEnable() {
        mc.thePlayer ?: return
        mc.theWorld ?: return

        updateTargetValue()
    }

    /**
     * Disable kill aura module
     */
    override fun onDisable() {
        target = null
        currentTarget = null
        hitable = false
        packetSent = false
        prevTargetEntities.clear()
        discoveredTargets.clear()
        inRangeDiscoveredTargets.clear()
        attackTimer.reset()
        clicks = 0
        canSwing = false
        swingTimer.reset()

        stopBlocking()
        rotationUtils.setTargetRotationReverse(rotationUtils.serverRotation, 0, 0)
    }

    private val rotationUtils = Client.rotationUtils

    @EventTarget
    fun onTick(e: EventTick) {
        maxRange = rangeValue.floatValue().toDouble()
    }

    /**
     * Motion event
     */
    @EventTarget
    fun onMotion(event: EventMove) {
        if (mc.thePlayer.isRiding) {
            return
        }
        runAttackLoop()


        if (packetSent && noBadPacketsValue.getValue()) {
            return
        }
        // AutoBlock
        if (autoBlockValue.equals("Range") && discoveredTargets.isNotEmpty() && (!autoBlockPacketValue.equals("AfterAttack") || discoveredTargets.any {
                mc.thePlayer.getDistanceToEntity(
                    it
                ) > maxRange
            })) {
            val target = this.target ?: discoveredTargets.first()
            if (mc.thePlayer.getDistanceToEntity(target) < autoBlockRangeValue.getValue()) {
                startBlocking(
                    target, interactAutoBlockValue.getValue() && (mc.thePlayer.getDistanceToEntity(target) < maxRange)
                )
            }
        }

        target ?: return
        currentTarget ?: return
        // Update hitable
        updateHitable()

        if (rotationStrafeValue.equals("Off")) {
            update()
        }
    }

    /**
     * Strafe event
     */
    @EventTarget
    fun onStrafe(event: EventStrafe) {
        if (rotationStrafeValue.equals("Off") && !mc.thePlayer.isRiding) {
            return
        }

        // if(event.eventState == EventState.PRE)
        update()

        if (strafeOnlyGroundValue.getValue() && !mc.thePlayer.onGround) {
            return
        }

        // TODO: Fix Rotation issue on Strafe POST Event

        if (discoveredTargets.isNotEmpty() && rotationUtils.targetRotation != null) {
            when (rotationStrafeValue.getValue().lowercase()) {
                "strict" -> {
                    val (yaw) = rotationUtils.targetRotation ?: return
                    var strafe = event.strafe
                    var forward = event.forward
                    val friction = event.friction

                    var f = strafe * strafe + forward * forward

                    if (f >= 1.0E-4F) {
                        f = MathHelper.sqrt_float(f)

                        if (f < 1.0F) {
                            f = 1.0F
                        }

                        f = friction / f
                        strafe *= f
                        forward *= f

                        val yawSin = MathHelper.sin((yaw * Math.PI / 180F).toFloat())
                        val yawCos = MathHelper.cos((yaw * Math.PI / 180F).toFloat())

                        mc.thePlayer.motionX += strafe * yawCos - forward * yawSin
                        mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin
                    }
                    event.isCancelled = true
                }

                "silent" -> {
                    // update()

                    rotationUtils.targetRotation.applyStrafeToPlayer(event)
                    event.isCancelled = true
                }
            }
        }
    }

    fun update() {
        if (cancelRun || (noInventoryAttackValue.equals("CancelRun") && (mc.currentScreen is GuiContainer || System.currentTimeMillis() - containerOpen < noInventoryDelayValue.getValue()))) {
            return
        }

        // Update target
        updateTargetValue()

        if (discoveredTargets.isEmpty()) {
            stopBlocking()
            return
        }

        // Target
        currentTarget = target

        if (!targetModeValue.equals("Switch") && (currentTarget != null && EntityUtils.isSelected(
                currentTarget!!, true
            ))
        ) {
            target = currentTarget
        }
    }

    /**
     * Update event
     */
    @EventTarget
    fun onUpdate() {
        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            discoveredTargets.clear()
            inRangeDiscoveredTargets.clear()
            return
        }

        if (noInventoryAttackValue.equals("CancelRun") && (mc.currentScreen is GuiContainer || System.currentTimeMillis() - containerOpen < noInventoryDelayValue.getValue())) {
            target = null
            currentTarget = null
            hitable = false
            if (mc.currentScreen is GuiContainer) containerOpen = System.currentTimeMillis()
            return
        }

        if (!rotationStrafeValue.equals("Off") && !mc.thePlayer.isRiding) {
            return
        }

        if (mc.thePlayer.isRiding) {
            update()
        }

        runAttackLoop()
    }

    /**
     * Render event
     */
    @EventTarget
    fun onRender3D(event: EventRender3D) {
        if (circleValue.getValue()) {
            GL11.glPushMatrix()
            GL11.glTranslated(
                mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.renderManager.renderPosX,
                mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.renderManager.renderPosY,
                mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.renderManager.renderPosZ
            )
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

            GL11.glLineWidth(circleThicknessValue.getValue())
            GL11.glColor4f(
                circleRedValue.getValue().toFloat() / 255.0F,
                circleGreenValue.getValue().toFloat() / 255.0F,
                circleBlueValue.getValue().toFloat() / 255.0F,
                circleAlphaValue.getValue().toFloat() / 255.0F
            )
            GL11.glRotatef(90F, 1F, 0F, 0F)
            GL11.glBegin(GL11.GL_LINE_STRIP)

            for (i in 0..360 step 5) { // You can change circle accuracy  (60 - accuracy)
                GL11.glVertex2f(
                    cos(i * Math.PI / 180.0).toFloat() * rangeValue.getValue(),
                    (sin(i * Math.PI / 180.0).toFloat() * rangeValue.getValue())
                )
            }

            GL11.glEnd()

            GL11.glDisable(GL11.GL_BLEND)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)

            GL11.glPopMatrix()
        }

        if (cancelRun) {
            target = null
            currentTarget = null
            hitable = false
            stopBlocking()
            discoveredTargets.clear()
            inRangeDiscoveredTargets.clear()
        }
        if (currentTarget != null && attackTimer.hasTimePassed(attackDelay) && currentTarget!!.hurtTime <= hurtTimeValue.getValue()) {
            clicks++
            attackTimer.reset()
            attackDelay = getAttackDelay(minCpsValue.getValue(), maxCpsValue.getValue())
        }

        discoveredTargets.forEach {
            when (markValue.getValue().lowercase()) {
                "liquid" -> {
                    RenderUtils.drawPlatform(
                        it, if (it.hurtTime <= 0) Color(37, 126, 255, 170) else Color(255, 0, 0, 170)
                    )
                }

                "block" -> {
                    val bb = it.entityBoundingBox
                    it.entityBoundingBox = bb.expand(0.2, 0.2, 0.2)
                    RenderUtils.drawEntityBox(
                        it,
                        if (it.hurtTime <= 0) if (it == target) Color.BLUE else Color.GREEN else Color.RED,
                        true,
                        true,
                        4f
                    )
                    it.entityBoundingBox = bb
                }

                "fdp" -> {
                    val drawTime = (System.currentTimeMillis() % 1500).toInt()
                    val drawMode = drawTime > 750
                    var drawPercent = drawTime / 750.0
                    // true when goes up
                    if (!drawMode) {
                        drawPercent = 1 - drawPercent
                    } else {
                        drawPercent -= 1
                    }
                    drawPercent = EaseUtils.easeInOutQuad(drawPercent)
                    mc.entityRenderer.disableLightmap()
                    GL11.glPushMatrix()
                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    GL11.glEnable(GL11.GL_LINE_SMOOTH)
                    GL11.glEnable(GL11.GL_BLEND)
                    GL11.glDisable(GL11.GL_DEPTH_TEST)

                    val bb = it.entityBoundingBox
                    val radius = ((bb.maxX - bb.minX) + (bb.maxZ - bb.minZ)) * 0.5f
                    val height = bb.maxY - bb.minY
                    val x =
                        it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                    val y =
                        (it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + height * drawPercent
                    val z =
                        it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    mc.entityRenderer.disableLightmap()
                    GL11.glLineWidth((radius * 8f).toFloat())
                    GL11.glBegin(GL11.GL_LINE_STRIP)
                    for (i in 0..360 step 10) {
                        RenderUtils.glColor(
                            Color.getHSBColor(
                                if (i < 180) {
                                    ColorUtils.rainbowStartValue + (ColorUtils.rainbowStopValue - ColorUtils.rainbowStartValue) * (i / 180f)
                                } else {
                                    ColorUtils.rainbowStartValue + (ColorUtils.rainbowStopValue - ColorUtils.rainbowStartValue) * (-(i - 360) / 180f)
                                }, 0.7f, 1.0f
                            )
                        )
                        GL11.glVertex3d(x - sin(i * Math.PI / 180F) * radius, y, z + cos(i * Math.PI / 180F) * radius)
                    }
                    GL11.glEnd()

                    GL11.glEnable(GL11.GL_DEPTH_TEST)
                    GL11.glDisable(GL11.GL_LINE_SMOOTH)
                    GL11.glDisable(GL11.GL_BLEND)
                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                    GL11.glPopMatrix()
                }

                "jello" -> {
                    val everyTime = 3000
                    val drawTime = (System.currentTimeMillis() % everyTime).toInt()
                    val drawMode = drawTime > (everyTime / 2)
                    var drawPercent = drawTime / (everyTime / 2.0)
                    // true when goes up
                    if (!drawMode) {
                        drawPercent = 1 - drawPercent
                    } else {
                        drawPercent -= 1
                    }
                    drawPercent = EaseUtils.easeInOutQuad(drawPercent)
                    mc.entityRenderer.disableLightmap()
                    GL11.glPushMatrix()
                    GL11.glDisable(GL11.GL_TEXTURE_2D)
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    GL11.glEnable(GL11.GL_LINE_SMOOTH)
                    GL11.glEnable(GL11.GL_BLEND)
                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                    GL11.glDisable(GL11.GL_CULL_FACE)
                    GL11.glShadeModel(7425)
                    mc.entityRenderer.disableLightmap()

                    val bb = it.entityBoundingBox
                    val radius = ((bb.maxX - bb.minX) + (bb.maxZ - bb.minZ)) * 0.5f
                    val height = bb.maxY - bb.minY
                    val x =
                        it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                    val y =
                        (it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + height * drawPercent
                    val z =
                        it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    val eased = (height / 3) * (if (drawPercent > 0.5) {
                        1 - drawPercent
                    } else {
                        drawPercent
                    }) * (if (drawMode) {
                        -1
                    } else {
                        1
                    })
                    for (i in 5..360 step 5) {
                        val color = Color.getHSBColor(
                            if (i < 180) {
                                ColorUtils.rainbowStartValue + (ColorUtils.rainbowStopValue - ColorUtils.rainbowStartValue) * (i / 180f)
                            } else {
                                ColorUtils.rainbowStartValue + (ColorUtils.rainbowStopValue - ColorUtils.rainbowStartValue) * (-(i - 360) / 180f)
                            }, 0.7f, 1.0f
                        )
                        val x1 = x - sin(i * Math.PI / 180F) * radius
                        val z1 = z + cos(i * Math.PI / 180F) * radius
                        val x2 = x - sin((i - 5) * Math.PI / 180F) * radius
                        val z2 = z + cos((i - 5) * Math.PI / 180F) * radius
                        GL11.glBegin(GL11.GL_QUADS)
                        RenderUtils.glColor(color, 0f)
                        GL11.glVertex3d(x1, y + eased, z1)
                        GL11.glVertex3d(x2, y + eased, z2)
                        RenderUtils.glColor(color, 150f)
                        GL11.glVertex3d(x2, y, z2)
                        GL11.glVertex3d(x1, y, z1)
                        GL11.glEnd()
                    }

                    GL11.glEnable(GL11.GL_CULL_FACE)
                    GL11.glShadeModel(7424)
                    GL11.glColor4f(1f, 1f, 1f, 1f)
                    GL11.glEnable(GL11.GL_DEPTH_TEST)
                    GL11.glDisable(GL11.GL_LINE_SMOOTH)
                    GL11.glDisable(GL11.GL_BLEND)
                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                    GL11.glPopMatrix()
                }

                "sims" -> {
                    val radius = 0.15f
                    val side = 4
                    GL11.glPushMatrix()
                    GL11.glTranslated(
                        it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX,
                        (it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + it.height * 1.1,
                        it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                    )
                    GL11.glRotatef(-it.width, 0.0f, 1.0f, 0.0f)
                    GL11.glRotatef((mc.thePlayer.ticksExisted + mc.timer.renderPartialTicks) * 5, 0f, 1f, 0f)
                    RenderUtils.glColor(if (it.hurtTime <= 0) Color(80, 255, 80) else Color(255, 0, 0))
                    RenderUtils.enableSmoothLine(1.5F)
                    val c = Cylinder()
                    GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f)
                    c.draw(0F, radius, 0.3f, side, 1)
                    c.drawStyle = 100012
                    GL11.glTranslated(0.0, 0.0, 0.3)
                    c.draw(radius, 0f, 0.3f, side, 1)
                    GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f)
                    GL11.glTranslated(0.0, 0.0, -0.3)
                    c.draw(0F, radius, 0.3f, side, 1)
                    GL11.glTranslated(0.0, 0.0, 0.3)
                    c.draw(radius, 0F, 0.3f, side, 1)
                    RenderUtils.disableSmoothLine()
                    GL11.glPopMatrix()
                }
            }
        }
    }

    /**
     * Handle entity move
     */
//    @EventTarget
//    fun onEntityMove(event: EntityMovementEvent) {
//        val movedEntity = event.movedEntity
//
//        if (target == null || movedEntity != currentTarget)
//            return
//
//        updateHitable()
//    }

    private fun runAttackLoop() {
        if (clicks <= 0 && canSwing && swingTimer.hasTimePassed(swingDelay)) {
            swingTimer.reset()
            swingDelay = getAttackDelay(minCpsValue.getValue(), maxCpsValue.getValue())
            runSwing()
            return
        }

        try {
            while (clicks > 0) {
                runAttack()
                clicks--
            }
        } catch (e: java.lang.IllegalStateException) {
            return
        }
    }

    /**
     * Attack enemy
     */
    private fun runAttack() {
        target ?: return
        currentTarget ?: return

        // Settings
        val failRate = failRateValue.getValue()
        val openInventory = noInventoryAttackValue.equals("Spoof") && mc.currentScreen is GuiInventory
        val failHit = failRate > 0 && Random().nextInt(100) <= failRate

        // Check is not hitable or check failrate
        if (hitable && !failHit) {
            // Close inventory when open
            if (openInventory) {
                mc.netHandler.addToSendQueue(C0DPacketCloseWindow())
            }

            // Attack
            if (!targetModeValue.equals("Multi")) {
                attackEntity(currentTarget!!)
            } else {
                inRangeDiscoveredTargets.forEachIndexed { index, entity ->
                    if (limitedMultiTargetsValue.getValue() == 0 || index < limitedMultiTargetsValue.getValue()) {
                        attackEntity(entity)
                    }
                }
            }

            if (targetModeValue.equals("Switch")) {
                if (switchTimer.hasTimePassed(switchDelayValue.getValue().toLong())) {
                    prevTargetEntities.add(if (aacValue.getValue()) target!!.entityId else currentTarget!!.entityId)
                    switchTimer.reset()
                }
            } else {
                prevTargetEntities.add(if (aacValue.getValue()) target!!.entityId else currentTarget!!.entityId)
            }

            if (target == currentTarget) {
                target = null
            }

            // Open inventory
            if (openInventory) {
                mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
            }
        } else if (fakeSwingValue.getValue()) {
            runSwing()
        }
    }

    /**
     * Update current target
     */
    private fun updateTargetValue() {
        // Settings
        val hurtTime = hurtTimeValue.getValue()
        val fov = fovValue.getValue()
        val switchMode = targetModeValue.equals("Switch")

        // Find possible targets
        discoveredTargets.clear()

        for (entity in mc.theWorld.loadedEntityList) {
            if (entity !is EntityLivingBase || !EntityUtils.isSelected(
                    entity, true
                ) || (switchMode && prevTargetEntities.contains(entity.entityId))
            ) {
                continue
            }

            val distance = mc.thePlayer.getDistanceToEntity(entity)
            val entityFov = rotationUtils.getRotationDifference(entity)

            if (distance <= discoverRangeValue.getValue() && (fov == 180F || entityFov <= fov) && entity.hurtTime <= hurtTime) {
                discoveredTargets.add(entity)
            }
        }

        // Sort targets by priority
        when (priorityValue.getValue().lowercase()) {
            "distance" -> discoveredTargets.sortBy { mc.thePlayer.getDistanceToEntity(it) } // Sort by distance
            "health" -> discoveredTargets.sortBy { it.health } // Sort by health
            "fov" -> discoveredTargets.sortBy { rotationUtils.getRotationDifference(it) } // Sort by FOV
            "livingtime" -> discoveredTargets.sortBy { -it.ticksExisted } // Sort by existence
            "armor" -> discoveredTargets.sortBy { it.totalArmorValue } // Sort by armor
            "hurtresistanttime" -> discoveredTargets.sortBy { it.hurtResistantTime } // Sort by armor
        }

        inRangeDiscoveredTargets.clear()
        inRangeDiscoveredTargets.addAll(discoveredTargets.filter { mc.thePlayer.getDistanceToEntity(it) < getRange(it) })

        // Cleanup last targets when no targets found and try again
        if (inRangeDiscoveredTargets.isEmpty() && prevTargetEntities.isNotEmpty()) {
            prevTargetEntities.clear()
            updateTargetValue()
            return
        }

        // Find best target
        for (entity in discoveredTargets) {
            // Update rotations to current target
            if (!updateRotations(entity)) { // when failed then try another target
                continue
            }

            // Set target to current entity
            if (mc.thePlayer.getDistanceToEntity(entity) < maxRange) {
                target = entity
                canSwing = false
                return
            }
        }

        target = null
        canSwing = discoveredTargets.find { mc.thePlayer.getDistanceToEntity(it) < swingRangeValue.getValue() } != null
    }

    private fun runSwing() {
        val swing = swingValue.getValue()
        if (swing.equals("packet", true)) {
            mc.netHandler.addToSendQueue(C0APacketAnimation())
        } else if (swing.equals("normal", true)) {
            mc.thePlayer.swingItem()
        }
    }

    /**
     * Attack [entity]
     * @throws IllegalStateException when bad packets protection
     */
    private fun attackEntity(entity: EntityLivingBase) {
        if (packetSent && noBadPacketsValue.getValue()) {
            throw java.lang.IllegalStateException("Attack canceled because of bad packets protection")
        }
        // Call attack event
        val event = EventAttack(entity)
        EventManager.instance.call(event)
        if (event.isCancelled) {
            return
        }

        // Stop blocking
        if (!autoBlockPacketValue.equals("Vanilla") && (mc.thePlayer.isBlocking || blockingStatus)) {
            mc.netHandler.addToSendQueue(
                C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN
                )
            )
            blockingStatus = false
            if (noBadPacketsValue.getValue()) {
                packetSent = true
                throw java.lang.IllegalStateException("Attack canceled because of bad packets protection")
            }
        }

        // Attack target
        runSwing()

        packetSent = true
        mc.netHandler.addToSendQueue(C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK))

        if (keepSprintValue.getValue()) {
            // Enchant Effect
            if (EnchantmentHelper.getModifierForCreature(mc.thePlayer.heldItem, entity.creatureAttribute) > 0F) {
                mc.thePlayer.onEnchantmentCritical(entity)
            }
        } else {
            if (mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
                mc.thePlayer.attackTargetEntityWithCurrentItem(entity)
            }
        }

        // Start blocking after attack
        if (mc.thePlayer.isBlocking || (autoBlockValue.equals("Range") && canBlock)) {
            if (autoBlockPacketValue.equals("AfterTick")) {
                return
            }

            if (!(blockRateValue.getValue() > 0 && Random().nextInt(100) <= blockRateValue.getValue())) {
                return
            }

            startBlocking(entity, interactAutoBlockValue.getValue())
        }
    }

    /**
     * Update killaura rotations to enemy
     */
    private fun updateRotations(entity: Entity): Boolean {
        if (rotationModeValue.equals("None")) {
            return true
        }

        var boundingBox = entity.entityBoundingBox

        if (predictValue.getValue() && rotationModeValue.getValue() != "Test") {
            boundingBox = boundingBox.offset(
                (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(
                    minPredictSizeValue.getValue(), maxPredictSizeValue.getValue()
                ), (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(
                    minPredictSizeValue.getValue(), maxPredictSizeValue.getValue()
                ), (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(
                    minPredictSizeValue.getValue(), maxPredictSizeValue.getValue()
                )
            )
        }
        val rModes = when (rotationModeValue.getValue()) {
            "LiquidBounce", "SmoothLiquid", "Derp" -> "LiquidBounce"
            "ForceCenter", "SmoothCenter", "OldMatrix", "Spin", "FastSpin" -> "CenterLine"
            "LockView" -> "CenterSimple"
            "Test" -> "HalfUp"
            else -> "LiquidBounce"
        }

        val (_, directRotation) = rotationUtils.calculateCenter(
            rModes,
            randomCenterModeValue.getValue(),
            (randomCenRangeValue.getValue()).toDouble(),
            boundingBox,
            predictValue.getValue() && rotationModeValue.getValue() != "Test",
            mc.thePlayer.getDistanceToEntity(entity) <= throughWallsRangeValue.getValue()
        ) ?: return false

        if (rotationModeValue.getValue() == "OldMatrix") directRotation.pitch = (89.9).toFloat()

        var diffAngle = rotationUtils.getRotationDifference(rotationUtils.serverRotation, directRotation)
        if (diffAngle < 0) diffAngle = -diffAngle
        if (diffAngle > 180.0) diffAngle = 180.0

        val calculateSpeed = when (rotationSmoothModeValue.getValue()) {
            "Custom" -> diffAngle / rotationSmoothValue.getValue()
            "Line" -> (diffAngle / 180) * maxTurnSpeedValue.getValue() + (1 - diffAngle / 180) * minTurnSpeedValue.getValue()
            //"Quad" -> Math.pow((diffAngle / 180.0), 2.0) * maxTurnSpeedValue.getValue() + (1 - Math.pow((diffAngle / 180.0), 2.0)) * minTurnSpeedValue.getValue()
            "Quad" -> (diffAngle / 180.0).pow(2.0) * maxTurnSpeedValue.getValue() + (1 - (diffAngle / 180.0).pow(2.0)) * minTurnSpeedValue.getValue()
            "Sine" -> (-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5) * maxTurnSpeedValue.getValue() + (cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5) * minTurnSpeedValue.getValue()
            //"QuadSine" -> Math.pow(-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5, 2.0) * maxTurnSpeedValue.getValue() + (1 - Math.pow(-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5, 2.0)) * minTurnSpeedValue.getValue()
            "QuadSine" -> (-cos(diffAngle / 180 * Math.PI) * 0.5 + 0.5).pow(2.0) * maxTurnSpeedValue.getValue() + (1 - (-cos(
                diffAngle / 180 * Math.PI
            ) * 0.5 + 0.5).pow(2.0)) * minTurnSpeedValue.getValue()

            else -> 180.0
        }

        val rotation = when (rotationModeValue.getValue()) {
            "LiquidBounce", "ForceCenter" -> rotationUtils.limitAngleChange(
                rotationUtils.serverRotation,
                directRotation,
                (Math.random() * (maxTurnSpeedValue.getValue() - minTurnSpeedValue.getValue()) + minTurnSpeedValue.getValue()).toFloat()
            )

            "LockView" -> rotationUtils.limitAngleChange(
                rotationUtils.serverRotation, directRotation, (180.0).toFloat()
            )

            "SmoothCenter", "SmoothLiquid", "OldMatrix" -> rotationUtils.limitAngleChange(
                rotationUtils.serverRotation, directRotation, (calculateSpeed).toFloat()
            )

            "Test" -> rotationUtils.limitAngleChange(
                rotationUtils.serverRotation, directRotation, (calculateSpeed).toFloat()
            )

            else -> return true
        }

        if (silentRotationValue.getValue()) {
            if (rotationRevTickValue.getValue() > 0 && rotationRevValue.getValue()) {
                if (keepDirectionValue.getValue()) {
                    rotationUtils.setTargetRotationReverse(
                        rotation, keepDirectionTickValue.getValue(), rotationRevTickValue.getValue()
                    )
                } else {
                    rotationUtils.setTargetRotationReverse(rotation, 1, rotationRevTickValue.getValue())
                }
            } else {
                if (keepDirectionValue.getValue()) {
                    rotationUtils.setTargetRotation(rotation, keepDirectionTickValue.getValue())
                } else {
                    rotationUtils.setTargetRotation(rotation, 1)
                }
            }
        } else {
            rotation.toPlayer(mc.thePlayer)
        }
        return true
    }


    private var maxRange = 6.0

    /**
     * Check if enemy is hitable with current rotations
     */
    private fun updateHitable() {
        if (hitableValue.getValue()) {
            hitable = true
            return
        }
        // Disable hitable check if turn speed is zero
        if (maxTurnSpeedValue.getValue() <= 0F) {
            hitable = true
            return
        }

        val reach = maxRange.toDouble()

        if (raycastValue.getValue()) {
            val raycastedEntity = RaycastUtils.raycastEntity(reach) {
                (!livingRaycastValue.getValue() || it is EntityLivingBase && it !is EntityArmorStand) && (EntityUtils.isSelected(
                    it, true
                ) || raycastIgnoredValue.getValue() || aacValue.getValue() && mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                    it, it.entityBoundingBox
                ).isNotEmpty())
            }

            if (raycastValue.getValue() && raycastedEntity is EntityLivingBase) {
                currentTarget = raycastedEntity
            }

            hitable = if (!rotationModeValue.equals("None")) currentTarget == raycastedEntity else true
        } else {
            hitable = rotationUtils.isFaced(currentTarget, reach)
        }
    }

    /**
     * Start blocking
     */
    private fun startBlocking(interactEntity: Entity, interact: Boolean) {
        if (autoBlockValue.equals("Range") && mc.thePlayer.getDistanceToEntity(interactEntity) > autoBlockRangeValue.getValue()) {
            return
        }

        if (blockingStatus) {
            return
        }

        if (packetSent && noBadPacketsValue.getValue()) {
            return
        }

        if (interact) {
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, interactEntity.positionVector))
            mc.netHandler.addToSendQueue(C02PacketUseEntity(interactEntity, C02PacketUseEntity.Action.INTERACT))
        }

        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()))
        blockingStatus = true
        packetSent = true
    }

    /**
     * Stop blocking
     */
    private fun stopBlocking() {
        if (blockingStatus) {
            if (packetSent && noBadPacketsValue.getValue()) {
                return
            }
            mc.netHandler.addToSendQueue(
                C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    if (MovementUtils.isMoving()) BlockPos(-1, -1, -1) else BlockPos.ORIGIN,
                    EnumFacing.DOWN
                )
            )
            blockingStatus = false
            packetSent = true
        }
    }

    /**
     * Attack Delay
     */
    private fun getAttackDelay(minCps: Int, maxCps: Int): Long {
        var delay = TimeUtils.randomClickDelay(minCps.coerceAtMost(maxCps), minCps.coerceAtLeast(maxCps))
        if (combatDelayValue.getValue()) {
            var value = 4.0
            if (mc.thePlayer.inventory.getCurrentItem() != null) {
                when (mc.thePlayer.inventory.getCurrentItem().item) {
                    is ItemSword -> {
                        value -= 2.4
                    }

                    is ItemPickaxe -> {
                        value -= 2.8
                    }

                    is ItemAxe -> {
                        value -= 3
                    }
                }
            }
            delay = delay.coerceAtLeast((1000 / value).toLong())
        }
        return delay
    }

    /**
     * Check if [entity] is alive
     */
    private fun isAlive(entity: EntityLivingBase) =
        entity.isEntityAlive && entity.health > 0 || aacValue.getValue() && entity.hurtTime > 3

    private fun getRange(entity: Entity) =
        (if (mc.thePlayer.getDistanceToEntity(entity) >= throughWallsRangeValue.getValue()) rangeValue.getValue() else throughWallsRangeValue.getValue())
}