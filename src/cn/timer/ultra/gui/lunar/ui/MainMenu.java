package cn.timer.ultra.gui.lunar.ui;

import cn.timer.ultra.Client;
import cn.timer.ultra.alt.MicrosoftOAuthLogin;
import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.lunar.ui.buttons.ImageButton;
import cn.timer.ultra.gui.lunar.ui.buttons.LoginButton;
import cn.timer.ultra.gui.lunar.ui.buttons.MainButton;
import cn.timer.ultra.gui.lunar.ui.buttons.QuitButton;
import cn.timer.ultra.gui.particles.ParticleEngine;
import cn.timer.ultra.protection.ProtectionLoader;
import cn.timer.ultra.utils.jello.GLUtils;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.color.ColorUtil;
import cn.timer.ultra.utils.ultra.render.GradientUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.ShaderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class MainMenu extends GuiScreen {
    private MainButton btnSingleplayer;
    private MainButton btnMultiplayer;

    private ImageButton btnLunarOptions;
    private ImageButton btnCosmetics;
    private ImageButton btnMinecraftOptions;
    private ImageButton btnLanguage;
    private LoginButton btnOAuth;
    private QuitButton btnQuit;
    long start;

    @Override
    public void initGui() {
        pe.particles.clear();
        aniX = GLUtils.getMouseX();
        aniY = GLUtils.getMouseY();

        this.btnSingleplayer = new MainButton("S I N G L E P L A Y E R", this.width / 2f - 66, this.height / 2f, () -> {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        });
        this.btnMultiplayer = new MainButton("M U L T I P L A Y E R", this.width / 2f - 66, this.height / 2f + 15, () -> {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        });

        int yPos = this.height - 20;
        this.btnLunarOptions = new ImageButton("ULTRA SETTINGS", new ResourceLocation("lunar/icons/lunar.png"), this.width / 2f - 30, yPos, () -> {

        });
        this.btnCosmetics = new ImageButton("ULTRA COSMETICS", new ResourceLocation("lunar/icons/cosmetics.png"), this.width / 2f - 15, yPos, () -> {

        });
        this.btnMinecraftOptions = new ImageButton("MINECRAFT SETTINGS", new ResourceLocation("lunar/icons/cog.png"), this.width / 2f, yPos, () -> {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        });
        this.btnLanguage = new ImageButton("LANGUAGE", new ResourceLocation("lunar/icons/globe.png"), this.width / 2f + 15, yPos, () -> {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        });
        this.btnOAuth = new LoginButton("", 5, 5,
                MicrosoftOAuthLogin::login
        );

        this.btnQuit = new QuitButton(this.width - 17, 7);
        start = System.currentTimeMillis();
    }

    public final Color getClientColor() {
        return Client.instance.getClientColor();
    }

    public final Color getAlternateClientColor() {
        return Client.instance.getAlternateClientColor();
    }

    public Color[] getClientColors() {
        Color firstColor;
        Color secondColor;
        firstColor = mixColors(getClientColor(), getAlternateClientColor());
        secondColor = mixColors(getAlternateClientColor(), getClientColor());
        return new Color[]{firstColor, secondColor};
    }

    private Color mixColors(Color color1, Color color2) {
        return ColorUtil.interpolateColorC(color1, color2, 0);
    }

    public ParticleEngine pe = new ParticleEngine();
    private final AnimationUtils animationUtils = new AnimationUtils();
    float aniX, aniY;
    //ShaderUtil shaderUtil = new ShaderUtil("client/shaders/background.frag");
    //ShaderUtil shaderUtil2 = new ShaderUtil("client/shaders/background2.frag");//https://glslsandbox.com/e#99676.1
    //ShaderUtil shaderUtil3 = new ShaderUtil("client/shaders/background3.frag");//https://glslsandbox.com/e#99650.0
    //ShaderUtil shaderUtil4 = new ShaderUtil("client/shaders/background4.frag");//https://glslsandbox.com/e#99935.0
    ShaderUtil shaderUtil5 = new ShaderUtil("client/shaders/background5.frag");//liquid-bounce
    ShaderUtil background = shaderUtil5;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect2(0, 0, width, height, 0);
        int scaleFactor = 1;
        float k = (float) mc.gameSettings.guiScale;
        if (k == 0.0f) {
            k = 1000.0f;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        background.init();
        background.setUniformf("iTime", (System.currentTimeMillis() - start) / 1000f);
        background.setUniformf("iResolution", width * scaleFactor, height * scaleFactor);
        if (Long.MAX_VALUE - 100 < start) {
            start = 0;
        }
        ShaderUtil.drawQuads();
        background.unload();
        GlStateManager.disableBlend();

        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Color[] clientColors = getClientColors();
        GradientUtil.applyGradientHorizontal((this.width - FontUtil.LOGO.getFont().getStringWidth("ULTRA")) / 2f, this.height / 2f - 32, (float) FontUtil.LOGO.getFont().getStringWidth("ULTRA"), FontUtil.LOGO.getFont().getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            FontUtil.LOGO.getFont().drawString("ULTRA", (this.width - FontUtil.LOGO.getFont().getStringWidth("ULTRA")) / 2f, this.height / 2f - 32, 0xffffffff);
        });

        FontUtil.TITLE.getFont().drawCenteredString("C L I E N T", this.width / 2f - 0.25F, this.height / 2f - 13, new Color(30, 30, 30, 70).getRGB());
        FontUtil.TITLE.getFont().drawCenteredString("C L I E N T", this.width / 2f, this.height / 2f - 14, -1);

        this.btnSingleplayer.drawButton(mouseX, mouseY);
        this.btnMultiplayer.drawButton(mouseX, mouseY);

        this.btnLunarOptions.drawButton(mouseX, mouseY);
        this.btnCosmetics.drawButton(mouseX, mouseY);
        this.btnMinecraftOptions.drawButton(mouseX, mouseY);
        this.btnLanguage.drawButton(mouseX, mouseY);
        this.btnOAuth.drawButton(mouseX, mouseY);

        this.btnQuit.drawButton(mouseX, mouseY);

        String Copyright = "Copyright Mojang Studios. Made by Timer, yuxiangll and Minelimer.";
        try {
            FontUtil.TEXT.getFont().drawString("ULTRA Client 1.8.9 (" + ((String) ProtectionLoader.getLoadedClass().get("CheckMD5").getMethod("getMD5_num").invoke(null)).substring(0, 6) + "/master)", 7, this.height - 11, new Color(255, 255, 255, 100).getRGB());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        FontUtil.TEXT.getFont().drawString(Copyright, this.width - FontUtil.TEXT.getFont().getStringWidth(Copyright) - 6, this.height - 11, new Color(255, 255, 255, 100).getRGB());

        aniX = animationUtils.animate(mouseX, aniX, 0.07f);
        aniY = animationUtils.animate(mouseY, aniY, 0.07f);
        pe.render(aniX, aniY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
