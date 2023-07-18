// 
// Decompiled by Procyon v0.5.30
// 

package cn.timer.ultra.utils.simple;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class BlockUtils {
    public static final Minecraft mc;
    static ItemInWorldManager iIM;

    static {
        mc = Minecraft.getMinecraft();
    }

    public static IBlockState getState(final BlockPos pos) {
        return BlockUtils.mc.theWorld.getBlockState(pos);
    }

    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static Material getMaterial(final BlockPos pos) {
        return getState(pos).getBlock().getMaterial();
    }

    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    public static boolean placeBlockLegit(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                        BlockUtils.mc.playerController.onPlayerRightClick(BlockUtils.mc.thePlayer, BlockUtils.mc.theWorld, BlockUtils.mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                        BlockUtils.mc.thePlayer.swingItem();
                        BlockUtils.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean rcActionLegit(final BlockPos pos) {
        BlockUtils.iIM = new ItemInWorldManager(BlockUtils.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                        NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(pos, side2.getIndex(), BlockUtils.mc.thePlayer.getCurrentEquippedItem(), (float) hitVec.xCoord, (float) hitVec.yCoord, (float) hitVec.zCoord));
                        BlockUtils.mc.thePlayer.swingItem();
                        BlockUtils.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static double getDistanceToBlock(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        final EnumFacing[] values;
        if ((values = EnumFacing.values()).length != 0) {
            final EnumFacing side = values[0];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
            return eyesPos.distanceTo(hitVec);
        }
        return 0.0;
    }

    public static boolean rcAction(final BlockPos pos, final double reach) {
        BlockUtils.iIM = new ItemInWorldManager(BlockUtils.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= reach * reach) {
                        faceVectorPacket(hitVec);
                        NetUtils.sendPacket(new C08PacketPlayerBlockPlacement(pos, side2.getIndex(), BlockUtils.mc.thePlayer.getCurrentEquippedItem(), (float) hitVec.xCoord, (float) hitVec.yCoord, (float) hitVec.zCoord));
                        BlockUtils.mc.thePlayer.swingItem();
                        BlockUtils.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean breakBlockTap(final BlockPos pos, final double reach, final boolean start) {
        BlockUtils.iIM = new ItemInWorldManager(BlockUtils.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= reach * reach) {
                        faceVectorPacket(hitVec);
                        BlockUtils.mc.playerController.clickBlock(pos, side2);
                        BlockUtils.mc.thePlayer.swingItem();
                        BlockUtils.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean breakBlockTapLegit(final BlockPos pos) {
        BlockUtils.iIM = new ItemInWorldManager(BlockUtils.mc.theWorld);
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        faceVectorPacket(hitVec);
                        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                        BlockUtils.mc.thePlayer.swingItem();
                        BlockUtils.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean placeBlockLegit2(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) < eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5))) {
                if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                    final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                    if (eyesPos.squareDistanceTo(hitVec) <= 18.0625) {
                        BlockUtils.mc.playerController.onPlayerRightClick(BlockUtils.mc.thePlayer, BlockUtils.mc.theWorld, BlockUtils.mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                        BlockUtils.mc.thePlayer.swingItem();
                        BlockUtils.mc.rightClickDelayTimer = 4;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean placeBlockSimple(final BlockPos pos) {
        final Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
            final EnumFacing side = values[i];
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (getBlock(neighbor).canCollideCheck(BlockUtils.mc.theWorld.getBlockState(neighbor), false)) {
                final Vec3 hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()).scale(0.5));
                if (eyesPos.squareDistanceTo(hitVec) <= 36.0) {
                    BlockUtils.mc.playerController.onPlayerRightClick(BlockUtils.mc.thePlayer, BlockUtils.mc.theWorld, BlockUtils.mc.thePlayer.getCurrentEquippedItem(), neighbor, side2, hitVec);
                    return true;
                }
            }
        }
        return false;
    }

    private static void faceVectorPacket(final Vec3 vec) {
        final double diffX = vec.xCoord - BlockUtils.mc.thePlayer.posX;
        final double diffY = vec.yCoord - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - BlockUtils.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));
        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch), BlockUtils.mc.thePlayer.onGround));
    }

    private static void faceVectorPacket2(final Vec3 vec) {
        final double diffX = vec.xCoord - BlockUtils.mc.thePlayer.posX;
        final double diffY = vec.yCoord - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord - BlockUtils.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));
        Minecraft.getMinecraft().thePlayer.rotationYaw = (BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw));
        Minecraft.getMinecraft().thePlayer.rotationPitch = (BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch));
    }

    public static void faceBlockClient(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.5 - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        BlockUtils.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw);
        BlockUtils.mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch);
    }

    public static void faceBlockPacket(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        final double diffY = blockPos.getY() + 0.5 - (BlockUtils.mc.thePlayer.posY + BlockUtils.mc.thePlayer.getEyeHeight());
        final double diffZ = blockPos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch), BlockUtils.mc.thePlayer.onGround));
    }

    public static void faceBlockClientHorizontally(final BlockPos blockPos) {
        final double diffX = blockPos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        final double diffZ = blockPos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        BlockUtils.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw);
    }

    public static float getPlayerBlockDistance(final BlockPos blockPos) {
        return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static float getPlayerBlockDistance(final double posX, final double posY, final double posZ) {
        final float xDiff = (float) (BlockUtils.mc.thePlayer.posX - posX);
        final float yDiff = (float) (BlockUtils.mc.thePlayer.posY - posY);
        final float zDiff = (float) (BlockUtils.mc.thePlayer.posZ - posZ);
        return getBlockDistance(xDiff, yDiff, zDiff);
    }

    public static float getBlockDistance(final float xDiff, final float yDiff, final float zDiff) {
        return MathHelper.sqrt_float((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }

    public static float getHorizontalPlayerBlockDistance(final BlockPos blockPos) {
        final float xDiff = (float) (BlockUtils.mc.thePlayer.posX - blockPos.getX());
        final float zDiff = (float) (BlockUtils.mc.thePlayer.posZ - blockPos.getZ());
        return MathHelper.sqrt_float((xDiff - 0.5f) * (xDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }
}
