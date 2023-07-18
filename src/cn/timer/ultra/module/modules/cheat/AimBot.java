package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Numbers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.realms.RealmsMth;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class AimBot extends CombatModule {
    private final Numbers<Double> Reach;
    private final Booleans magnetism;
    private final Numbers<Double> yaw;
    private final Numbers<Double> pitch;
    private final Booleans customTarget;
    private final Booleans inventory;
    private final Booleans smooth;
    public EntityLivingBase target;

    public AimBot() {
        super("AimBot", Keyboard.KEY_NONE, Category.Cheat);
        this.Reach = new Numbers<>("Reach", 3.0, 6.0, 4.5);
        this.magnetism = new Booleans("Magnetism", true);
        this.yaw = new Numbers<>("Yaw", 1.0, 50.0, 15.0);
        this.pitch = new Numbers<>("Pitch", 1.0, 50.0, 15.0);
        this.customTarget = new Booleans("CustomTarget", false);
        this.inventory = new Booleans("inventory", true);
        this.smooth = new Booleans("Smooth", true);
        addValues(Reach, magnetism, yaw, pitch, customTarget, inventory, smooth);
    }

    @EventTarget
    public void onTick(final EventPreUpdate event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (this.magnetism.getValue()) {
            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                this.updateTarget();
                assistFaceEntity(this.target, this.yaw.getValue().floatValue(), this.pitch.getValue().floatValue());
                this.target = null;
            }
        } else {
            this.updateTarget();
            if (!(inventory.getValue() && mc.currentScreen != null))
                assistFaceEntity(customTarget.getValue() ? Client.instance.getModuleManager().getByClass(Target.class).getCurrentTarget() : this.target, this.yaw.getValue().floatValue(), this.pitch.getValue().floatValue());
            this.target = null;
        }
    }

    public void assistFaceEntity(final Entity entity, final float yaw, final float pitch) {
        if (entity == null) {
            return;
        }
        final double diffX = entity.posX - mc.thePlayer.posX;
        final double diffZ = entity.posZ - mc.thePlayer.posZ;
        double yDifference;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            yDifference = entityLivingBase.posY + entityLivingBase.getEyeHeight()
                    - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        } else {
            yDifference = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0
                    - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        }
        final double dist = RealmsMth.sqrt(diffX * diffX + diffZ * diffZ);
        final float rotationYaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float rotationPitch = (float) (-(Math.atan2(yDifference, dist) * 180.0 / 3.141592653589793));
        if (yaw > 0.0f) {
            mc.thePlayer.rotationYaw = getAnimationStateSmooth(updateRotation(mc.thePlayer.rotationYaw, rotationYaw, yaw / 4.0f), mc.thePlayer.rotationYaw, 0.8f);
        }
        if (pitch > 0.0f) {
            mc.thePlayer.rotationPitch = getAnimationStateSmooth(updateRotation(mc.thePlayer.rotationPitch, rotationPitch, pitch / 4.0f), mc.thePlayer.rotationPitch, 0.8f);
        }
    }

    private float getAnimationStateSmooth(float target, float current, float speed) {
        if (!smooth.getValue())
            return target;
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0F;
        } else if (speed > 1.0) {
            speed = 1.0F;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            if (current + factor > target) {
                current = target;
            } else {
                current += factor;
            }
        } else {
            if (current - factor < target) {
                current = target;
            } else {
                current -= factor;
            }
        }
        return current;
    }

    public static float updateRotation(final float p_70663_1_, final float p_70663_2_, final float p_70663_3_) {
        float var4 = RealmsMth.wrapDegrees(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

    void updateTarget() {
        try {
            for (final Entity object : getEntityList()) {
                if (object instanceof EntityLivingBase) {
                    final EntityLivingBase entity;
                    if (!this.check(entity = (EntityLivingBase) object)) {
                        continue;
                    }
                    this.target = entity;
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    public static List<Entity> getEntityList() {
        return mc.theWorld.getLoadedEntityList();
    }

    public boolean check(final EntityLivingBase entity) {
        return !(entity instanceof EntityArmorStand) && entity != mc.thePlayer && !entity.isDead
                && !AntiBot.isServerBot(entity)
                && entity.getDistanceToEntity(mc.thePlayer) <= this.Reach.getValue()
                && mc.thePlayer.canEntityBeSeen(entity);
    }

    @Override
    public void onDisable() {
        this.target = null;
    }
}
