package cn.timer.ultra.module.modules.movement;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPostUpdate;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Vanilla", "NCP"}, "Vanilla");
    public NoSlow() {
        super("NoSlow", Keyboard.KEY_B, Category.Movement);
        this.addValues(this.mode);
    }

    @EventTarget
    public void onPre(EventPreUpdate e) {
        if (mode.isCurrentMode("NCP")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
    }

    @EventTarget
    public void onPost(EventPostUpdate e) {
        if (mode.isCurrentMode("NCP")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
        }
    }
}
