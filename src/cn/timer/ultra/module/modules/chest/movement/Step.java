package cn.timer.ultra.module.modules.chest.movement;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.event.events.EventStep;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Step extends Module {
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Vanilla", "NCP"}, "Vanilla");
    public Step() {
        super("Step", Keyboard.KEY_Z, Category.Movement);
        this.addValues(this.mode);
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
        mc.thePlayer.stepHeight = 1.0f;
    }

    @EventTarget
    public void onStep(EventStep e) {
        if (mode.isCurrentMode("NCP")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42D, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.75D, mc.thePlayer.posZ, false));
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6F;
        super.onDisable();
    }
}
