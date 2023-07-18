/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.timer.ultra.module.modules.cheat

import cn.timer.ultra.event.EventTarget
import cn.timer.ultra.event.events.EventPacket
import cn.timer.ultra.event.events.EventPreUpdate
import cn.timer.ultra.event.events.EventRender3D
import cn.timer.ultra.module.Category
import cn.timer.ultra.module.CombatModule
import cn.timer.ultra.utils.fdp.*
import cn.timer.ultra.values.Booleans
import cn.timer.ultra.values.Mode
import cn.timer.ultra.values.Numbers
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.Vec3
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.concurrent.thread

class InfiniteAura : CombatModule("InfiniteAura", Keyboard.KEY_NONE, Category.Cheat) {
    private val modeValue = Mode("Mode", arrayOf("Aura", "Click"), "Aura")
    private val targetsValue = Numbers("Targets", 1, 10, 3)
    private val cpsValue = Numbers("CPS", 1, 10, 1)
    private val distValue = Numbers("Distance", 20, 100, 30)
    private val moveDistanceValue = Numbers("MoveDistance", 2F, 15F, 5F)
    private val noRegenValue = Booleans("NoRegen", true)
    private val swingValue = Booleans("Swing", true)
    private val pathRenderValue = Booleans("PathRender", true)
    private val colorRedValue =
        Numbers("ColorRed", 0, 255, 0)
    private val colorGreenValue =
        Numbers("ColorGreen", 0, 255, 160)
    private val colorBlueValue =
        Numbers("ColorBlue", 0, 255, 255)
    private val colorAlphaValue = Numbers("ColorAlpha", 0, 255, 150)
    private val colorRainbowValue = Booleans("Rainbow", false)

    private val timer = MSTimer()
    private var points = mutableListOf<Vec3>()
    private var thread: Thread? = null

    init {
        addValues(
            modeValue,
            targetsValue,
            cpsValue,
            distValue,
            moveDistanceValue,
            noRegenValue,
            swingValue,
            pathRenderValue,
            colorRedValue,
            colorGreenValue,
            colorBlueValue,
            colorAlphaValue,
            colorRainbowValue
        )
    }

    private fun getDelay(): Int {
        return 1000 / cpsValue.getValue()
    }

    override fun onEnable() {
        timer.reset()
        points.clear()
    }

    override fun onDisable() {
        timer.reset()
        points.clear()
        thread?.stop()
    }

    @EventTarget
    fun onUpdate(event: EventPreUpdate) {
        suffix = modeValue.value
        if (!timer.hasTimePassed(getDelay().toLong())) return
        if (thread?.isAlive == true) return
        when (modeValue.getValue().lowercase()) {
            "aura" -> {
                thread = thread(name = "InfiniteAura") {
                    // do it async because a* pathfinding need some time
                    doTpAura()
                }
                points.clear()
                timer.reset()
            }

            "click" -> {
                if (mc.gameSettings.keyBindAttack.isKeyDown) {
                    thread = thread(name = "InfiniteAura") {
                        // do it async because a* pathfinding need some time
                        val entity = RaycastUtils.raycastEntity(
                            distValue.floatValue().toDouble()
                        ) { entity -> entity != null && EntityUtils.isSelected(entity, true) } ?: return@thread
                        if (mc.thePlayer.getDistanceToEntity(entity) < 3) {
                            return@thread
                        }

                        hit(entity as EntityLivingBase, true)
                    }
                    timer.reset()
                }
                points.clear()
            }
        }
    }

    private fun doTpAura() {
        try {
            val targets = mc.theWorld.loadedEntityList.filter {
                it is EntityLivingBase &&
                        EntityUtils.isSelected(it, true) &&
                        mc.thePlayer.getDistanceToEntity(it) < distValue.floatValue()
            }.toMutableList()
            if (targets.isEmpty()) return
            targets.sortBy { mc.thePlayer.getDistanceToEntity(it) }

            var count = 0
            for (entity in targets) {

                if (hit(entity as EntityLivingBase)) {
                    count++
                }
                if (count > targetsValue.getValue()) break
            }
        } catch (_: ConcurrentModificationException) {
        }
    }

