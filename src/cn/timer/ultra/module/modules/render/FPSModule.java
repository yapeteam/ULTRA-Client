package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.values.Booleans;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class FPSModule extends HUDModule {
    private final Booleans showBackground;

    public FPSModule() {
        super("FPS", Keyboard.KEY_NONE, Category.Render, 0, 0, 0, mc.fontRendererObj.FONT_HEIGHT, "Free", "Free");
        showBackground = new Booleans("Show Background", true);
        addValues(showBackground);
    }

    @Override
    public void drawHUD() {
        GL11.glPushMatrix();
        if (this.showBackground.getValue()) {
            Gui.drawRect((int) getXPosition() - 1, (int) getYPosition() - 1, (int) (getXPosition() + width + 1), (int) (getYPosition() + height + 1), 0x6F000000);
            String string = Minecraft.getDebugFPS() + " FPS";
            mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff);
            this.width = mc.fontRendererObj.getStringWidth(string);
        } else {
            String string = "[" + Minecraft.getDebugFPS() + " FPS]";
            mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff, true);
            this.width = mc.fontRendererObj.getStringWidth(string);
        }
        GL11.glPopMatrix();
    }
}
