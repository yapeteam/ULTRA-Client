package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.event.events.EventLoop;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.gui.Font.CFont.CFontLoaders;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.*;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.utils.jello.CircleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.Render);
        this.setEnabled(true);
        for (int i = 0; i < Category.values().length; i++) {
            CatA[i] = new AnimationUtils();
        }
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

        if (e.getKey() == Keyboard.KEY_UP) {
            Client.instance.tabgui.keyUp();
        }
        if (e.getKey() == Keyboard.KEY_DOWN) {
            Client.instance.tabgui.keyDown();
        }
        if (e.getKey() == Keyboard.KEY_LEFT) {
            Client.instance.tabgui.keyLeft();
        }
        if (e.getKey() == Keyboard.KEY_RIGHT) {
            Client.instance.tabgui.keyRight();
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
        if (Client.instance.tabgui.showModules) {
            int categoryIndex = Client.instance.tabgui.currentCategory;
            Category category = Client.instance.tabgui.cats.get(categoryIndex);
            List<Module> modules = Client.instance.getModuleManager().getByCategory(category);
            int currentModule = category.selectedIndex;

            category.lastSelectedTrans = category.selectedTrans;

            category.selectedTrans += (((currentModule * 15) - category.selectedTrans) / (2.5f)) + 0.01;
        }
    }

    AnimationUtils[] CatA = new AnimationUtils[Category.values().length];

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
        if (Client.instance.getModuleManager().getByClass(TabGUI.class).isEnabled()) {
            Module module = Client.instance.getModuleManager().getByClass(TabGUI.class);
            float TabX = ((TabGUI) module).getXPosition();
            float TabY = ((TabGUI) module).getYPosition();
            mc.getTextureManager().bindTexture(new ResourceLocation("Jello/TabGUIShadow.png"));
            Gui.drawModalRectWithCustomSizedTexture(TabX - 4.5f, TabY - 4.5f, 0, 0, 84, 86, 84, 86);
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GL11.glEnable(3042);
            GL11.glColor4f(1, 1, 1, 1);

            if (Client.instance.tabgui.showModules) {
                int categoryIndex = Client.instance.tabgui.currentCategory;
                Category category = Client.instance.tabgui.cats.get(categoryIndex);
                java.util.List<Module> modules = Client.instance.getModuleManager().getByCategory(category);
                int size = modules.size();

                float trans = category.selectedTrans;
                float lastTrans = category.lastSelectedTrans;

                float smoothTrans = smoothTrans(trans, lastTrans);

                if (size != 0) {
                    GlStateManager.disableAlpha();
                    GlStateManager.enableBlend();
                    GL11.glEnable(3042);
                    GL11.glColor4f(1, 1, 1, 1);

                    mc.getTextureManager().bindTexture(new ResourceLocation("Jello/TabGUISelector.png"));
                    Gui.drawModalRectWithCustomSizedTexture(TabX + 80, TabY + smoothTrans, 0, 0, 75 + 10, 17.5f, 75, 17.5f);
                }
                int y1 = 0;
                for (Module m : modules) {
                    if (y1 == 0) {
                        mc.getTextureManager().bindTexture(new ResourceLocation("Jello/TabGUIShadow2.png"));
                        Gui.drawModalRectWithCustomSizedTexture(TabX + 0.5f + 75, TabY - 4.5f, 0, 0, 84 + 10, 20, 84 + 10, 86);
                    } else if (y1 == size - 1) {
                        mc.getTextureManager().bindTexture(new ResourceLocation("Jello/TabGUIShadow2.png"));
                        Gui.drawModalRectWithCustomSizedTexture(TabX + 0.5f + 75, TabY - 4.5f + 15 * y1 + 5f, 0, 64.5f, 84 + 10,
                                20, 84 + 10, 86);
                    } else {
                        mc.getTextureManager().bindTexture(new ResourceLocation("Jello/TabGUIShadow2.png"));
                        Gui.drawModalRectWithCustomSizedTexture(TabX + 0.5f + 75, TabY - 4.5f + 15 * y1 + 5f, 0, 30, 84 + 10, 15,
                                84 + 10, 86);

                    }
                    if (m.isEnabled()) {
                        FontLoaders.jelloB18.drawString(m.getName(), TabX + 80 + 11 / 2f, TabY + 15 * y1 + 5, -1);
                    } else {
                        CFontLoaders.jello18.drawString(m.getName(), TabX + 80 + 11 / 2f, TabY + 15 * y1 + 5, -1);
                    }
                    y1++;
                }
            }

            for (Category c : Client.instance.tabgui.cats) {
                boolean selected = c.equals(Client.instance.tabgui.cats.get(Client.instance.tabgui.currentCategory));
                if (selected) {
                    if (!Float.isFinite(Client.instance.tabgui.seenTrans)) {
                        Client.instance.tabgui.seenTrans = 0;
                    }
                    Client.instance.tabgui.seenTrans += (((Client.instance.tabgui.currentCategory * 15) - Client.instance.tabgui.seenTrans)
                            / (5 * Minecraft.getDebugFPS() * 0.04)) - 0.001;
                    Client.instance.tabgui.seenTrans += (((Client.instance.tabgui.currentCategory * 15) - Client.instance.tabgui.seenTrans)
                            / (5 * Minecraft.getDebugFPS() * 0.04)) - 0.001;
                    mc.getTextureManager().bindTexture(new ResourceLocation("Jello/TabGUISelector.png"));
                    Gui.drawModalRectWithCustomSizedTexture(TabX, TabY + Client.instance.tabgui.seenTrans, 0, 0, 75, 17, 75, 17);
                }
            }

            int x = 0;
            for (Category c : Client.instance.tabgui.cats) {
                boolean selected = c.equals(Client.instance.tabgui.cats.get(Client.instance.tabgui.currentCategory));

                if (!Float.isFinite(c.seenTrans)) {
                    c.seenTrans = 0;
                }
                c.seenTrans = CatA[x].animate(selected ? 7 : 0, c.seenTrans, 0.1f);
                //mc.getTextureManager().bindTexture(new ResourceLocation("Jello/" + c.name + ".png"));
                //Gui.drawModalRectWithCustomSizedTexture(TabX + c.seenTrans, TabY + x * 15, 0, 0, 75, 17, 75, 17);
                CFontLoaders.jello21.drawString(c.name, TabX + c.seenTrans + 4, TabY + x * 15 + (17 - CFontLoaders.jello21.getHeight()) / 2f, -1);
                x++;
            }
        }
    }

    public float smoothTrans(double current, double last) {
        return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    }
}
