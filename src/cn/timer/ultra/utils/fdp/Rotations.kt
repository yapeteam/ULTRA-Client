/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.timer.ultra.utils.fdp

import cn.timer.ultra.module.Category
import cn.timer.ultra.module.CombatModule
import cn.timer.ultra.values.Booleans
import cn.timer.ultra.values.Mode
import org.lwjgl.input.Keyboard

class Rotations : CombatModule("Rotations", Keyboard.KEY_NONE, Category.Cheat) {
    private val headValue = Booleans("Head", true)
    private val bodyValue = Booleans("Body", true)
    val fixedValue = Mode("SensitivityFixed", arrayOf("None", "Old", "New"), "New")
    val nanValue = Booleans("NaNCheck", true)

    init {
        addValues(headValue, bodyValue, fixedValue, nanValue)
    }
}
