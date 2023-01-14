package cn.timer.ultra.module.modules.player;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventLoop;
import cn.timer.ultra.event.events.EventPostUpdate;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.Rotation;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Numbers;
import org.lwjgl.input.Keyboard;

public class Derp extends Module {
    public Derp() {
        super("Derp", Keyboard.KEY_NONE, Category.Player);
        addValues(spinyValue, incrementValue, fakeDerp);
    }

    private final Booleans spinyValue = new Booleans("Spiny", true);
    private final Numbers<Double> incrementValue = new Numbers<>("Increment", 0d, 50d, 1d, 1d);
    private final Booleans fakeDerp = new Booleans("Fake Derp", true);
    private float currentSpin = 0F;
    private static float targetYaw = 0f;
    private static float targetPitch = 0f;

    public static boolean enable;

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;
        currentSpin = mc.thePlayer.rotationYaw;
    }

    @EventTarget
    public void onPreUpdate(EventPreUpdate event) {
        if (!this.isEnabled() || fakeDerp.getValue()) {
            return;
        }
        event.setYaw(targetYaw);
        event.setPitch(targetPitch);
    }

    @EventTarget
    public void onLoop(EventLoop e) {
        enable = this.isEnabled() && !fakeDerp.getValue();
    }

    @EventTarget
    public void onUpdate(EventPostUpdate e) {
        Rotation rotation = getRotation();
        setTargetRotation(rotation);
        Client.instance.rotationUtils.setRotation(rotation);
    }

    private Rotation getRotation() {
        float s1 = (float) (mc.thePlayer.rotationYaw + (Math.random() * 360 - 180));
        float s2 = (float) (Math.random() * 180 - 90);

        if (spinyValue.getValue()) {
            s1 = currentSpin + incrementValue.getValue().intValue();
            currentSpin = s1;
            s2 = 0;
        }

        return new Rotation(s1, s2);
    }

    public static void setTargetRotation(Rotation rotation/*, final int keepLength*/) {
        targetYaw = rotation.getYaw();
        targetPitch = rotation.getPitch();
    }
}
