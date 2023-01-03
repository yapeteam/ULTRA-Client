package cn.timer.ultra.module.modules.chest.movement;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventMove;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Timer", "Potion", "BHop", "NCP"}, "BHop");
    public Speed() {
        super("Speed", Keyboard.KEY_U, Category.Movement);
        this.addValues(mode);
    }

    @EventTarget
    public void onMove(EventMove e) {
        if (mode.isCurrentMode("Timer")) {
            mc.timer.timerSpeed = 2.5f;
        } else {
            mc.timer.timerSpeed = 1.0f;
        }
        if (mode.isCurrentMode("Potion")) {
            mc.thePlayer.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1, 1));
        } else {
            mc.thePlayer.removePotionEffect(Potion.moveSpeed.id);
        }
        if (mode.isCurrentMode("BHop")) {
            if (mc.thePlayer.moveForward == 0.0F && mc.thePlayer.moveStrafing == 0.0F) {
                return;
            }
            mc.timer.timerSpeed = 1.2F;
            double moveSpeed = this.getBaseMoveSpeed();
            if (mc.thePlayer.onGround) {
                e.setY(mc.thePlayer.motionY = 0.4D);
                moveSpeed *= 2;
            } else {
                if (mc.thePlayer.motionY < 0.2) {
                    moveSpeed = 1.89 * this.getBaseMoveSpeed();
                }
            }
            e.setMoveSpeed(moveSpeed);
        }
        if (mode.isCurrentMode("NCP")) {
            if (mc.thePlayer.movementInput.moveForward != 0.0D || mc.thePlayer.movementInput.moveStrafe != 0.0D) {
                if (mc.thePlayer.onGround) e.setY(mc.thePlayer.motionY = 0.42D);
                e.setMoveSpeed(this.getBaseMoveSpeed());
            }
        }
    }

    private double getBaseMoveSpeed() {
        double moveSpeed = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            moveSpeed += (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) * 0.2;
        }
        return moveSpeed;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
    }
}
