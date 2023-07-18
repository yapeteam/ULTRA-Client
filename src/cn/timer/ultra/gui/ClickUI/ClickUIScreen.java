package cn.timer.ultra.gui.ClickUI;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRenderShadow;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.ClickUI.component.impl.ModuleComponent;
import cn.timer.ultra.gui.ClickUI.component.impl.MusicPlayerComponent;
import cn.timer.ultra.gui.ClickUI.screen.HUDEditor;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.module.modules.render.ClickGUI;
import cn.timer.ultra.utils.soar.ClickEffect;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.render.*;
import javafx.embed.swing.JFXPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClickUIScreen extends GuiScreen {
    public static float x, y;
    public static float width = 320, height = 190;
    public static float topHeight = 20;
    private float scrollY = 0;
    public static float leftWidth = 110;
    public static float rightWidth = 200;
    public static ArrayList<Component> components = new ArrayList<>();
    public static MusicPlayerComponent musicPlayerComponent = new MusicPlayerComponent();

    private Panels currentPanel = Panels.Modules;
    public static ModuleComponent currentModule;
    public static Color backgroundColor = new Color(255, 255, 255);
    public static Color boxColor = new Color(244, 244, 244);
    public static int globalAlpha = 255;
    private final List<ClickEffect> clickEffects = new ArrayList<>();

    // drag
    float dragX, dragY;
    public boolean dragging;
    public boolean sizeDragging;
    public static boolean close;
    public static boolean open;
    private boolean editor;

    public ClickUIScreen() {
        SwingUtilities.invokeLater(JFXPanel::new);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void initGui() {
        if (!Client.instance.getModuleManager().getByClass(ClickGUI.class).isEnabled()) {
            mc.displayGuiScreen(null);
            Client.instance.getModuleManager().getByClass(ClickGUI.class).setConEnabled(true);
        }
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        if (x <= 0 && y <= 0) {
            x = sr.getScaledWidth() / 2f - width / 2;
            y = sr.getScaledHeight() / 2f - height / 2;
        }
        if (components.isEmpty()) {
            if (currentPanel == Panels.Modules) {
                for (Module mod : Client.instance.getModuleManager().getModules()) {
                    if (mod instanceof CombatModule && !Client.instance.getModuleManager().getByClass(ClickGUI.class).vape)
                        continue;
                    components.add(new ModuleComponent(mod));
                }
            }
        }
        close = false;
        editor = false;
        closeScale = 1;
        open = true;
        openScale = 0;
        openProcess = 1;
        for (int i = 0; i < Panels.values().length; i++) {
            ButtonScale[i] = 0f;
        }
        EventManager.instance.register(this);
        start = System.currentTimeMillis();
    }

    float ami;
    AnimationUtils animationUtils = new AnimationUtils();
    float lmx, lmy;

    public Color[] getClientColors() {
        return new Color[]{Client.instance.getClientColor(), Client.instance.getAlternateClientColor()};
    }

    private final Float[] ButtonScale = new Float[Panels.values().length];

    @Override
    public void updateScreen() {
        if (!Client.instance.getModuleManager().getByClass(ClickGUI.class).isEnabled()) {
            mc.displayGuiScreen(null);
        }
    }

    @EventTarget
    private void renderShadow(EventRenderShadow e) {
        if (mc == null) return;
        GlStateManager.pushMatrix();
        float centreX;
        float centreY;
        centreX = x + width / 2;
        centreY = y + height / 2;
        if (close) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(closeScale, closeScale, closeScale);
            GlStateManager.translate(-centreX, -centreY, 0);
        }
        if (open) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(openScale, openScale, openScale);
            GlStateManager.translate(-centreX, -centreY, 0);
        }
        RoundedUtil.drawRound(x, y, width, height, 10, backgroundColor);
        GlStateManager.popMatrix();
    }

    @Override
    public void onGuiClosed() {
        EventManager.instance.unregister(this);
        Client.instance.getModuleManager().getByClass(ClickGUI.class).setConEnabled(false);
        Keyboard.enableRepeatEvents(false);
        for (Component component : components) {
            component.mouseReleased(0, 0, 0);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            close = true;
            editor = false;
        }
        if (currentPanel == Panels.MusicPlayer) {
            musicPlayerComponent.keyTyped(typedChar, keyCode);
        }
    }

    private final AnimationUtils rightAni = new AnimationUtils();
    private float openProcess;
    private float closeScale, openScale;
    long start;

    ShaderUtil panelBackground = new ShaderUtil("client/shaders/background3.frag");

    public void doRender(int mouseX, int mouseY) {
        float centreX;
        float centreY;
        centreX = x + width / 2;
        centreY = y + height / 2;
        if (close && closeScale < 3) {
            closeScale -= (closeScale - 4) / (7 / (50f / Minecraft.getDebugFPS()));
        }
        if (close) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(closeScale, closeScale, closeScale);
            GlStateManager.translate(-centreX, -centreY, 0);
            if (globalAlpha < 30 && !editor) {
                mc.displayGuiScreen(null);
            }
        }

        if (open && openScale < 1.3 && openProcess == 1) {
            openScale -= (openScale - 1.4) / (7 / (50f / Minecraft.getDebugFPS()));
        } else if (openScale >= 1.3) {
            openProcess = 0;
        }
        if (open && openProcess == 0) {
            openScale -= (openScale - 1) / (7 / (120f / Minecraft.getDebugFPS()));
            if (openScale <= 1.01f) open = false;
        }
        if (open) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(openScale, openScale, openScale);
            GlStateManager.translate(-centreX, -centreY, 0);
        }

        if (globalAlpha < 30 && editor) {
            mc.displayGuiScreen(new HUDEditor(this));
        }

        backgroundColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), globalAlpha);
        boxColor = new Color(boxColor.getRed(), boxColor.getGreen(), boxColor.getBlue(), globalAlpha);
        if (close) {
            globalAlpha -= globalAlpha / (3.5 / (60f / Minecraft.getDebugFPS()));
            if (globalAlpha > 255) globalAlpha = 255;
            if (globalAlpha < 0) globalAlpha = 0;
        } else globalAlpha = 255;
        if (globalAlpha < 30) return;

        /* Render */
        Stencil.write(false);
        RoundedUtil.drawRound(x, y, width, height, 10, backgroundColor);
        Stencil.erase(true);

        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        if (sizeDragging) {
            if (width >= 320 || (mouseX > lmx && mouseX - x >= 320)) {
                width = mouseX + dragX;
            } else width = 320 - 0.1f;
            if (height >= 190 || (mouseY > lmy && mouseY - y >= 190)) {
                height = mouseY + dragY;
            } else height = 190 - 0.1f;
        }
        lmx = mouseX;
        lmy = mouseY;
        rightWidth = rightAni.animate(currentModule != null ? width / 3f : 0, rightWidth, 0.05f);
        RoundedUtil.drawRound(x, y, width, height, 10, backgroundColor);
        RoundedUtil.drawRound(x, y, leftWidth, height, 10, boxColor);
        Color[] clientColors = getClientColors();
        //logo
        GradientUtil.applyGradientHorizontal(x + 5, y + 30, (float) UniFontLoaders.logo.getStringWidth("ULTRA"), UniFontLoaders.logo.getHeight(), globalAlpha / 255f, clientColors[0], clientColors[1], () -> {
            UniFontLoaders.logo.drawString("ULTRA", x + 5 + 0.5f, y + 30 + 0.5f, 0x00000000);
            UniFontLoaders.logo.drawString("ULTRA", x + 5, y + 30, 0xffffffff);
        });
        //HUD Editor
        if (isHovered(x + 5, y + height - 15, 10, 10, mouseX, mouseY)) {
            RoundedUtil.drawRound(x + 5 - 2, y + height - 15 - 2, 10 + 4, 10 + 4, 3, backgroundColor.darker());
        }
        RenderUtil.drawImage(new ResourceLocation("client/icons/clickgui/editor.png"), x + 5, y + height - 15, 10, 10, globalAlpha / 255f);
        if (currentPanel != null) {//panel
            UniFontLoaders.PingFangMedium18.drawString(currentPanel.name(), x + leftWidth + 5, y + 5, new Color(66, 66, 66, globalAlpha).getRGB());
        }
        if (currentModule != null) {
            RoundedUtil.drawRound(x + width - rightWidth, y, rightWidth, height, 10, boxColor);
            UniFontLoaders.PingFangMedium18.drawString(currentModule.module.getName() + " Settings:", x + width - rightWidth + 5, y + 5, new Color(66, 66, 66, globalAlpha).getRGB());
            if (currentModule.module.getValues().isEmpty()) {
                String s = "There's Nothing here :(";
                UniFontLoaders.PingFangLight18.drawString(s, x + width - rightWidth + (rightWidth - UniFontLoaders.PingFangLight18.getStringWidth(s)) / 2f, y + (height - UniFontLoaders.PingFangLight18.getHeight()) / 2f, new Color(66, 66, 66, globalAlpha).getRGB());
            }
        }
        //Panel
        float cy = y + topHeight + 30;
        Stencil.write(false);
        for (int index = 0; index < Panels.values().length; index++) {
            GL11.glPushMatrix();
            GlStateManager.translate(x + 5 + (leftWidth - 10) / 2f, cy - 2 + 18 / 2f, 0);
            GlStateManager.scale(ButtonScale[index], ButtonScale[index], ButtonScale[index]);
            GlStateManager.translate(-(x + 5 + (leftWidth - 10) / 2f), -(cy - 2 + 18 / 2f), 0);
            GlStateManager.disableColorLogic();
            //RenderUtil.drawRoundedRect2(x + 5, cy - 2, leftWidth - 10, 18, 5, -1);
            RoundedUtil.drawRound(x + 5, cy - 2, leftWidth - 10, 18, 5, new Color(-1));
            GL11.glPopMatrix();
            cy += 20;
        }
        Stencil.erase(true);
        RenderUtil.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ScaledResolution sr = new ScaledResolution(mc);
        panelBackground.init();
        panelBackground.setUniformf("time", (System.currentTimeMillis() - start) / 1000f);
        panelBackground.setUniformf("resolution", sr.getScaledWidth() * sr.getScaleFactor(), sr.getScaledHeight() * sr.getScaleFactor());
        panelBackground.setUniformf("mouse", mouseX / 10f, mouseY / 10f);
        panelBackground.setUniformf("alpha", globalAlpha / 255f);
        if (Long.MAX_VALUE - 100 < start) {
            start = 0;
        }
        ShaderUtil.drawQuads();
        panelBackground.unload();
        Stencil.dispose();
        GlStateManager.disableBlend();
        cy = y + topHeight + 30;
        for (int index = 0; index < Panels.values().length; index++) {
            Panels c = Panels.get(index);
            if (isHovered(x, cy - 2, leftWidth, 18, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                setCurrentPanel(c);
                if (currentPanel == Panels.MusicPlayer) {
                    currentModule = null;
                    rightWidth = 0;
                }
            }
            if (currentPanel == c) {
                if (ButtonScale[index] <= 1)
                    ButtonScale[index] += (1 - ButtonScale[index]) / (5 * (Minecraft.getDebugFPS() / 60f));
            } else {
                if (ButtonScale[index] >= 0)
                    ButtonScale[index] -= (ButtonScale[index] - 0) / (5 * (Minecraft.getDebugFPS() / 60f));
            }
            if (Float.isNaN(ButtonScale[index])) {
                ButtonScale[index] = 1F;
            }
            //RoundedUtil.drawGradientHorizontal(x + 5, cy - 2, leftWidth - 10, 18, 5, setAlpha(clientColors[0]), setAlpha(clientColors[1]));
            assert c != null;
            UniFontLoaders.PingFangMedium18.drawString(c.name(), x + (leftWidth - UniFontLoaders.PingFangMedium18.getStringWidth(c.name())) / 2f, cy + (18 - UniFontLoaders.arial18.getHeight()) / 2f, setAlpha(new Color(0)).getRGB());
            cy += 20;
        }
        Stencil.dispose();
        Stencil.write(false);
        RenderUtil.drawRect2(x, y + topHeight, width, height - topHeight, 0);
        Stencil.erase(true);
        float modY = y + topHeight + ami;
        if (currentPanel == Panels.Modules) {
            for (Component component : components) {
                component.draw(x + leftWidth + 5, modY, mouseX, mouseY);
                modY += 35;
            }
            if (currentModule != null) {
                currentModule.drawSubComponents(x + width - rightWidth + 10, y + topHeight, mouseX, mouseY);
            }
        }
        if (currentPanel == Panels.MusicPlayer) {
            musicPlayerComponent.draw(x + leftWidth + 5, y, mouseX, mouseY);
            Stencil.write(false);
            RenderUtil.drawRect2(ClickUIScreen.x + ClickUIScreen.leftWidth + 10, ClickUIScreen.y + ClickUIScreen.topHeight + 22, ClickUIScreen.width - ClickUIScreen.leftWidth - 15, ClickUIScreen.height - 50 - 20 - 25, -1);
            Stencil.erase(true);
            musicPlayerComponent.drawSubComponents(x + leftWidth + 10, y + topHeight + 22, mouseX, mouseY);
            Stencil.dispose();
        }
        Stencil.dispose();

        if (clickEffects.size() > 0) {
            Iterator<ClickEffect> clickEffectIterator = clickEffects.iterator();
            while (clickEffectIterator.hasNext()) {
                ClickEffect clickEffect = clickEffectIterator.next();
                clickEffect.draw();
                if (clickEffect.canRemove()) clickEffectIterator.remove();
            }
        }

        int dWheel = Mouse.getDWheel();
        ami = animationUtils.animate(scrollY, ami, 0.2f);
        if (dWheel > 0) scrollY += 25;
        if (dWheel < 0) scrollY -= 25;
        if (ClickUIScreen.y + ClickUIScreen.topHeight + scrollY > ClickUIScreen.y + ClickUIScreen.topHeight + 3) {
            scrollY = 0;
        }
        if ((components.size() + 1) * 35 + scrollY < ClickUIScreen.height + ClickUIScreen.topHeight) {
            scrollY = ClickUIScreen.height + ClickUIScreen.topHeight - ((components.size() + 1) * 35);
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!Client.instance.getModuleManager().getByClass(ClickGUI.class).getMotionBlur().getValue())
            doRender(mouseX, mouseY);
    }

    public void setCurrentPanel(Panels currentPanel) {
        if (this.currentPanel != currentPanel) {
            ami = -100;
            scrollY = 0;
            this.currentPanel = currentPanel;
            components.clear();
            if (currentPanel == Panels.Modules) {
                for (Module mod : Client.instance.getModuleManager().getModules()) {
                    if (mod instanceof CombatModule) continue;
                    components.add(new ModuleComponent(mod));
                }
            }
        }
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        clickEffects.add(new ClickEffect(mouseX, mouseY));
        if (isHovered(x, y, width, topHeight, mouseX, mouseY) && mouseButton == 0) {//Pos-drag
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = true;
        }
        if (isHovered(x + width - 10, y + height - 10, 10, 10, mouseX, mouseY) && mouseButton == 0) {//Size-drag
            dragX = width - mouseX;
            dragY = height - mouseY;
            sizeDragging = true;
        }
        if (isHovered(x + 5, y + height - 15, 10, 10, mouseX, mouseY) && mouseButton == 0) {
            close = true;
            editor = true;
        }

        if (currentPanel == Panels.Modules) if (currentModule != null) {
            currentModule.mouseClickedSubComponents(mouseX, mouseY, mouseButton);
        }
        if (currentPanel == Panels.Modules) for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (currentPanel == Panels.MusicPlayer) {
            musicPlayerComponent.mouseClicked(mouseX, mouseY, mouseButton);
            musicPlayerComponent.mouseClickedSubComponents(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        sizeDragging = false;
        if (currentPanel == Panels.Modules) for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
        if (currentPanel == Panels.MusicPlayer) {
            musicPlayerComponent.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }
}
