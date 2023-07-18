package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPacketSend;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class FreeCam extends CombatModule {
    private EntityOtherPlayerMP entity;

    public FreeCam() {
        super("FreeCam", Keyboard.KEY_Y, Category.Cheat);
    }

    @Override
    public void onEnable() {
        this.entity = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(250, 520), mc.thePlayer.getName()));
        entity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        mc.theWorld.addEntityToWorld(110, entity);
        super.onEnable();
    }

    @EventTarget
    public void onPacket(EventPacketSend e) {
        e.setCancelled(true);
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null || mc.theWorld == null || entity == null) return;
        mc.thePlayer.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        mc.theWorld.removeEntity(entity);
        super.onDisable();
    }
}
