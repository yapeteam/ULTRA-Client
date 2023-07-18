package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender3D;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.notification.Notification;
import cn.timer.ultra.notification.NotificationManager;
import cn.timer.ultra.utils.mobends.AnimatedEntity;
import cn.timer.ultra.utils.mobends.client.renderer.entity.RenderBendsPlayer;
import cn.timer.ultra.utils.mobends.client.renderer.entity.RenderBendsSpider;
import cn.timer.ultra.utils.mobends.client.renderer.entity.RenderBendsZombie;
import cn.timer.ultra.utils.mobends.data.Data_Player;
import cn.timer.ultra.utils.mobends.data.Data_Spider;
import cn.timer.ultra.utils.mobends.data.Data_Zombie;
import cn.timer.ultra.values.Booleans;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;


public class MoBendsMod extends Module {
    private final Booleans zombieAnimation = new Booleans("Zombie Animation", true);
    private final Booleans spiderAnimation = new Booleans("Spider Animation", true);
    public final Booleans swordTrail = new Booleans("Sword Trail", true);
    public final Booleans spinAttack = new Booleans("Spin attack", true);

    public MoBendsMod() {
        super("Mo'Bends", Keyboard.KEY_NONE, Category.Render);
        addValues(zombieAnimation, spiderAnimation, swordTrail, spinAttack);
        AnimatedEntity.register();
    }

    @Override
    public void onEnable() {
        Module module = Client.instance.getModuleManager().getByClass(Loli.class);
        if (module.isEnabled()) {
            module.setConEnabled(false);
            NotificationManager.instance.add(new Notification("MoBends does not support Loli!", Notification.Type.Info));
        }
    }

    public static final ResourceLocation texture_NULL = new ResourceLocation("client/mobends/textures/white.png");

    @EventTarget
    public void onRender3D(EventRender3D e) {
        float partialTicks = e.getPartialTicks();
        if (mc.theWorld == null) {
            return;
        }

        for (int i = 0; i < Data_Player.dataList.size(); i++) {
            Data_Player.dataList.get(i).update(partialTicks);
        }

        for (int i = 0; i < Data_Zombie.dataList.size(); i++) {
            Data_Zombie.dataList.get(i).update(partialTicks);
        }

        for (int i = 0; i < Data_Spider.dataList.size(); i++) {
            Data_Spider.dataList.get(i).update(partialTicks);
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (mc.theWorld == null) {
            return;
        }

        for (int i = 0; i < Data_Player.dataList.size(); i++) {
            Data_Player data = Data_Player.dataList.get(i);
            Entity entity = mc.theWorld.getEntityByID(data.entityID);
            if (entity != null) {
                if (!data.entityType.equalsIgnoreCase(entity.getName())) {
                    Data_Player.dataList.remove(data);
                    Data_Player.add(new Data_Player(entity.getEntityId()));
                    //BendsLogger.log("Reset entity",BendsLogger.DEBUG);
                } else {

                    data.motion_prev.set(data.motion);

                    data.motion.x = (float) entity.posX - data.position.x;
                    data.motion.y = (float) entity.posY - data.position.y;
                    data.motion.z = (float) entity.posZ - data.position.z;

                    data.position = new Vector3f((float) entity.posX, (float) entity.posY, (float) entity.posZ);
                }
            } else {
                Data_Player.dataList.remove(data);
                //BendsLogger.log("No entity",BendsLogger.DEBUG);
            }
        }

        for (int i = 0; i < Data_Zombie.dataList.size(); i++) {
            Data_Zombie data = Data_Zombie.dataList.get(i);
            Entity entity = mc.theWorld.getEntityByID(data.entityID);
            if (entity != null) {
                if (!data.entityType.equalsIgnoreCase(entity.getName())) {
                    Data_Zombie.dataList.remove(data);
                    Data_Zombie.add(new Data_Zombie(entity.getEntityId()));
                    //BendsLogger.log("Reset entity",BendsLogger.DEBUG);
                } else {

                    data.motion_prev.set(data.motion);

                    data.motion.x = (float) entity.posX - data.position.x;
                    data.motion.y = (float) entity.posY - data.position.y;
                    data.motion.z = (float) entity.posZ - data.position.z;

                    data.position = new Vector3f((float) entity.posX, (float) entity.posY, (float) entity.posZ);
                }
            } else {
                Data_Zombie.dataList.remove(data);
                //BendsLogger.log("No entity",BendsLogger.DEBUG);
            }
        }

        for (int i = 0; i < Data_Spider.dataList.size(); i++) {
            Data_Spider data = Data_Spider.dataList.get(i);
            Entity entity = mc.theWorld.getEntityByID(data.entityID);
            if (entity != null) {
                if (!data.entityType.equalsIgnoreCase(entity.getName())) {
                    Data_Spider.dataList.remove(data);
                    Data_Spider.add(new Data_Spider(entity.getEntityId()));
                    //BendsLogger.log("Reset entity",BendsLogger.DEBUG);
                } else {

                    data.motion_prev.set(data.motion);

                    data.motion.x = (float) entity.posX - data.position.x;
                    data.motion.y = (float) entity.posY - data.position.y;
                    data.motion.z = (float) entity.posZ - data.position.z;

                    data.position = new Vector3f((float) entity.posX, (float) entity.posY, (float) entity.posZ);
                }
            } else {
                Data_Spider.dataList.remove(data);
                //BendsLogger.log("No entity",BendsLogger.DEBUG);
            }
        }
    }

    public boolean onRenderLivingEvent(RendererLivingEntity renderer, EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.isEnabled() || renderer instanceof RenderBendsPlayer || renderer instanceof RenderBendsZombie || renderer instanceof RenderBendsSpider) {
            return false;
        }

        AnimatedEntity animatedEntity = AnimatedEntity.getByEntity(entity);

        if (animatedEntity != null && (entity instanceof EntityPlayer || (entity instanceof EntityZombie && zombieAnimation.getValue()) || (entity instanceof EntitySpider && spiderAnimation.getValue()))) {
            if (entity instanceof EntityPlayer) {
                AbstractClientPlayer player = (AbstractClientPlayer) entity;
                AnimatedEntity.getPlayerRenderer(player).doRender(player, x, y, z, entityYaw, partialTicks);
            } else if (entity instanceof EntityZombie) {
                EntityZombie zombie = (EntityZombie) entity;
                AnimatedEntity.zombieRenderer.doRender(zombie, x, y, z, entityYaw, partialTicks);
            } else {
                EntitySpider spider = (EntitySpider) entity;
                AnimatedEntity.spiderRenderer.doRender(spider, x, y, z, entityYaw, partialTicks);
            }
            return true;
        }
        return false;
    }
}
