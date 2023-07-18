package cn.timer.ultra.gui.ListedClickUi.component;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.ListedClickUi.component.components.Button;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.color.ColorUtils;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

    public ArrayList<Component> components;
    public Category category;
    private boolean open;
    private final int width;
    private int y;
    private int x;
    private final int barHeight;
    public boolean isDragging;
    public int dragX;
    public int dragY;

    public Frame(Category cat) {
        this.components = new ArrayList<>();
        this.category = cat;
        this.width = 88;
        this.x = 5;
        this.y = 5;
        this.barHeight = 13;
        this.dragX = 0;
        this.open = true;
        this.isDragging = false;
        int tY = this.barHeight;
        for (Module mod : Client.instance.getModuleManager().getModules()) {
            if (!mod.getCategory().equals(cat)) continue;
            Button modButton = new Button(mod, this, tY);
            this.components.add(modButton);
            tY += 12;
        }
        size = Client.instance.getModuleManager().getByCategory(cat).size() * 12;
        off = -size;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    AnimationUtils a = new AnimationUtils();
    float off;
    int target;
    int size;

    public void renderFrame() {
        if (!this.components.isEmpty()) {
            if (this.open) {
                target = barHeight;
            } else {
                target = -size;
            }
            int ty = 0;
            for (Component component : components) {
                off = a.animate(target, off, 0.1f);
                component.setOff((int) off + ty);
                ty += component.getHeight();
                if (component.getOff() >= 0)
                    component.renderComponent();
                if (!this.open) {
                    ((Button) component).open = false;
                }
            }
        }
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, new Color(224, 224, 224, 255).getRGB());
        UniFontLoaders.jello18.drawString(this.category.name(), this.x + (this.width - UniFontLoaders.jello18.getStringWidth(this.category.name())) / 2f, this.y + (this.barHeight - UniFontLoaders.jello18.getHeight()) / 2f, new Color(75, 75, 75, 255).getRGB());
        for (int i = 0; i < width; ++i)
            RenderUtil.drawRect2((float) (x + i), y + barHeight - 1f, 1.0f, 1.0f, ColorUtils.rainbow(25000000L * i).getRGB());
        RenderUtil.drawShadow2(this.x, this.y, this.x + this.width, this.y + this.barHeight);
    }

    public void refresh() {
        int off = this.barHeight;
        for (Component comp : components) {
            comp.setOff(off);
            off += comp.getHeight();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void updatePosition(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - dragX);
            this.setY(mouseY - dragY);
        }
    }

    public boolean isWithinHeader(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

}
