package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.values.Mode;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends CombatModule {
    public SafeWalk() {
        super("SafeWalk", Keyboard.KEY_NONE, Category.Cheat);
        addValues(mode);
    }

    public boolean sneak;
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Sneak", "Motion"}, "Sneak");

    @EventTarget
    public void update(EventPreUpdate e) {
        final int keyCode = mc.gameSettings.keyBindSneak.getKeyCode();
        sneak = mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isPressed() && !isCollidable(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.getPositionVector().add(new Vec3(0.0, -0.5, 0.0)))).getBlock());
        if (mode.getValue().equals("Motion") && sneak)
            mc.thePlayer.setPosition(mc.thePlayer.prevPosX, mc.thePlayer.posY, mc.thePlayer.prevPosZ);
        if (mode.getValue().equals("Sneak")) KeyBinding.setKeyBindState(keyCode, sneak);
    }

    @Override
    public void onDisable() {
        final int keyCode = mc.gameSettings.keyBindSneak.getKeyCode();
        KeyBinding.setKeyBindState(keyCode, false);
    }

    public static boolean isCollidable(final Block block) {
        return block != Blocks.air
                && block != Blocks.carrots
                && block != Blocks.deadbush
                && block != Blocks.double_plant
                && block != Blocks.flowing_lava
                && block != Blocks.flowing_water
                && block != Blocks.lava
                && block != Blocks.melon_stem
                && block != Blocks.nether_wart
                && block != Blocks.potatoes
                && block != Blocks.pumpkin_stem
                && block != Blocks.red_flower
                && block != Blocks.red_mushroom
                && block != Blocks.redstone_torch
                && block != Blocks.tallgrass
                && block != Blocks.torch
                && block != Blocks.unlit_redstone_torch
                && block != Blocks.yellow_flower
                && block != Blocks.vine
                && block != Blocks.water
                && block != Blocks.web
                && block != Blocks.wheat;
    }
}
