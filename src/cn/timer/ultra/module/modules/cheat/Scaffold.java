package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.event.events.EventRender3D;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.utils.simple.BlockUtils;
import cn.timer.ultra.utils.simple.RotationUtil;
import cn.timer.ultra.values.Booleans;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Scaffold extends CombatModule {
    private final Booleans diagonal = new Booleans("Diagonal", true);
    private final Booleans silent = new Booleans("Slient", true);
    private final Booleans onlyGround = new Booleans("Only Ground", true);
    private final Booleans render = new Booleans("ESP", true);
    float curYaw;
    float curPitch;
    int sneakCount;
    int slot;
    EnumFacing enumFacing;
    boolean istower;
    public static ItemStack items;
    private final List<Block> blackList;

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_NONE, Category.Cheat);
        this.blackList = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.gravel, Blocks.ender_chest, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.redstone_torch);
        Booleans safeWalk = new Booleans("Safe Walk", true, (v1, v2) -> {
            Client.instance.getModuleManager().getByClass(SafeWalk.class).setEnabled(v2);
        });
        addValues(diagonal, silent, safeWalk, onlyGround, render);
    }

    public static Vec3i translate(BlockPos blockPos, EnumFacing enumFacing) {
        double x = blockPos.getX();
        double y = blockPos.getY();
        double z = blockPos.getZ();
        double r1 = ThreadLocalRandom.current().nextDouble(0.3, 0.5);
        double r2 = ThreadLocalRandom.current().nextDouble(0.9, 1.0);
        if (enumFacing.equals(EnumFacing.UP)) {
            x += r1;
            z += r1;
            ++y;
        } else if (enumFacing.equals(EnumFacing.DOWN)) {
            x += r1;
            z += r1;
        } else if (enumFacing.equals(EnumFacing.WEST)) {
            y += r2;
            z += r1;
        } else if (enumFacing.equals(EnumFacing.EAST)) {
            y += r2;
            z += r1;
            ++x;
        } else if (enumFacing.equals(EnumFacing.SOUTH)) {
            y += r2;
            x += r1;
            ++z;
        } else if (enumFacing.equals(EnumFacing.NORTH)) {
            y += r2;
            x += r1;
        }

        return new Vec3i(x, y, z);
    }

    float updateRotation(float curRot, float destination, float speed) {
        float f = MathHelper.wrapAngleTo180_float(destination - curRot);
        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return curRot + f;
    }

    public static float fade(final float target, final float current, final float speed) {
        if (target > current) {
            return Math.min(current + speed, target);
        } else {
            if (target >= current) {
                return target;
            }
            return Math.max(current - speed, target);
        }
    }

    float ly, lp;

    @EventTarget
    public void update(EventPreUpdate e) {
        if (mc.thePlayer == null || getBlockPosToPlaceOn(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)) == null || mc.gameSettings.keyBindJump.isPressed())
            return;
        if (!mc.thePlayer.onGround && onlyGround.getValue()) return;
        ly = mc.thePlayer.rotationYaw;
        lp = mc.thePlayer.rotationPitch;
        float[] r = RotationUtil.grabBlockRotations(Objects.requireNonNull(getBlockPosToPlaceOn(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ))));
        if (!SafeWalk.isCollidable(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.getPositionVector().add(new Vec3(0.0, -0.5, 0.0)))).getBlock())) {
            RotationUtil.yaw(r[0]);
            RotationUtil.pitch(r[1] + 13);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(r[0], r[1] + 13, mc.thePlayer.onGround));
            BlockUtils.placeBlockSimple(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ));
            RotationUtil.yaw(ly);
            RotationUtil.pitch(lp);
        }
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;
        this.sneakCount = 0;
        this.curYaw = mc.thePlayer.rotationYaw;
        this.curPitch = mc.thePlayer.rotationPitch;
        this.slot = mc.thePlayer.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;
        if (this.silent.getValue() && this.slot != mc.thePlayer.inventory.currentItem) {
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(this.slot = mc.thePlayer.inventory.currentItem));
        }
        if (mc.timer.timerSpeed != 1.0F) {
            mc.timer.timerSpeed = 1.0F;
        }
    }

    private BlockPos getBlockPosToPlaceOn(BlockPos pos) {
        BlockPos blockPos1 = pos.add(-1, 0, 0);
        BlockPos blockPos2 = pos.add(1, 0, 0);
        BlockPos blockPos3 = pos.add(0, 0, -1);
        BlockPos blockPos4 = pos.add(0, 0, 1);
        float down = 0.0F;
        if (mc.theWorld.getBlockState(pos.add(0.0, (double) (-1.0F - down), 0.0)).getBlock() != Blocks.air) {
            this.enumFacing = EnumFacing.UP;
            return pos.add(0, -1, 0);
        } else if (mc.theWorld.getBlockState(pos.add(-1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air) {
            this.enumFacing = EnumFacing.EAST;
            return pos.add(-1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(pos.add(1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air) {
            this.enumFacing = EnumFacing.WEST;
            return pos.add(1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(pos.add(0.0, (double) (0.0F - down), -1.0)).getBlock() != Blocks.air) {
            this.enumFacing = EnumFacing.SOUTH;
            return pos.add(0.0, (double) (0.0F - down), -1.0);
        } else if (mc.theWorld.getBlockState(pos.add(0.0, (double) (0.0F - down), 1.0)).getBlock() != Blocks.air) {
            this.enumFacing = EnumFacing.NORTH;
            return pos.add(0.0, (double) (0.0F - down), 1.0);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0.0, (double) (-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.UP;
            return blockPos1.add(0.0, (double) (-1.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos1.add(-1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.EAST;
            return blockPos1.add(-1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos1.add(1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.WEST;
            return blockPos1.add(1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0.0, (double) (0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.SOUTH;
            return blockPos1.add(0.0, (double) (0.0F - down), -1.0);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0.0, (double) (0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.NORTH;
            return blockPos1.add(0.0, (double) (0.0F - down), 1.0);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0.0, (double) (-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.UP;
            return blockPos2.add(0.0, (double) (-1.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos2.add(-1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.EAST;
            return blockPos2.add(-1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos2.add(1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.WEST;
            return blockPos2.add(1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0.0, (double) (0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.SOUTH;
            return blockPos2.add(0.0, (double) (0.0F - down), -1.0);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0.0, (double) (0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.NORTH;
            return blockPos2.add(0.0, (double) (0.0F - down), 1.0);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0.0, (double) (-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.UP;
            return blockPos3.add(0.0, (double) (-1.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos3.add(-1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.EAST;
            return blockPos3.add(-1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos3.add(1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.WEST;
            return blockPos3.add(1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0.0, (double) (0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.SOUTH;
            return blockPos3.add(0.0, (double) (0.0F - down), -1.0);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0.0, (double) (0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.NORTH;
            return blockPos3.add(0.0, (double) (0.0F - down), 1.0);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0.0, (double) (-1.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.UP;
            return blockPos4.add(0.0, (double) (-1.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos4.add(-1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.EAST;
            return blockPos4.add(-1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos4.add(1.0, (double) (0.0F - down), 0.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.WEST;
            return blockPos4.add(1.0, (double) (0.0F - down), 0.0);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0.0, (double) (0.0F - down), -1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.SOUTH;
            return blockPos4.add(0.0, (double) (0.0F - down), -1.0);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0.0, (double) (0.0F - down), 1.0)).getBlock() != Blocks.air && (Boolean) this.diagonal.getValue()) {
            this.enumFacing = EnumFacing.NORTH;
            return blockPos4.add(0.0, (double) (0.0F - down), 1.0);
        } else {
            return null;
        }
    }

    @EventTarget
    public void render(EventRender3D e) {
        if (this.render.getValue()) {
            this.esp(mc.thePlayer, e.getPartialTicks(), 0.5);
            this.esp(mc.thePlayer, e.getPartialTicks(), 0.4);
        }
    }

    public void esp(Entity entity, float partialTicks, double rad) {
        float points = 90.0F;
        GlStateManager.enableDepth();

        for (double il = 0.0; il < Double.MIN_VALUE; il += Double.MIN_VALUE) {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glEnable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glHint(3153, 4354);
            GL11.glDisable(2929);
            GL11.glLineWidth(3.5F);
            GL11.glBegin(3);
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;
            float speed = 5000.0F;
            float baseHue = (float) (System.currentTimeMillis() % (long) ((int) speed));
            baseHue /= speed;

            for (int i = 0; i <= 90; ++i) {
                float max = ((float) i + (float) (il * 8.0)) / points;

                for (float hue = max + baseHue; hue > 1.0F; --hue) ;

                int i2;
                for (i2 = 0; i2 <= 6; ++i2) {
                    if (this.istower) {
                        GlStateManager.color(255.0F, 255.0F, 255.0F, 1.0F);
                    } else {
                        GlStateManager.color(255.0F, 255.0F, 255.0F, 0.4F);
                    }

                    GL11.glVertex3d(x + rad * Math.cos((double) i2 * 6.283185307179586 / 6.0), y, z + rad * Math.sin((double) i2 * 6.283185307179586 / 6.0));
                }

                for (i2 = 0; i2 <= 6; ++i2) {
                    if (this.istower) {
                        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
                    } else {
                        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
                    }

                    GL11.glVertex3d(x + rad * Math.cos((double) i2 * 6.283185307179586 / 6.0) * 1.01, y, z + rad * Math.sin((double) i2 * 6.283185307179586 / 6.0) * 1.01);
                }
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GlStateManager.color(255.0F, 255.0F, 255.0F);
        }

    }

    private void fakeJump() {
        mc.thePlayer.isAirBorne = true;
        mc.thePlayer.triggerAchievement(StatList.jumpStat);
    }

    private int getBlockSlot() {
        int slot = -1;

        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && itemStack.stackSize >= 1) {
                ItemBlock block = (ItemBlock) itemStack.getItem();
                if (!this.blackList.contains(block.getBlock())) {
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }
}
