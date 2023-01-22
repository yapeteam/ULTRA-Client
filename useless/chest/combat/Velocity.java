package cn.timer.ultra.module.modules.cheat.combat;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPacketReceive;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Keyboard.KEY_V, Category.Combat);
    }

    @EventTarget
    public void onPacket(EventPacketReceive e) {
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            e.setCancelled(true);
        }
    }
}
