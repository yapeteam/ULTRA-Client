package cn.timer.ultra.module.modules.movement;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventMove;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Vanilla", "Float", "Glide", "Motion"}, "Glide");
    private final Numbers<Double> speed = new Numbers<>("MotionSpeed", 0.5, 5.0, 0.5, 1.0);

    public Fly() {
        super("Fly", Keyboard.KEY_F, Category.Movement);
        this.addValues(this.mode, this.speed);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.capabilities.allowFlying = false;
        mc.thePlayer.capabilities.isFlying = false;
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
        if (mode.isCurrentMode("Vanilla")) {
            if (mc.thePlayer.capabilities.isCreativeMode || mc.thePlayer.capabilities.isFlying) {
                return;
            }
            mc.thePlayer.capabilities.allowFlying = true;
            mc.thePlayer.capabilities.isFlying = true;
        } else if (mc.thePlayer.capabilities.allowFlying || mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.capabilities.allowFlying = false;
            mc.thePlayer.capabilities.isFlying = false;
        }
        if (mode.isCurrentMode("Float")) {
            mc.thePlayer.motionY = 0.0D;
        }
        if (mode.isCurrentMode("Glide") && !mc.thePlayer.onGround) {
            if (mc.thePlayer.motionY < 0.1D) {
                mc.thePlayer.motionY = -0.02D;
            }
        }
        if (mode.isCurrentMode("Motion")) {
            mc.thePlayer.motionY = 0.0D;
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY = 0.4 * this.speed.getValue();
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.thePlayer.onGround) {
                mc.thePlayer.motionY = -0.4 * this.speed.getValue();
            }
        }
    }

    @EventTarget
    public void onMove(EventMove e) {
        if (mode.isCurrentMode("Motion")) {
            if (mc.thePlayer.movementInput.moveForward != 0.0f || mc.thePlayer.movementInput.moveStrafe != 0.0F) {
                e.setMotion(this.speed.getValue() * this.getBaseMoveSpeed());
            } else {
                e.setMotion(0.0D);
            }
        }
    }

    private double getBaseMoveSpeed() {
        double base = 0.2873D;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            base += 0.2 * (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return base;
    }
}
