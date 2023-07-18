package cn.timer.ultra.alt.devlogin;

import cn.timer.ultra.alt.MicrosoftOAuthLogin;
import cn.timer.ultra.gui.Font.CFontRenderer.CFontLoaders;
import cn.timer.ultra.gui.Font.CFontRenderer.CFontRenderer;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class LoginScreen {
    public static void render() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution s = new ScaledResolution(mc);
        int loader_size = 14;
        CFontRenderer jello21 = CFontLoaders.jello21;
        CFontRenderer jello18 = CFontLoaders.jello18;
        int width = s.getScaledWidth();
        int height = s.getScaledHeight();
        int w = 100, h = 130;
        int x = (width - w) / 2;
        int y = (height - h) / 2;
        RenderUtil.drawRect2(x, y, w, h, new Color(0, 0, 0, 97).getRGB());
        RenderUtil.drawRect2(x + 2, y + jello21.getHeight() + 4, w - 4, 0.5f, -1);

        RenderUtil.drawShadow(x, y, w, h);
        jello21.drawString("Login your account", x + 2, y + 2, -1);
        jello18.drawString("Paste the code and", x + 2, y + jello21.getHeight() + 6, -1);
        jello18.drawString("login in the browser", x + 2, y + jello21.getHeight() + jello21.getHeight() + 6, -1);
        jello21.drawString("Listening...", x + 17, y + h / 2f + 3, -1);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation("client/loader.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + 2, y + h / 2f, counter * loader_size, 0, loader_size, loader_size, loader_size * 12, loader_size);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            DevLogin.StopLoginThread();
            MicrosoftOAuthLogin.browser.close();
        }
    }

    private static final TimerUtil timerUtil = new TimerUtil();
    private static int counter = 0;

    public static void update() {
        if (!timerUtil.delay(500)) return;
        counter -= 1;
        if (counter < 0) {
            counter = 11;
        }
        timerUtil.reset();
    }
}
