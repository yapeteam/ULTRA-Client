package cn.timer.ultra.utils.simple;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Random;

public class RotationUtil {
    static Minecraft mc;
    public static Rotation targetRotation;
    private static int keepLengt;
    private static final Random random;

    public static float pitch() {
        return RotationUtil.mc.thePlayer.rotationPitch;
    }

    public static void pitch(final float pitch) {
        RotationUtil.mc.thePlayer.rotationPitch = pitch;
    }

    public static float yaw() {
        return RotationUtil.mc.thePlayer.rotationYaw;
    }

    public static void yaw(final float yaw) {
        RotationUtil.mc.thePlayer.rotationYaw = yaw;
    }

    public static void reset() {
        RotationUtil.keepLengt = 0;
        RotationUtil.targetRotation = null;
    }

    public static float[] getFacingRotations2(final int paramInt1, final double d, final int paramInt3) {
        final EntitySnowball localEntityPig = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = paramInt3 + 0.5;
        return getRotationsNeeded(localEntityPig);
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final Minecraft mc = Minecraft.getMinecraft();
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2.0f - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180.0 / 3.141592653589793));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360.0f, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }

    public static float clampRotation() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float n = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        } else if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }

    public static float getSensitivityMultiplier() {
        final float SENSITIVITY = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6f + 0.2f;
        return SENSITIVITY * SENSITIVITY * SENSITIVITY * 8.0f * 0.15f;
    }

    public static void setTargetRotation(final Rotation rotation) {
        setTargetRotation(rotation, 0);
    }

    public static void setTargetRotation(final Rotation rotation, final int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90.0f || rotation.getPitch() < -90.0f) {
            return;
        }
        RotationUtil.targetRotation = rotation;
        RotationUtil.keepLengt = keepLength;
    }

    public static float smoothRotation(final float from, final float to, final float speed) {
        float f = MathHelper.wrapAngleTo180_float(to - from);
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return from + f;
    }

    public static float[] faceTarget(final Entity target, final float p_706252, final float p_706253, final boolean miss) {
        final double var4 = target.posX - RotationUtil.mc.thePlayer.posX;
        final double var5 = target.posZ - RotationUtil.mc.thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase) target;
            var7 = var6.posY + var6.getEyeHeight() - (RotationUtil.mc.thePlayer.posY + RotationUtil.mc.thePlayer.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (RotationUtil.mc.thePlayer.posY + RotationUtil.mc.thePlayer.getEyeHeight());
        }
        final Random rnd = new Random();
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float) (Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float) (-Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.25 : 0.0), var8) * 180.0 / 3.141592653589793);
        final float pitch = changeRotation(RotationUtil.mc.thePlayer.rotationPitch, var10, p_706253);
        final float yaw = changeRotation(RotationUtil.mc.thePlayer.rotationYaw, var9, p_706252);
        return new float[]{yaw, pitch};
    }

    public static float[] getPredictedRotations(final Entity ent) {
        final double x = ent.posX + (ent.posX - ent.lastTickPosX);
        final double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }

    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }

    public static float[] getNeededRotations(final Vec3 current, final Vec3 target) {
        final double diffX = target.xCoord - current.xCoord;
        final double diffY = target.yCoord - current.yCoord;
        final double diffZ = target.zCoord - current.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public static float changeRotation(final float p_706631, final float p_706632, final float p_706633) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < -p_706633) {
            var4 = -p_706633;
        }
        return p_706631 + var4;
    }

    public static double[] getRotationToEntity(final Entity entity) {
        final double pX = RotationUtil.mc.thePlayer.posX;
        final double pY = RotationUtil.mc.thePlayer.posY + RotationUtil.mc.thePlayer.getEyeHeight();
        final double pZ = RotationUtil.mc.thePlayer.posZ;
        final double eX = entity.posX;
        final double eY = entity.posY + entity.height / 2.0f;
        final double eZ = entity.posZ;
        final double dX = pX - eX;
        final double dY = pY - eY;
        final double dZ = pZ - eZ;
        final double dH = Math.sqrt(Math.pow(dX, 2.0) + Math.pow(dZ, 2.0));
        final double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        final double pitch = Math.toDegrees(Math.atan2(dH, dY));
        return new double[]{yaw, 90.0 - pitch};
    }

    public static float[] getRotations(final Entity entity) {
        if (entity == null) {
            return null;
        }
        final double diffX = entity.posX - RotationUtil.mc.thePlayer.posX;
        final double diffZ = entity.posZ - RotationUtil.mc.thePlayer.posZ;
        double diffY;
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase elb = (EntityLivingBase) entity;
            diffY = elb.posY + (elb.getEyeHeight() - 0.4) - (RotationUtil.mc.thePlayer.posY + RotationUtil.mc.thePlayer.getEyeHeight());
        } else {
            diffY = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - (RotationUtil.mc.thePlayer.posY + RotationUtil.mc.thePlayer.getEyeHeight());
        }
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public static float[] grabBlockRotations(final BlockPos pos) {
        return getVecRotation(RotationUtil.mc.thePlayer.getPositionVector().addVector(0.0, RotationUtil.mc.thePlayer.getEyeHeight(), 0.0), new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    public static float[] getVecRotation(final Vec3 origin, final Vec3 position) {
        final Vec3 difference = position.subtract(origin);
        final double distance = new Vec3(difference.xCoord, 0.0, difference.zCoord).lengthVector();
        final float yaw = (float) Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static int wrapAngleToDirection(final float yaw, final int zones) {
        int angle = (int) (yaw + 360 / (2 * zones) + 0.5) % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle / (360 / zones);
    }

    private static double x;

    private static double y;

    private static double z;

    static {
        RotationUtil.mc = Minecraft.getMinecraft();
        random = new Random();
        RotationUtil.x = RotationUtil.random.nextDouble();
        RotationUtil.y = RotationUtil.random.nextDouble();
        RotationUtil.z = RotationUtil.random.nextDouble();
    }
}
