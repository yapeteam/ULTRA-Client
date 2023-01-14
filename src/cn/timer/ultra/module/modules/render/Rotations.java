package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.module.modules.player.Derp;
import cn.timer.ultra.values.Booleans;
import org.lwjgl.input.Keyboard;

public class Rotations extends Module {
    private final Booleans silentRotation = new Booleans("Silent", true);

    public Rotations() {
        super("Rotations", Keyboard.KEY_NONE, Category.Render);
        addValues(silentRotation);
        setEnabled(true);
    }

    private void setRotating() {
        Client.instance.rotationUtils.rotating = (
                Client.instance.getModuleManager().getByClass(Derp.class).isEnabled() ||
                        FreeLook.free
        );
        if (!silentRotation.getValue() && Client.instance.rotationUtils.rotating) {
            mc.thePlayer.rotationYaw = Client.instance.rotationUtils.rotation.getYaw();
            mc.thePlayer.rotationPitch = Client.instance.rotationUtils.rotation.getPitch();
            mc.thePlayer.rotationYawHead = Client.instance.rotationUtils.rotation.getYaw();
        }
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        setRotating();
    }

    @EventTarget
    public void onLoop(EventTick event) {
        if (this.isEnabled()) {
            setRotating();
        }
    }
}
