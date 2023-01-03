package cn.timer.ultra.module.modules.render.alt;

import cn.timer.ultra.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;

public class GuiTipScreen extends GuiScreen {
    private final String text1;
    private final String text2;
    public static boolean su = false;

    public GuiTipScreen(String test1, String text2) {
        this.text1 = test1;
        this.text2 = text2;
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        ScaledResolution s = new ScaledResolution(mc);
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
        int width = s.getScaledWidth();
        int height = s.getScaledHeight();
        font.drawString(text1, (width - font.getStringWidth(text1)) / 2f, (height - font.FONT_HEIGHT) / 2f, new Color(255, 255, 255).getRGB(), true);
        font.drawString(text2, (width - font.getStringWidth(text2)) / 2f, (height - font.FONT_HEIGHT) / 2f + font.FONT_HEIGHT, new Color(255, 255, 255).getRGB(), true);
    }

    public static void close() {
        su = true;
        Minecraft.getMinecraft().displayGuiScreen(null);
    }

    @Override
    public void onGuiClosed() {
        if (su) return;
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "[" + Client.CLIENT_NAME + "] " + "Login Cancelled"));
    }
}
