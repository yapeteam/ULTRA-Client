/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.timer.ultra.module.modules.cheat

import cn.timer.ultra.module.Category
import cn.timer.ultra.module.CombatModule
import cn.timer.ultra.values.Booleans
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemArmor
import org.lwjgl.input.Keyboard

class Teams : CombatModule("Team", Keyboard.KEY_NONE, Category.Cheat) {

    private val scoreboardValue = Booleans("ScoreboardTeam", true)
    private val colorValue = Booleans("Color", true)
    private val gommeSWValue = Booleans("GommeSW", false)
    private val armorValue = Booleans("ArmorColor", false)

    init {
        addValues(scoreboardValue, colorValue, gommeSWValue, armorValue)
    }

    /**
     * Check if [entity] is in your own team using scoreboard, name color or team prefix
     */
    fun isInYourTeam(entity: EntityLivingBase): Boolean {
        mc.thePlayer ?: return false

        if (scoreboardValue.getValue() && mc.thePlayer.team != null && entity.team != null &&
            mc.thePlayer.team.isSameTeam(entity.team)
        ) {
            return true
        }
        if (gommeSWValue.getValue() && mc.thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "")
            val clientName = mc.thePlayer.displayName.formattedText.replace("§r", "")
            if (targetName.startsWith("T") && clientName.startsWith("T")) {
                if (targetName[1].isDigit() && clientName[1].isDigit()) {
                    return targetName[1] == clientName[1]
                }
            }
        }
        if (armorValue.getValue()) {
            val entityPlayer = entity as EntityPlayer
            if (mc.thePlayer.inventory.armorInventory[3] != null && entityPlayer.inventory.armorInventory[3] != null) {
                val myHead = mc.thePlayer.inventory.armorInventory[3]
                val myItemArmor = myHead.item as ItemArmor

                val entityHead = entityPlayer.inventory.armorInventory[3]
                val entityItemArmor = myHead.item as ItemArmor

                if (myItemArmor.getColor(myHead) == entityItemArmor.getColor(entityHead)) {
                    return true
                }
            }
        }
        if (colorValue.getValue() && mc.thePlayer.displayName != null && entity.displayName != null) {
            val targetName = entity.displayName.formattedText.replace("§r", "")
            val clientName = mc.thePlayer.displayName.formattedText.replace("§r", "")
            return targetName.startsWith("§${clientName[1]}")
        }

        return false
    }
}
