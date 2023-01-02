package cn.timer.ultra.gui.ClickUI;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.ClickUI.component.impl.ModuleComponent;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.AnimationUtils;
import cn.timer.ultra.utils.ColorUtil;
import cn.timer.ultra.utils.GradientUtil;
import cn.timer.ultra.utils.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cn.timer.ultra.gui.ClickUI.component.Component;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickUIScreen extends GuiScreen {
    public static float x, y;
    public static float width = 485, height = 295;
    public static float topHeight = 20;
    private float scrollY = 0;
    public static float leftWidth = 110;
    public static float rightWidth = 200;
    public static ArrayList<Component> components = new ArrayList<>();

    private Category currentCategory = Category.Combat;
    public static ModuleComponent currentModule;
    public static Color backgroundColor = new Color(255, 255, 255);
    public static Color boxColor = new Color(244, 244, 244);
    public static Color themeColor = new Color(24, 143, 254);

    // drag
    float dragX, dragY;
    boolean dragging;
    boolean sizeDragging;


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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor(x, y, width, height);
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
        RenderUtil.drawRoundedRect(x, y, x + width, y + height, 3, backgroundColor.getRGB());
        RenderUtil.drawRoundedRect(x, y, x + leftWidth, y + height, 3, boxColor.getRGB());
        if (currentCategory != null) {
            FontLoaders.jello18.drawString(currentCategory.name(), x + leftWidth + 5, y + 5, new Color(66, 66, 66).getRGB());
        }
        if (currentModule != null) {
            RenderUtil.drawRoundedRect(x + width - rightWidth, y, x + width, y + height, 3, boxColor.getRGB());
            FontLoaders.jello18.drawString(currentModule.module.getName() + " Settings:", x + width - rightWidth + 5, y + 5, new Color(66, 66, 66).getRGB());
            if (currentModule.module.getValues().isEmpty()) {
                String s = "There's Nothing here :(";
                FontLoaders.jello14.drawString(s, x + width - rightWidth + (rightWidth - FontLoaders.jello14.getStringWidth(s)) / 2f, y + (height - FontLoaders.jello14.getHeight()) / 2f, new Color(66, 66, 66).getRGB());
            }
        }
        //RenderUtil.drawImage(new ResourceLocation("client/icons/logo.png"), x + 5, y + 5, (537 / 5.5f), (211 / 5.5f));
        Color[] clientColors = getClientColors();
        GradientUtil.applyGradientHorizontal(x + 5, y + 30, (float) FontLoaders.logo.getWidth("ULTRA"), FontLoaders.logo.getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            FontLoaders.logo.drawString("ULTRA", x + 5 + 0.5f, y + 30 + 0.5f, 0x00000000);
            FontLoaders.logo.drawString("ULTRA", x + 5, y + 30, 0xffffffff);
        });
        float cy = y + topHeight + 30;
        for (Category c : Category.values()) {
            if (isHovered(x, cy - 2, leftWidth, 18, mouseX, mouseY) && Mouse.isButtonDown(0)) {
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
            if (currentCategory == c)
                RenderUtil.drawRect(x, cy - 2, x + leftWidth, cy + 18, themeColor.getRGB());
            RenderUtil.drawImage(new ResourceLocation("client/icons/category/" + c.name() + ".png"), x + 5, cy, 16, 16, currentCategory == c ? new Color(255, 255, 255) : new Color(0, 0, 0));
            FontLoaders.arial18.drawString(c.name(), x + 23, cy + 4, currentCategory == c ? new Color(255, 255, 255).getRGB() : new Color(0, 0, 0).getRGB());
            cy += 20;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.doGlScissor(x, y + topHeight, width, height - topHeight);
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
}
