package cn.timer.ultra.module.modules.world;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPacketReceive;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.lwjgl.input.Keyboard;

public class WorldTime extends Module {
    private final String[] AntiMode = {"Custom", "Change"};
    private final Mode<String> mode = new Mode<>("Mode", AntiMode, AntiMode[0]);
    private final Numbers<Double> time = new Numbers<>("Time", 0.0, 24000.0, 1000.0);
    int i;

    public WorldTime() {
        super("WorldTime", Keyboard.KEY_NONE, Category.World);
        addValues(mode, time);
    }

    @EventTarget
    public final void onReceivePacket(EventPacketReceive event) {
        if (mc.theWorld != null) {
            if (event.getPacket() instanceof S03PacketTimeUpdate) {
                event.setCancelled(true);
            }
            if (mode.isCurrentMode("Custom")) {
                mc.theWorld.setWorldTime((long) time.floatValue());
            }
            if (mode.isCurrentMode("Change")) {
                mc.theWorld.setWorldTime(i);
            }
        }
    }

    @EventTarget
    public final void onMotionUpdate(EventTick event) {
        if (mc.theWorld != null) {
            setSuffix(mode.getValue());
            if (mode.isCurrentMode("Custom")) {
                mc.theWorld.setWorldTime((long) (time).floatValue());
            }
            if (mode.isCurrentMode("Change")) {
                i += 100;
                if (i > 24000) {
                    i = 0;
                }
                mc.theWorld.setWorldTime(i);
            }
        }

    }
}
