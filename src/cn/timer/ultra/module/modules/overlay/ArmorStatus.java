package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ArmorStatus extends HUDModule {
    public ArmorStatus() {
        super("ArmorStatus", Keyboard.KEY_NONE, Category.Overlay, 0, 0, 64, 64, "Right", "Bottom");
        registerOverlay((e) -> {
            int i = 0;
            for (ItemStack itemStack : mc.thePlayer.inventory.armorInventory) {
                renderItemStack(i, itemStack);
                i++;
            }
        });
    }

    private void renderItemStack(int i, ItemStack itemStack) {
        if (itemStack == null) return;
        GL11.glPushMatrix();
        int yAdd = -16 * i + 48;

        if (itemStack.getItem().isDamageable()) {
            double damage = ((itemStack.getMaxDamage() - itemStack.getItemDamage()) / (double) itemStack.getMaxDamage()) * 100;
            UniFontLoaders.arial14.drawString(String.format("%.2f/%.2f", damage, 100d), getXPosition() + 16, getYPosition() + yAdd + 4, -1);
        }
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) getXPosition(), (int) (getYPosition() + yAdd));
        GL11.glPopMatrix();
    }
}
