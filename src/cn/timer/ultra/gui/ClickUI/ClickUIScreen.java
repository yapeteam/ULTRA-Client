package cn.timer.ultra.gui.ClickUI;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRenderShadow;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.gui.ClickUI.component.impl.ModuleComponent;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.*;
import com.sun.org.apache.regexp.internal.RE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.timer.ultra.gui.ClickUI.component.Component;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ClickUIScreen extends GuiScreen {
    public static float x, y;
    public static float width = 485, height = 295;
    public static float topHeight = 20;
    private float scrollY = 0;
    public static float leftWidth = 110;
    public static float rightWidth = 200;
    public static ArrayList<Component> components = new ArrayList<>();

    private Category currentCategory = Category.Render;
    public static ModuleComponent currentModule;
    public static Color backgroundColor = new Color(255, 255, 255);
    public static Color boxColor = new Color(244, 244, 244);
    public static Color themeColor = new Color(24, 143, 254);

    // drag
    float dragX, dragY;
    boolean dragging;
    boolean sizeDragging;
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    AnimationUtils as = new AnimationUtils();

    public ClickUIScreen() {
        EventManager.instance.register(this);
    }

    @EventTarget
    public void onTick(EventTick e) {
        if (mc == null) return;
        if (!(mc.currentScreen instanceof ClickUIScreen)) return;
        lastPercent = percent;
        lastPercent2 = percent2;
        if (percent > .98) {
            percent += ((.98 - percent) / (1.45f)) - 0.001;
        }
        if (percent <= .98) {
            if (percent2 < 1) {
                percent2 += ((1 - percent2) / (2.8f)) + 0.002;
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        if (x <= 0 && y <= 0) {
            x = sr.getScaledWidth() / 2f - width / 2;
            y = sr.getScaledHeight() / 2f - height / 2;
        }
        if (components.isEmpty()) {
            for (Module mod : Client.instance.getModuleManager().getByCategory(currentCategory)) {
                components.add(new ModuleComponent(mod));
            }
        }
        percent = 1.23f;
        lastPercent = 1.23f;
        percent2 = 0.98f;
        lastPercent2 = 0.98f;
        for (int i = 0; i < Category.values().length; i++) {
            scale[i] = 0f;
        }
        EventManager.instance.register(this);
    }

    float ami;
    AnimationUtils animationUtils = new AnimationUtils();
    float lmx, lmy;

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

    private final Float[] scale = new Float[Category.values().length];

    @EventTarget
    private void renderShadow(EventRenderShadow e) {
        if (mc == null) return;
        GlStateManager.pushMatrix();
        float centreX;
        float centreY;
        centreX = x + width / 2;
        centreY = y + height / 2;
        float percent = smoothTrans(this.percent, lastPercent);
        float percent2 = smoothTrans(this.percent2, lastPercent2);
        if (percent > 0.98) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(percent, percent, percent);
            GlStateManager.translate(-centreX, -centreY, 0);
        } else if (percent2 <= 1) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(percent2, percent2, percent2);
            GlStateManager.translate(-centreX, -centreY, 0);
        }
        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, backgroundColor.getRGB());
        GlStateManager.popMatrix();
    }

    @Override
    public void onGuiClosed() {
        EventManager.instance.unregister(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float centreX;
        float centreY;
        centreX = x + width / 2;
        centreY = y + height / 2;
        float percent = smoothTrans(this.percent, lastPercent);
        float percent2 = smoothTrans(this.percent2, lastPercent2);
        if (percent > 0.98) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(percent, percent, percent);
            GlStateManager.translate(-centreX, -centreY, 0);
        } else if (percent2 <= 1) {
            GlStateManager.translate(centreX, centreY, 0);
            GlStateManager.scale(percent2, percent2, percent2);
            GlStateManager.translate(-centreX, -centreY, 0);
        }
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor(x - 5, y - 5, width + 10, height + 10);
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
        rightWidth = width / 3f;
        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 10, backgroundColor.getRGB());
        RenderUtil.drawRoundedRect(x, y, x + leftWidth, y + height, 10, boxColor.getRGB());
        if (currentCategory != null) {
            FontLoaders.jello18.drawString(currentCategory.name(), x + leftWidth + 5, y + 5, new Color(66, 66, 66).getRGB());
        }
        if (currentModule != null) {
            RenderUtil.drawRoundedRect(x + width - rightWidth, y, x + width, y + height, 10, boxColor.getRGB());
            FontLoaders.jello18.drawString(currentModule.module.getName() + " Settings:", x + width - rightWidth + 5, y + 5, new Color(66, 66, 66).getRGB());
            if (currentModule.module.getValues().isEmpty()) {
                String s = "There's Nothing here :(";
                FontLoaders.jello14.drawString(s, x + width - rightWidth + (rightWidth - FontLoaders.jello14.getStringWidth(s)) / 2f, y + (height - FontLoaders.jello14.getHeight()) / 2f, new Color(66, 66, 66).getRGB());
            }
        }
        Color[] clientColors = getClientColors();

        GradientUtil.applyGradientHorizontal(x + 5, y + 30, (float) FontLoaders.logo.getWidth("ULTRA"), FontLoaders.logo.getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            FontLoaders.logo.drawString("ULTRA", x + 5 + 0.5f, y + 30 + 0.5f, 0x00000000);
            FontLoaders.logo.drawString("ULTRA", x + 5, y + 30, 0xffffffff);
        });
        float cy = y + topHeight + 30;
        for (int index = 0; index < Category.values().length; index++) {
            Category c = Category.get(index);
            float finalCy = cy;
            GL11.glPushMatrix();
            GL11.glColor4f(1, 1, 1, 1);
            GlStateManager.translate(x + 5 + (leftWidth - 10) / 2f, finalCy - 2 + 18 / 2f, 0);
            GlStateManager.scale(scale[index], scale[index], scale[index]);
            GlStateManager.translate(-(x + 5 + (leftWidth - 10) / 2f), -(finalCy - 2 + 18 / 2f), 0);
            GlStateManager.disableColorLogic();
            if (isHovered(x, finalCy - 2, leftWidth, 18, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                if (!(currentCategory == c)) {
                    ami = -100;
                    scrollY = 0;
                    currentCategory = c;
                    components.clear();
                    for (Module mod : Client.instance.getModuleManager().getByCategory(currentCategory)) {
                        components.add(new ModuleComponent(mod));
                    }
                }
            }
            if (currentCategory == c) {
                if (scale[index] <= 1)
                    scale[index] += (1 - scale[index]) / 5;
            } else {
                if (scale[index] >= 0)
                    scale[index] -= (scale[index] - 0) / 5;
            }
            //RenderUtil.drawImage(new ResourceLocation("client/icons/category/" + c.name() + ".png"), x + 5, cy, 16, 16, currentCategory == c ? new Color(255, 255, 255) : new Color(0, 0, 0));
            RoundedUtil.drawGradientHorizontal(x + 5, finalCy - 2, leftWidth - 10, 18, 5, clientColors[0], clientColors[1]);
            //drawGradientRoundedRect(x + 5, finalCy - 2, x + leftWidth - 10, finalCy + 18, 5, clientColors[0], clientColors[1]);
            GL11.glPopMatrix();
            assert c != null;
            FontLoaders.arial18.drawString(c.name(), x + 40, cy + 4, currentCategory == c ? -1 : 0);
            cy += 20;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor(x, y + topHeight, width, height - topHeight - 2);
        float modY = y + topHeight + ami;
        float rmodY = y + topHeight + scrollY;
        for (Component component : components) {
            if (currentModule != null) {
                currentModule.drawSubComponents(x + width - rightWidth + 10, y + topHeight, mouseX, mouseY);
            }
            component.draw(x + leftWidth + 5, modY, mouseX, mouseY);
            modY += 35;
            rmodY += 35;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        int dWheel = Mouse.getDWheel();
        ami = animationUtils.animate(scrollY, ami, 0.2f);
        if (dWheel < 0 && rmodY - topHeight >= y + height) {
            scrollY -= 25;
        } else if (dWheel > 0 && (scrollY + 10) <= 0) {
            scrollY += 25;
        }
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovered(x, y, width, topHeight, mouseX, mouseY) && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = true;
        }
        if (isHovered(x + width - 10, y + height - 10, 10, 10, mouseX, mouseY) && mouseButton == 0) {
            dragX = width - mouseX;
            dragY = height - mouseY;
            sizeDragging = true;
        }

        if (currentModule != null) {
            currentModule.mouseClickedSubComponents(mouseX, mouseY, mouseButton);
        }
        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
        sizeDragging = false;
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public float smoothTrans(double current, double last) {
        return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    }
}
