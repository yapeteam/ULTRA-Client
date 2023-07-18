package cn.timer.ultra.alt.login;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontRenderer;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LoginScreen {
    private static int counter = 0;
    private static final TimerUtil timerUtil = new TimerUtil();
    private static boolean render = false;

    public static void setRender(boolean render) {
        LoginScreen.render = render;
    }

    public static void update() {
        if (!timerUtil.delay(500))
            return;
        counter -= 1;
        if (counter < 0)
            counter = 11;
        timerUtil.reset();
    }

    public static void render() {
        if (!render)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        UniFontRenderer jello18 = UniFontLoaders.jello18;
        String str = "Posting...";
        int loader_size = 14;
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        int w = 2 + loader_size + 2 + jello18.getStringWidth(str) + 2, h = loader_size + 4;
        int x = (width - w) / 2;
        int y = (height - h) / 2;
        RenderUtil.drawRect2(x, y, w, h, 0x00000061);
        RenderUtil.drawShadow(x, y, w, h);
        jello18.drawString(str, x + 2 + loader_size + 2, y + 2, -1);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(new ResourceLocation("client/loader.png"));
        Gui.drawModalRectWithCustomSizedTexture(x + 2, y + 2, counter * loader_size, 0, loader_size, loader_size, loader_size * 12, loader_size);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
}
