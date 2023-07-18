package cn.timer.ultra.utils.mobends;

import cn.timer.ultra.utils.mobends.animation.Animation;
import cn.timer.ultra.utils.mobends.client.renderer.entity.RenderBendsPlayer;
import cn.timer.ultra.utils.mobends.client.renderer.entity.RenderBendsSpider;
import cn.timer.ultra.utils.mobends.client.renderer.entity.RenderBendsZombie;
import cn.timer.ultra.utils.mobends.util.BendsLogger;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnimatedEntity {
    public static List<AnimatedEntity> animatedEntities = new ArrayList<>();

    public static Map<String, RenderBendsPlayer> skinMap = Maps.newHashMap();
    public static RenderBendsPlayer playerRenderer;
    public static RenderBendsZombie zombieRenderer;
    public static RenderBendsSpider spiderRenderer;

    public String id;
    public String displayName;
    public Entity entity;

    public Class<? extends Entity> entityClass;
    public Render renderer;

    public List<Animation> animations = new ArrayList<>();

    public AnimatedEntity(String argID, String argDisplayName, Entity argEntity, Class<? extends Entity> argClass, Render argRenderer) {
        this.id = argID;
        this.displayName = argDisplayName;
        this.entityClass = argClass;
        this.renderer = argRenderer;
        this.entity = argEntity;
    }

    public AnimatedEntity add(Animation argGroup) {
        this.animations.add(argGroup);
        return this;
    }

    public static void register() {
        BendsLogger.log("Registering Animated Entities...", BendsLogger.INFO);

        animatedEntities.clear();

        registerEntity(new AnimatedEntity("player", "Player", Minecraft.getMinecraft().thePlayer, EntityPlayer.class, new RenderBendsPlayer(Minecraft.getMinecraft().getRenderManager())).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Stand()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Walk()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Sneak()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Sprint()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Jump()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Attack()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Swimming()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Bow()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Riding()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Mining()).
                add(new cn.timer.ultra.utils.mobends.animation.player.Animation_Axe()));
        registerEntity(new AnimatedEntity("zombie", "Zombie", new EntityZombie(null), EntityZombie.class, new RenderBendsZombie(Minecraft.getMinecraft().getRenderManager())).
                add(new cn.timer.ultra.utils.mobends.animation.zombie.Animation_Stand()).
                add(new cn.timer.ultra.utils.mobends.animation.zombie.Animation_Walk()));
        registerEntity(new AnimatedEntity("spider", "Spider", new EntitySpider(null), EntitySpider.class, new RenderBendsSpider(Minecraft.getMinecraft().getRenderManager())).
                add(new cn.timer.ultra.utils.mobends.animation.spider.Animation_OnGround()).
                add(new cn.timer.ultra.utils.mobends.animation.spider.Animation_Jump()).
                add(new cn.timer.ultra.utils.mobends.animation.spider.Animation_WallClimb()));

        //for (int i = 0;i < animatedEntities.size();i++) {
        //	if (animatedEntities.get(i).animate) RenderingRegistry.registerEntityRenderingHandler(animatedEntities.get(i).entityClass, animatedEntities.get(i).renderer);
        //}

        playerRenderer = new RenderBendsPlayer(Minecraft.getMinecraft().getRenderManager());
        zombieRenderer = new RenderBendsZombie(Minecraft.getMinecraft().getRenderManager());
        spiderRenderer = new RenderBendsSpider(Minecraft.getMinecraft().getRenderManager());
        skinMap.put("default", playerRenderer);
        skinMap.put("slim", new RenderBendsPlayer(Minecraft.getMinecraft().getRenderManager(), true));
    }

    public static void registerEntity(AnimatedEntity argEntity) {
        //BendsLogger.log("Registering " + argEntity.displayName, BendsLogger.INFO);
        animatedEntities.add(argEntity);
    }

    public Animation get(String argName) {
        for (Animation animation : animations) {
            if (animation.getName().equalsIgnoreCase(argName)) {
                return animation;
            }
        }
        return null;
    }

    public static AnimatedEntity getByEntity(Entity argEntity) {
        for (int i = 0; i < animatedEntities.size(); i++) {
            if (animatedEntities.get(i).entityClass.isInstance(argEntity)) {
                return animatedEntities.get(i);
            }
        }
        return null;
    }

    public static RenderBendsPlayer getPlayerRenderer(AbstractClientPlayer player) {
        String s = player.getSkinType();
        RenderBendsPlayer renderPlayer = skinMap.get(s);
        return renderPlayer != null ? renderPlayer : playerRenderer;
    }
}
