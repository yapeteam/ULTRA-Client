package cn.timer.ultra.module;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.events.EventDrawGui;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.utils.MathUtils;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;

public abstract class HUDModule extends Module {
    public boolean dragging = false;
    public float width = 0;
    public float height = 0;
    public int alpha = 0;
    public final Mode<String> horizontalFacing = new Mode<>("HorizontalFacing", new String[]{"Left", "Middle", "Right", "Free"}, "Free");
    public final Mode<String> verticalFacing = new Mode<>("VerticalFacing", new String[]{"Top", "Middle", "Bottom", "Free"}, "Free");
    public final Numbers<Float> subXPosition;
    public final Numbers<Float> subYPosition;

    public HUDModule(String name, int key, Category category) {
        super(name, key, category);
        ScaledResolution sr = new ScaledResolution(mc);
        this.subXPosition = new Numbers<>("posX", 0f, sr.getScaledWidth() - width, 1f, 0f);
        this.subYPosition = new Numbers<>("posY", 0f, sr.getScaledHeight() - height, 1f, 0f);
        addValues(this.subXPosition, this.subYPosition, this.horizontalFacing, this.verticalFacing);
    }

    public HUDModule(String name, int key, Category category, float xPosition, float yPosition, float width, float height, String horizontalFacing, String verticalFacing) {
        super(name, key, category);
        ScaledResolution sr = new ScaledResolution(mc);
        this.subXPosition = new Numbers<>("posX", 0f, sr.getScaledWidth() - width, 1f, xPosition);
        this.subYPosition = new Numbers<>("posY", 0f, sr.getScaledHeight() - height, 1f, yPosition);
        this.width = width;
        this.height = height;
        for (String horizontalFacing1 : this.horizontalFacing.getModes()) {
            if (horizontalFacing1.equalsIgnoreCase(horizontalFacing)) {
                this.horizontalFacing.setValue(horizontalFacing1);
            }
        }
        for (String verticalFacing1 : this.verticalFacing.getModes()) {
            if (verticalFacing1.equalsIgnoreCase(verticalFacing)) {
                this.verticalFacing.setValue(verticalFacing1);
            }
        }
        addValues(this.subXPosition, this.subYPosition, this.horizontalFacing, this.verticalFacing);
    }

    private int fade(int target, int current, int speed) {
        if (MathUtils.getDifference(target, current) <= speed) {
            return target;
        }
        return target > current ? current + speed : current - speed;
    }

    public void updateAlpha(boolean selected) {
        this.alpha = fade(selected ? 255 : 0, this.alpha, 10);
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= getXPosition() && mouseY >= getYPosition() && mouseX <= getXPosition() + width && mouseY <= getYPosition() + height;
    }

    public boolean isHoveringOnSettings(int mouseX, int mouseY) {
        return mouseX >= getXPosition() && mouseY >= getYPosition() && mouseX <= getXPosition() + FontLoaders.icon20.getStringWidth("N") && mouseY <= getYPosition() + FontLoaders.icon20.getHeight();
    }

    public void drawOutline(int mouseX, int mouseY) {
        if (this.alpha != 0) {
            RenderUtil.drawRoundedBorder(getXPosition() - 5, getYPosition() - 5, getXPosition() + width + 5, getYPosition() + height + 5, 5, 5, new Color(255, 255, 255, this.alpha).getRGB());
            FontLoaders.icon20.drawString("N", getXPosition(), getYPosition(), isHoveringOnSettings(mouseX, mouseY) ? Client.instance.getClientColor().getRGB() : new Color(255, 255, 255, this.alpha).getRGB());
        }
    }

    public float getXPosition() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        switch (horizontalFacing.getValue().toLowerCase()) {
            case "left": {
                return 0;
            }
            case "middle": {
                return (sr.getScaledWidth() - width) / 2f;
            }
            case "right": {
                return sr.getScaledWidth() - width;
            }
        }
        return subXPosition.getValueF();
    }

    public float getYPosition() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        switch (verticalFacing.getValue().toLowerCase()) {
            case "top": {
                return 0;
            }
            case "middle": {
                return (sr.getScaledHeight() - height) / 2f;
            }
            case "bottom": {
                return sr.getScaledHeight() - height;
            }
        }
        return subYPosition.getValueF();
    }

    public void onGuiScreen(EventDrawGui e) {
        ScaledResolution sr = new ScaledResolution(mc);
        subXPosition.setMax(sr.getScaledWidth() - width);
        subYPosition.setMax(sr.getScaledHeight() - height);
        if (e.getCurrentScreen() == Client.instance.clickGui) {
            drawOutline(e.getMouseX(), e.getMouseY());
            updateAlpha(isHovering(e.getMouseX(), e.getMouseY()));
            if (isHovering(e.getMouseX(), e.getMouseY()) && Mouse.isButtonDown(0)) {
                this.dragging = true;
            }
            if (!Mouse.isButtonDown(0)) {
                this.dragging = false;
            }
            if (dragging) {
                if (horizontalFacing.getValue().equals("Free")) this.subXPosition.setValue((float) e.getMouseX());
                if (verticalFacing.getValue().equals("Free")) this.subYPosition.setValue((float) e.getMouseY());
            }
        }
    }

    public void drawHUD() {
    }
}
