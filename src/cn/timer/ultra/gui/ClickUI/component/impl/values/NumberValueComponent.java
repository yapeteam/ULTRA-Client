package cn.timer.ultra.gui.ClickUI.component.impl.values;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.RoundedUtil;
import cn.timer.ultra.values.Numbers;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberValueComponent implements Component {
    Numbers<Double> value;
    boolean dragging = false;
    float x, y;

    public NumberValueComponent(Numbers<Double> setting) {
        this.value = setting;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 30f;
        double diff = Math.min(width * (value.floatMax() - value.floatMin()) / (value.floatMax() - value.floatMin()), Math.max(0, mouseX - lx - 10));
        this.x = x;
        this.y = y;
        UniFontLoaders.jello14.drawString(value.getName() + ":" + String.format("%.1f", value.floatValue()), lx, y, new Color(0, 0, 0).getRGB());

        UniFontLoaders.jello14.drawString("-", lx, y + 11, setAlpha(new Color(94, 94, 94)).getRGB());
        UniFontLoaders.jello14.drawString("+", lx + ClickUIScreen.rightWidth - 12, y + 11, setAlpha(new Color(66, 66, 66)).getRGB());

        RoundedUtil.drawRound(lx + 10, y + 15, (width * (value.floatMax() - value.floatMin()) / (value.floatMax() - value.floatMin())), 3, 1, setAlpha(new Color(222, 222, 222)));
        RoundedUtil.drawRound(lx + 10, y + 15, (width * (value.floatValue() - value.floatMin()) / (value.floatMax() - value.floatMin())), 3, 1, setAlpha(new Color(87, 175, 255)));
        RenderUtil.circle(lx + 10 + (width * (value.floatValue() - value.floatMin()) / (value.floatMax() - value.floatMin())), y + 16.5, 3, setAlpha(new Color(87, 175, 255)).getRGB());
        if (dragging && Mouse.isButtonDown(0)) {
            if (diff == 0) {
                value.setValue((double) value.floatMin());
            } else {
                double newValue = roundToPlace(((diff / width) * (value.floatMax() - value.floatMin()) + value.floatMin()), 2);
                value.setValue(newValue);
            }
        } else
            dragging = false;
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 30f;
        if (isHovered(lx + 10 - 1.5f, y + 15f - 1.5f, width + 3, 3 + 3, mouseX, mouseY) && mouseButton == 0 && !this.dragging) {
            this.dragging = true;
        }
    }

    private double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
        this.dragging = false;
    }
}
