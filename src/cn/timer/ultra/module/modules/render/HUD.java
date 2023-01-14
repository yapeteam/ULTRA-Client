package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.event.events.EventLoop;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ColorUtils;
import cn.timer.ultra.utils.GradientUtil;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.utils.Stencil;
import cn.timer.ultra.utils.jello.CircleManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.Render);
        this.setEnabled(true);
    }

    public static CircleManager Wcircles = new CircleManager();
    public static CircleManager Acircles = new CircleManager();
    public static CircleManager Scircles = new CircleManager();
    public static CircleManager Dcircles = new CircleManager();
    public static CircleManager Lcircles = new CircleManager();
    public static CircleManager Rcircles = new CircleManager();

    @EventTarget
    public void onKey(EventKey e) {
        Module module = Client.instance.getModuleManager().getByClass(KeyStrokes.class);
        float x = ((KeyStrokes) module).getXPosition();
        float y = ((KeyStrokes) module).getYPosition();
        if (e.getKey() == mc.gameSettings.keyBindForward.getKeyCode()) {
            Wcircles.addCircle(x + 24 + 1 + 24 / 2f, y + 24 / 2f, 26, 5, mc.gameSettings.keyBindForward.getKeyCode());
        }
        if (e.getKey() == mc.gameSettings.keyBindLeft.getKeyCode()) {
            Acircles.addCircle(x + 24 / 2f, y + 24 + 1 + 24 / 2f, 26, 5, mc.gameSettings.keyBindLeft.getKeyCode());
        }
        if (e.getKey() == mc.gameSettings.keyBindBack.getKeyCode()) {
            Scircles.addCircle(x + 24 + 1 + 24 / 2f, y + 24 + 1 + 24 / 2f, 26, 5, mc.gameSettings.keyBindBack.getKeyCode());
        }
        if (e.getKey() == mc.gameSettings.keyBindRight.getKeyCode()) {
            Dcircles.addCircle(x + 24 + 1 + 24 + 1 + 24 / 2f, y + 24 + 1 + 24 / 2f, 26, 5, mc.gameSettings.keyBindRight.getKeyCode());
        }
    }

    @EventTarget
    private void onLoop(EventLoop e) {
        Module module = Client.instance.getModuleManager().getByClass(KeyStrokes.class);
        float x = ((KeyStrokes) module).getXPosition();
        float y = ((KeyStrokes) module).getYPosition();
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            Lcircles.addCircle(x + 37 / 2f, y + 24 + 1 + 24 + 1 + 24, 35, 5, mc.gameSettings.keyBindAttack.getKeyCode());
        }
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            Rcircles.addCircle(x + 37 + 1.5 + 37 / 2f, y + 24 + 1 + 24 + 1 + 24, 35, 5, mc.gameSettings.keyBindUseItem.getKeyCode());
        }
    }

    @EventTarget
    private void onTick(EventTick e) {
        Wcircles.runCircles();
        Acircles.runCircles();
        Scircles.runCircles();
        Dcircles.runCircles();
        Lcircles.runCircles();
        Rcircles.runCircles();
    }

    @EventTarget
    public void onRender(EventRender2D e) {
        int width = new ScaledResolution(mc).getScaledWidth();
        int height = new ScaledResolution(mc).getScaledHeight();
        Color[] clientColors = Client.instance.getClientColors();
        GradientUtil.applyGradientHorizontal(5, 25, (float) FontLoaders.logo.getWidth("ULTRA"), FontLoaders.logo.getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            FontLoaders.logo.drawString("ULTRA", 5, 25, 0xffffffff);
        });
        if (Client.instance.getModuleManager().getModuleByName("KeyStrokes").isEnabled()) {
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            Module module = Client.instance.getModuleManager().getByClass(KeyStrokes.class);
            float keyStrokeX = ((KeyStrokes) module).getXPosition();
            float keyStrokeY = ((KeyStrokes) module).getYPosition();
            mc.getTextureManager().bindTexture(new ResourceLocation("Jello/keystrokes.png"));
            Gui.drawModalRectWithCustomSizedTexture(keyStrokeX - 5.5f, keyStrokeY - 5.5f, 0, 0, 172 / 2f, 172 / 2f, 172 / 2f, 172 / 2f);

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 26.5f - 1, keyStrokeY, keyStrokeX + 35 + 15.5f - 1, keyStrokeY + 25 - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Wcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX, keyStrokeY + 26.5f - 1, keyStrokeX + 25 - 1, keyStrokeY + 30 + 5 + 15.5f - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Acircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 51 / 2f, keyStrokeY + 26.5f - 1, keyStrokeX + 25 + 51 / 2f - 1, keyStrokeY + 30 + 5 + 15.5f - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Scircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 51 / 2f + 51 / 2f, keyStrokeY + 26.5f - 1, keyStrokeX + 25 + 51 / 2f + 51 / 2f - 1, keyStrokeY + 30 + 5 + 15.5f - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Dcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX, keyStrokeY + 26.5f + 51 / 2f - 1, keyStrokeX + 74 / 2f, keyStrokeY + 26.5f + 51 / 2f + 24 - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Lcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 77 / 2f, keyStrokeY + 26.5f + 51 / 2f - 1, keyStrokeX + 74 / 2f + 76 / 2f, keyStrokeY + 26.5f + 51 / 2f + 24 - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Rcircles.drawCircles();
            Stencil.dispose();
        }

        ArrayList<Module> modulesHasEnabled = new ArrayList<>();
        for (Module module : Client.instance.getModuleManager().modules) {
            if (!module.isEnabled()) continue;
            modulesHasEnabled.add(module);
        }
        modulesHasEnabled.sort((compare1, compare2) -> FontLoaders.jello18.getStringWidth(compare2.getName() + compare2.getSuffix()) - FontLoaders.jello18.getStringWidth(compare1.getName() + compare1.getSuffix()));
        int y = 1;
        for (Module module : modulesHasEnabled) {
            int moduleWidth = FontLoaders.jello18.getStringWidth(module.getName() + module.getSuffix());
            if (!module.getSuffix().equals(""))
                FontLoaders.jello18.drawString(module.getSuffix(), width - FontLoaders.jello18.getStringWidth(module.getSuffix()), y, new Color(200, 200, 200).getRGB());
            FontLoaders.jello18.drawString(module.getName(), width - moduleWidth - 1, y, ColorUtils.rainbow(y * -20000000L, 1).getRGB());
            y += FontLoaders.jello18.FONT_HEIGHT;
        }
    }
}
