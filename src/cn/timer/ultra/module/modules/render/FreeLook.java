package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventLoop;
import cn.timer.ultra.event.events.EventPacketReceive;
import cn.timer.ultra.event.events.EventPacketSend;
import cn.timer.ultra.event.events.EventPostUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.AnimationUtils;
import cn.timer.ultra.utils.Rotation;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class FreeLook extends Module {

    public FreeLook() {
        super("FreeLook", Keyboard.KEY_NONE, Category.Render);
    }

    AnimationUtils animationUtils = new AnimationUtils();
    AnimationUtils animationUtils1 = new AnimationUtils();

    @EventTarget
    private void onPostUpdate(EventPostUpdate e) {
        if (free) {
            Client.instance.rotationUtils.setRotation(new Rotation(this.yaw, this.pitch));
        } else if (stop) {
            mc.thePlayer.rotationYaw = animationUtils.animate(this.yaw, mc.thePlayer.rotationYaw, 1);
            mc.thePlayer.rotationPitch = animationUtils1.animate(this.pitch, mc.thePlayer.rotationPitch, 1);
            if (mc.thePlayer.rotationYaw == this.yaw && mc.thePlayer.rotationPitch == this.pitch) {
                stop = false;
            }
        }
    }

    @EventTarget
    private void onPacketSend(EventPacketSend e) {
        if (e.getPacket() instanceof C03PacketPlayer && free) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
            packet.setYaw(0);
            packet.setPitch(0);
            e.setPacket(packet);
        }
    }

    @EventTarget
    private void onPacketReceive(EventPacketReceive e) {
        if (e.getPacket() instanceof C03PacketPlayer && free) {
            C03PacketPlayer packet = (C03PacketPlayer) e.getPacket();
            packet.setYaw(0);
            packet.setPitch(0);
            e.setPacket(packet);
        }
    }

    static boolean free = false;
    boolean stop = true;
    float yaw, pitch;

    @EventTarget
    private void update(EventLoop e) {
        if (Keyboard.isKeyDown(Keyboard.KEY_H) && !free) {
            this.yaw = mc.thePlayer.rotationYaw;
            this.pitch = mc.thePlayer.rotationPitch;
            free = true;
            stop = true;
        } else if (!Keyboard.isKeyDown(Keyboard.KEY_H)) {
            free = false;
        }
    }
}
