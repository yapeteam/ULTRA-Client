/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package cn.timer.ultra.utils.fdp;

import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPacket;
import cn.timer.ultra.event.events.EventTick;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.util.Random;

public final class RotationUtils extends MinecraftInstance {
    public RotationUtils() {
        EventManager.instance.register(this);
    }

    private final Random random = new Random();

    private int keepLength;
    private int revTick;

    public Rotation targetRotation;
    public Rotation serverRotation = new Rotation(0F, 0F);

    public boolean keepCurrentRotation = false;

    private double x = random.nextDouble();
    private double y = random.nextDouble();
    private double z = random.nextDouble();

    /**
     * Calculate difference between two angle points
     *
     * @param a angle point
     * @param b angle point
     * @return difference between angle points
     */
    public float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    public void setTargetRotationReverse(final Rotation rotation, final int keepLength, final int revTick) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        rotation.fixedSensitivity(mc.gameSettings.mouseSensitivity);
        targetRotation = rotation;
        this.keepLength = keepLength;
        this.revTick = revTick + 1;
    }

    /**
     * Handle minecraft tick
     *
     * @param event Tick event
     */
    @EventTarget
    public void onTick(final EventTick event) {
        if (mc.thePlayer == null) return;
        if (targetRotation != null) {
            keepLength--;

            if (keepLength <= 0) {
                if (revTick > 0) {
                    revTick--;
                    reset();
                } else reset();
            }
        }

        if (random.nextGaussian() > 0.8D) x = Math.random();
        if (random.nextGaussian() > 0.8D) y = Math.random();
        if (random.nextGaussian() > 0.8D) z = Math.random();
    }

    /**
     * Handle packet
     *
     * @param event Packet Event
     */
    @EventTarget
    public void onPacket(final EventPacket event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;

            if (targetRotation != null && !keepCurrentRotation && (targetRotation.getYaw() != serverRotation.getYaw() || targetRotation.getPitch() != serverRotation.getPitch())) {
                packetPlayer.setYaw(targetRotation.getYaw());
                packetPlayer.setPitch(targetRotation.getPitch());
                packetPlayer.setRotating(true);
            }

            if (packetPlayer.rotating) serverRotation = new Rotation(packetPlayer.getYaw(), packetPlayer.getPitch());
        }
    }

    /**
     * Reset your target rotation
     */
    public void reset() {
        keepLength = 0;
        if (revTick > 0) {
            targetRotation = new Rotation(targetRotation.getYaw() - getAngleDifference(targetRotation.getYaw(), mc.thePlayer.rotationYaw) / revTick, targetRotation.getPitch() - getAngleDifference(targetRotation.getPitch(), mc.thePlayer.rotationPitch) / revTick);
        } else targetRotation = null;
    }

    public Rotation getRotations(Entity ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double) (ent.getEyeHeight() / 2.0f);
        return RotationUtils.getRotationFromPosition(x, z, y);
    }

    public Rotation getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = RotationUtils.mc.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double) player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-(Math.atan2(y, dist) * 180.0 / 3.141592653589793));
        return new Rotation(yaw, pitch);
    }

    public static Rotation getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
        return new Rotation(yaw, pitch);
    }

    /**
     * skid from https://github.com/danielkrupinski/Osiris
     */
    public Rotation rotationSmooth(Rotation currentRotation, Rotation targetRotation, float smooth) {
        return new Rotation(currentRotation.getYaw() + ((targetRotation.getYaw() - currentRotation.getYaw()) / smooth), currentRotation.getPitch() + ((targetRotation.getPitch() - currentRotation.getPitch()) / smooth));
    }

    public Rotation getRotationFromEyeHasPrev(EntityLivingBase target) {
        final double x = (target.prevPosX + (target.posX - target.prevPosX));
        final double y = (target.prevPosY + (target.posY - target.prevPosY));
        final double z = (target.prevPosZ + (target.posZ - target.prevPosZ));
        return getRotationFromEyeHasPrev(x, y, z);
    }

    public Rotation getRotationFromEyeHasPrev(double x, double y, double z) {
        double xDiff = x - (mc.thePlayer.prevPosX + (mc.thePlayer.posX - mc.thePlayer.prevPosX));
        double yDiff = y - ((mc.thePlayer.prevPosY + (mc.thePlayer.posY - mc.thePlayer.prevPosY)) + (mc.thePlayer.getEntityBoundingBox().maxY - mc.thePlayer.getEntityBoundingBox().minY));
        double zDiff = z - (mc.thePlayer.prevPosZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ));
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        return new Rotation((float) (Math.atan2(zDiff, xDiff) * 180D / Math.PI) - 90F, (float) -(Math.atan2(yDiff, dist) * 180D / Math.PI));
    }


    /**
     * Translate vec to rotation
     *
     * @param vec     target vec
     * @param predict predict new location of your body
     * @return rotation
     */
    public static Rotation toRotation(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if (predict) {
            if (mc.thePlayer.onGround) {
                eyesPos.addVector(mc.thePlayer.motionX, 0.0, mc.thePlayer.motionZ);
            } else eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
        }

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new Rotation(MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F), MathHelper.wrapAngleTo180_float((float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))));
    }

    /**
     * Get the center of a box
     *
     * @param bb your box
     * @return center of box
     */
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    /**
     * Calculate difference between the client rotation and your entity
     *
     * @param entity your entity
     * @return difference between rotation
     */
    public double getRotationDifference(final Entity entity) {
        final Rotation rotation = toRotation(getCenter(entity.getEntityBoundingBox()), true);

        return getRotationDifference(rotation, new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch));
    }

    /**
     * Calculate difference between the server rotation and your rotation
     *
     * @param rotation your rotation
     * @return difference between rotation
     */
    public double getRotationDifference(final Rotation rotation) {
        return serverRotation == null ? 0D : getRotationDifference(rotation, serverRotation);
    }

    /**
     * Calculate difference between two rotations
     *
     * @param a rotation
     * @param b rotation
     * @return difference between rotation
     */
    public double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public void setTargetRotation(final Rotation rotation) {
        setTargetRotation(rotation, 0);
    }

    /**
     * Set your target rotation
     *
     * @param rotation your target rotation
     */
    public void setTargetRotation(final Rotation rotation, final int keepLength) {
        if (Double.isNaN(rotation.getYaw()) || Double.isNaN(rotation.getPitch()) || rotation.getPitch() > 90 || rotation.getPitch() < -90)
            return;

        rotation.fixedSensitivity(mc.gameSettings.mouseSensitivity);
        targetRotation = rotation;
        this.keepLength = keepLength;
        this.revTick = 0;
    }

    /**
     * Limit your rotation using a turn speed
     *
     * @param currentRotation your current rotation
     * @param targetRotation  your goal rotation
     * @param turnSpeed       your turn speed
     * @return limited rotation
     */
    public Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    /**
     * Allows you to check if your crosshair is over your target entity
     *
     * @param targetEntity       your target entity
     * @param blockReachDistance your reach
     * @return if crosshair is over target
     */
    public boolean isFaced(final Entity targetEntity, double blockReachDistance) {
        return RaycastUtils.raycastEntity(blockReachDistance, entity -> entity == targetEntity) != null;
    }

    public VecRotation calculateCenter(final String calMode, final String randMode, final double randomRange, final AxisAlignedBB bb, final boolean predict, final boolean throughWalls) {
        VecRotation vecRotation = null;

        double xMin = 0.0D;
        double yMin = 0.0D;
        double zMin = 0.0D;
        double xMax = 0.0D;
        double yMax = 0.0D;
        double zMax = 0.0D;
        double xDist = 0.0D;
        double yDist = 0.0D;
        double zDist = 0.0D;

        xMin = 0.15D;
        xMax = 0.85D;
        xDist = 0.1D;
        yMin = 0.15D;
        yMax = 1.00D;
        yDist = 0.1D;
        zMin = 0.15D;
        zMax = 0.85D;
        zDist = 0.1D;

        Vec3 curVec3 = null;

        switch (calMode) {
            case "LiquidBounce":
                xMin = 0.15D;
                xMax = 0.85D;
                xDist = 0.1D;
                yMin = 0.15D;
                yMax = 1.00D;
                yDist = 0.1D;
                zMin = 0.15D;
                zMax = 0.85D;
                zDist = 0.1D;
                break;
            case "Full":
                xMin = 0.00D;
                xMax = 1.00D;
                xDist = 0.1D;
                yMin = 0.00D;
                yMax = 1.00D;
                yDist = 0.1D;
                zMin = 0.00D;
                zMax = 1.00D;
                zDist = 0.1D;
                break;
            case "HalfUp":
                xMin = 0.10D;
                xMax = 0.90D;
                xDist = 0.1D;
                yMin = 0.50D;
                yMax = 0.90D;
                yDist = 0.1D;
                zMin = 0.10D;
                zMax = 0.90D;
                zDist = 0.1D;
                break;
            case "HalfDown":
                xMin = 0.10D;
                xMax = 0.90D;
                xDist = 0.1D;
                yMin = 0.10D;
                yMax = 0.50D;
                yDist = 0.1D;
                zMin = 0.10D;
                zMax = 0.90D;
                zDist = 0.1D;
                break;
            case "CenterSimple":
                xMin = 0.45D;
                xMax = 0.55D;
                xDist = 0.0125D;
                yMin = 0.65D;
                yMax = 0.75D;
                yDist = 0.0125D;
                zMin = 0.45D;
                zMax = 0.55D;
                zDist = 0.0125D;
                break;
            case "CenterLine":
                xMin = 0.45D;
                xMax = 0.55D;
                xDist = 0.0125D;
                yMin = 0.10D;
                yMax = 0.90D;
                yDist = 0.1D;
                zMin = 0.45D;
                zMax = 0.55D;
                zDist = 0.0125D;
                break;
        }

        for (double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final Rotation rotation = toRotation(vec3, predict);

                    if (throughWalls || isVisible(vec3)) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);

                        if (vecRotation == null || (getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))) {
                            vecRotation = currentVec;
                            curVec3 = vec3;
                        }
                    }
                }
            }
        }

        if (vecRotation == null || randMode == "Off") return vecRotation;

        double rand1 = random.nextDouble();
        double rand2 = random.nextDouble();
        double rand3 = random.nextDouble();

        final double xRange = bb.maxX - bb.minX;
        final double yRange = bb.maxY - bb.minY;
        final double zRange = bb.maxZ - bb.minZ;
        double minRange = 999999.0D;

        if (xRange <= minRange) minRange = xRange;
        if (yRange <= minRange) minRange = yRange;
        if (zRange <= minRange) minRange = zRange;

        rand1 = rand1 * minRange * randomRange;
        rand2 = rand2 * minRange * randomRange;
        rand3 = rand3 * minRange * randomRange;

        final double xPrecent = minRange * randomRange / xRange;
        final double yPrecent = minRange * randomRange / yRange;
        final double zPrecent = minRange * randomRange / zRange;

        Vec3 randomVec3 = new Vec3(curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1, curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2, curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3);
        switch (randMode) {
            case "Horizonal":
                randomVec3 = new Vec3(curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1, curVec3.yCoord, curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3);
                break;
            case "Vertical":
                randomVec3 = new Vec3(curVec3.xCoord, curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2, curVec3.zCoord);
                break;
        }

        final Rotation randomRotation = toRotation(randomVec3, predict);

        /*
        for(double xSearch = 0.00D; xSearch < 1.00D; xSearch += 0.05D) {
            for (double ySearch = 0.00D; ySearch < 1.00D; ySearch += 0.05D) {
                for (double zSearch = 0.00D; zSearch < 1.00D; zSearch += 0.05D) {
                    final Vec3 vec3 = new Vec3(curVec3.xCoord - ((randMode == "Horizonal") ? 0.0D : (xPrecent * (curVec3.xCoord - bb.minX) + minRange * randomRange * xSearch)),
                                               curVec3.yCoord - ((randMode == "Vertical") ? 0.0D : (yPrecent * (curVec3.yCoord - bb.minY) + minRange * randomRange * ySearch)),
                                               curVec3.zCoord - ((randMode == "Horizonal") ? 0.0D : (zPrecent * (curVec3.zCoord - bb.minZ) + minRange * randomRange * zSearch)));
                    final Rotation rotation = toRotation(vec3, predict);
                    if(throughWalls || isVisible(vec3)) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);
                        if (vecRotation == null || (getRotationDifference(currentVec.getRotation(), randomRotation) < getRotationDifference(vecRotation.getRotation(), randomRotation)))
                            vecRotation = currentVec;
                    }
                }
            }
        }
        I Give Up :sadface: */
        vecRotation = new VecRotation(randomVec3, randomRotation);

        return vecRotation;
    }

    /**
     * Allows you to check if your enemy is behind a wall
     */
    public static boolean isVisible(final Vec3 vec3) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        return mc.theWorld.rayTraceBlocks(eyesPos, vec3) == null;
    }
}
