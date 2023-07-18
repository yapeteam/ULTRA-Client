/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.timer.ultra.utils.fdp

import cn.timer.ultra.Client
import cn.timer.ultra.module.modules.cheat.Target
import cn.timer.ultra.module.modules.cheat.Teams
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.monster.EntityGhast
import net.minecraft.entity.monster.EntityGolem
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer

object EntityUtils : MinecraftInstance() {
    fun isSelected(entity: Entity, canAttackCheck: Boolean): Boolean {
        if (entity is EntityLivingBase && (Target.deadValue.getValue() || entity.isEntityAlive()) && entity !== mc.thePlayer) {
            if (Target.invisibleValue.getValue() || !entity.isInvisible()) {
                if (Target.playerValue.getValue() && entity is EntityPlayer) {
                    if (canAttackCheck) {
                        if (entity.isSpectator) {
                            return false
                        }

                        if (entity.isPlayerSleeping) {
                            return false
                        }

                        val teams = Client.instance.moduleManager.getByClass(Teams::class.java)
                        return !teams!!.isEnabled || !teams.isInYourTeam(entity)
                    }

                    return true
                }
                return Target.mobValue.getValue() && isMob(entity) || Target.animalValue.getValue() && isAnimal(entity)
            }
        }
        return false
    }

    fun isAnimal(entity: Entity): Boolean {
        return entity is EntityAnimal || entity is EntitySquid || entity is EntityGolem || entity is EntityVillager || entity is EntityBat
    }

    fun isMob(entity: Entity): Boolean {
        return entity is EntityMob || entity is EntitySlime || entity is EntityGhast || entity is EntityDragon
    }
}