    private fun hit(entity: EntityLivingBase, force: Boolean = false): Boolean {
        val path = PathUtils.findBlinkPath(
            mc.thePlayer.posX,
            mc.thePlayer.posY,
            mc.thePlayer.posZ,
            entity.posX,
            entity.posY,
            entity.posZ,
            moveDistanceValue.floatValue().toDouble()
        )
        if (path.isEmpty()) return false
        val lastDistance = path.last().let { entity.getDistance(it.xCoord, it.yCoord, it.zCoord) }
        if (!force && lastDistance > 10) return false // pathfinding has failed

        path.forEach {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(it.xCoord, it.yCoord, it.zCoord, true))
            points.add(it)
        }

        if (lastDistance > 3) {
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(entity.posX, entity.posY, entity.posZ, true))
        }

        if (swingValue.getValue()) {
            mc.thePlayer.swingItem()
        }
        mc.playerController.attackEntity(mc.thePlayer, entity)

        for (i in path.size - 1 downTo 0) {
            val vec = path[i]
            mc.netHandler.addToSendQueue(C04PacketPlayerPosition(vec.xCoord, vec.yCoord, vec.zCoord, true))
        }
        mc.netHandler.addToSendQueue(
            C04PacketPlayerPosition(
                mc.thePlayer.posX,
                mc.thePlayer.posY,
                mc.thePlayer.posZ,
                true
            )
        )

        return true
    }

    @EventTarget
    fun onPacket(event: EventPacket) {
        if (event.packet is S08PacketPlayerPosLook) {
            timer.reset()
        }
        val isMovePacket =
            (event.packet is C04PacketPlayerPosition || event.packet is C03PacketPlayer.C06PacketPlayerPosLook)
        if (noRegenValue.getValue() && event.packet is C03PacketPlayer && !isMovePacket) {
            event.isCancelled = true
        }
    }

    @EventTarget
    fun onRender3D(event: EventRender3D) {
        synchronized(points) {
            if (points.isEmpty() || !pathRenderValue.getValue()) return
            val renderPosX = mc.renderManager.viewerPosX
            val renderPosY = mc.renderManager.viewerPosY
            val renderPosZ = mc.renderManager.viewerPosZ

            GL11.glPushMatrix()
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glShadeModel(GL11.GL_SMOOTH)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LIGHTING)
            GL11.glDepthMask(false)

            RenderUtils.glColor(
                if (colorRainbowValue.getValue()) {
                    ColorUtils.rainbow()
                } else {
                    Color(colorRedValue.getValue(), colorGreenValue.getValue(), colorBlueValue.getValue())
                }, colorAlphaValue.getValue()
            )

            for (vec in points) {
                val x = vec.xCoord - renderPosX
                val y = vec.yCoord - renderPosY
                val z = vec.zCoord - renderPosZ
                val width = 0.3
                val height = mc.thePlayer.getEyeHeight().toDouble()
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2)
                GL11.glLineWidth(2F)
                GL11.glBegin(GL11.GL_LINE_STRIP)
                GL11.glVertex3d(x - width, y, z - width)
                GL11.glVertex3d(x - width, y, z - width)
                GL11.glVertex3d(x - width, y + height, z - width)
                GL11.glVertex3d(x + width, y + height, z - width)
                GL11.glVertex3d(x + width, y, z - width)
                GL11.glVertex3d(x - width, y, z - width)
                GL11.glVertex3d(x - width, y, z + width)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_LINE_STRIP)
                GL11.glVertex3d(x + width, y, z + width)
                GL11.glVertex3d(x + width, y + height, z + width)
                GL11.glVertex3d(x - width, y + height, z + width)
                GL11.glVertex3d(x - width, y, z + width)
                GL11.glVertex3d(x + width, y, z + width)
                GL11.glVertex3d(x + width, y, z - width)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_LINE_STRIP)
                GL11.glVertex3d(x + width, y + height, z + width)
                GL11.glVertex3d(x + width, y + height, z - width)
                GL11.glEnd()
                GL11.glBegin(GL11.GL_LINE_STRIP)
                GL11.glVertex3d(x - width, y + height, z + width)
                GL11.glVertex3d(x - width, y + height, z - width)
                GL11.glEnd()
            }

            GL11.glDepthMask(true)
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
            GL11.glColor4f(1F, 1F, 1F, 1F)
        }
    }
}