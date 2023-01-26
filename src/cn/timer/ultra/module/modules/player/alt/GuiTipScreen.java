package cn.timer.ultra.module.modules.player.alt;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.gui.Font.FontRenderer;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiTipScreen extends GuiScreen {
    private final String text1;
    private final String text2;
    public static boolean su = false;

    public GuiTipScreen(String test1, String text2) {
        this.text1 = test1;
        this.text2 = text2;
        timerUtil = new TimerUtil();
    }

    private int counter = 11;

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        ScaledResolution s = new ScaledResolution(mc);
        FontRenderer font = FontLoaders.msFont18;
        FontRenderer font2 = FontLoaders.arial14;
        int width = s.getScaledWidth();
        int height = s.getScaledHeight();
        int tw;
        int th;
        int loader_size = 10;
        tw = Math.max(font.getStringWidth(text1), font.getStringWidth(text2)) + 10;
        th = font.FONT_HEIGHT * 3 + 10;
        if (mc.theWorld != null) {
            RenderUtil.drawShadow((width - tw) / 2f, (height - th) / 2f, tw, th);
        }
        RenderUtil.drawRoundedRect2((width - tw) / 2f, (height - th) / 2f, tw, th, 5, -1);
        font.drawString(text1, (width - font.getStringWidth(text1)) / 2f, (height - font.FONT_HEIGHT) / 2f - font.FONT_HEIGHT, new Color(0, 0, 0).getRGB());
        font.drawString(text2, (width - font.getStringWidth(text2)) / 2f, (height - font.FONT_HEIGHT) / 2f, new Color(0, 0, 0).getRGB());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation("client/loader.png"));
        Gui.drawModalRectWithCustomSizedTexture((width - tw) / 2f + 10, (height - font.FONT_HEIGHT) / 2f + font.FONT_HEIGHT, counter * loader_size, 0, loader_size, loader_size, loader_size * 12, loader_size);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        font2.drawString("请稍等", (width - tw) / 2f + 10 + loader_size, (height - font.FONT_HEIGHT) / 2f + font.FONT_HEIGHT, 0);
    }

    TimerUtil timerUtil;

    @EventTarget
    private void onTick(EventTick e) {
        if (!timerUtil.delay(500)) return;
        this.counter -= 1;
        if (this.counter < 0) {
            this.counter = 11;
        }
        timerUtil.reset();
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

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
