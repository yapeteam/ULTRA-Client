package cn.timer.ultra.gui.lunar.ui;

import java.awt.Color;

import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.particles.ParticleEngine;
import cn.timer.ultra.gui.lunar.ui.buttons.ImageButton;
import cn.timer.ultra.gui.lunar.ui.buttons.MainButton;
import cn.timer.ultra.gui.lunar.ui.buttons.QuitButton;
import cn.timer.ultra.utils.AnimationUtils;
import cn.timer.ultra.utils.ColorUtil;
import cn.timer.ultra.utils.GradientUtil;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.utils.jello.GLUtils;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MainMenu extends GuiScreen {
    private ResourceLocation logo;
    private MainButton btnSingleplayer;
    private MainButton btnMultiplayer;

    private ImageButton btnLunarOptions;
    private ImageButton btnCosmetics;
    private ImageButton btnMinecraftOptions;
    private ImageButton btnLanguage;

    private QuitButton btnQuit;

    private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{
            new ResourceLocation("lunar/panorama/panorama_0.png"),
            new ResourceLocation("lunar/panorama/panorama_1.png"),
            new ResourceLocation("lunar/panorama/panorama_2.png"),
            new ResourceLocation("lunar/panorama/panorama_3.png"),
            new ResourceLocation("lunar/panorama/panorama_4.png"),
            new ResourceLocation("lunar/panorama/panorama_5.png")
    };

    private static int panoramaTimer;
    private ResourceLocation backgroundTexture;

    @Override
    public void initGui() {
        pe.particles.clear();
        aniX = GLUtils.getMouseX();
        aniY = GLUtils.getMouseY();
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", new DynamicTexture(256, 256));

        this.logo = new ResourceLocation("lunar/logo.png");

        this.btnSingleplayer = new MainButton("S I N G L E P L A Y E R", this.width / 2 - 66, this.height / 2, () -> {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        });
        this.btnMultiplayer = new MainButton("M U L T I P L A Y E R", this.width / 2 - 66, this.height / 2 + 15, () -> {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        });

        int yPos = this.height - 20;
        this.btnLunarOptions = new ImageButton("ULTRA SETTINGS", new ResourceLocation("lunar/icons/lunar.png"), this.width / 2 - 30, yPos, () -> {

        });
        this.btnCosmetics = new ImageButton("ULTRA COSMETICS", new ResourceLocation("lunar/icons/cosmetics.png"), this.width / 2 - 15, yPos, () -> {

        });
        this.btnMinecraftOptions = new ImageButton("MINECRAFT SETTINGS", new ResourceLocation("lunar/icons/cog.png"), this.width / 2, yPos, () -> {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        });
        this.btnLanguage = new ImageButton("LANGUAGE", new ResourceLocation("lunar/icons/globe.png"), this.width / 2 + 15, yPos, () -> {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        });

        this.btnQuit = new QuitButton(this.width - 17, 7);
    }

    public final Color getClientColor() {
        return new Color(236, 133, 209);
    }

    public final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
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
    private AnimationUtils animationUtils = new AnimationUtils();
    float aniX, aniY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(partialTicks);
        GlStateManager.enableAlpha();

        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        //mc.getTextureManager().bindTexture(logo);
        Color[] clientColors = getClientColors();
        GradientUtil.applyGradientHorizontal((this.width - FontUtil.LOGO.getFont().getWidth("ULTRA")) / 2f, this.height / 2f - 32, (float) FontUtil.LOGO.getFont().getWidth("ULTRA"), FontUtil.LOGO.getFont().getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            FontUtil.LOGO.getFont().drawString("ULTRA", (this.width - FontUtil.LOGO.getFont().getWidth("ULTRA")) / 2f + 0.5f, this.height / 2f - 32 + 0.5f, 0x00000000);
            FontUtil.LOGO.getFont().drawString("ULTRA", (this.width - FontUtil.LOGO.getFont().getWidth("ULTRA")) / 2f, this.height / 2f - 32, 0xffffffff);
        });
        //Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 25, this.height / 2 - 68, 0, 0, 49, 49, 49, 49);

        FontUtil.TITLE.getFont().drawCenteredString("C L I E N T", this.width / 2f - 0.25F, this.height / 2f - 13, new Color(30, 30, 30, 70).getRGB());
        FontUtil.TITLE.getFont().drawCenteredString("C L I E N T", this.width / 2f, this.height / 2f - 14, -1);

        this.btnSingleplayer.drawButton(mouseX, mouseY);
        this.btnMultiplayer.drawButton(mouseX, mouseY);

        this.btnLunarOptions.drawButton(mouseX, mouseY);
        this.btnCosmetics.drawButton(mouseX, mouseY);
        this.btnMinecraftOptions.drawButton(mouseX, mouseY);
        this.btnLanguage.drawButton(mouseX, mouseY);

        this.btnQuit.drawButton(mouseX, mouseY);

        String s = "Copyright Mojang Studios. Do not distribute!";
        FontUtil.TEXT.getFont().drawString("ULTRA Client 1.8.9 (114514/master)", 7, this.height - 11, new Color(255, 255, 255, 100).getRGB());
        FontUtil.TEXT.getFont().drawString(s, this.width - FontUtil.TEXT.getFont().getWidth(s) - 6, this.height - 11, new Color(255, 255, 255, 100).getRGB());

        aniX = animationUtils.animate(mouseX, aniX, 0.07f);
        aniY = animationUtils.animate(mouseY, aniY, 0.07f);
        pe.render(aniX, aniY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        ++panoramaTimer;
        super.updateScreen();
    }

    //   Code from GuiMainMenu.java   //

    private void drawPanorama(float p_73970_3_) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        int i = 8;

        for (int j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            float f = ((float) (j % i) / (float) i - 0.5F) / 64.0F;
            float f1 = ((float) (j / i) / (float) i - 0.5F) / 64.0F;
            float f2 = 0.0F;
            GlStateManager.translate(f, f1, f2);
            GlStateManager.rotate(MathHelper.sin(((float) panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-((float) panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();

                if (k == 1) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3) {
                    GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4) {
                    GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5) {
                    GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int l = 255 / (j + 1);
                worldrenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }

            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }

        worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }

    private void rotateAndBlurSkybox() {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        int i = 3;

        for (int j = 0; j < i; ++j) {
            float f = 1.0F / (float) (j + 1);
            int k = this.width;
            int l = this.height;
            float f1 = (float) (j - i / 2) / 256.0F;
            worldrenderer.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
            worldrenderer.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }

    private void renderSkybox(float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_3_);
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.rotateAndBlurSkybox();
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        float f = this.width > this.height ? 120.0F / (float) this.width : 120.0F / (float) this.height;
        float f1 = (float) this.height * f / 256.0F;
        float f2 = (float) this.width * f / 256.0F;
        int i = this.width;
        int j = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0D, j, this.zLevel).tex(0.5F - f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, j, this.zLevel).tex(0.5F - f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(i, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F - f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldrenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + f1, 0.5F + f2).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
    }

}
