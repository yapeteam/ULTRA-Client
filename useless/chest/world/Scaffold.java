package cn.timer.ultra.module.modules.cheat.world;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold", Keyboard.KEY_G, Category.World);
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        if (mc.thePlayer.getHeldItem() == null) return;
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock && !(mc.thePlayer.movementInput.moveForward == 0.0F && mc.thePlayer.movementInput.moveStrafe == 0.0F)) {
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ), this.getFacing(), new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ));
        }
    }

    private EnumFacing getFacing() {
        if (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) {
            return EnumFacing.NORTH;
        } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) {
            return EnumFacing.SOUTH;
        } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) {
            return EnumFacing.WEST;
        } else if (mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST) {
            return EnumFacing.EAST;
        }
        return EnumFacing.NORTH;
    }
}
