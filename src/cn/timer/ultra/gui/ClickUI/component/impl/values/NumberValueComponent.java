package cn.timer.ultra.gui.ClickUI.component.impl.values;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.values.Numbers;

import java.awt.*;

public class NumberValueComponent implements Component {
    Numbers<Double> value;
    boolean dragging = false;
    float x, y;

    public NumberValueComponent(Numbers<Double> setting) {
        this.value = setting;
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        FontLoaders.jello14.drawString(value.getName() + ":" + String.format("%.1f", value.getValueF()), lx, y, new Color(0, 0, 0).getRGB());

        FontLoaders.jello14.drawString("-", lx, y + 11, new Color(94, 94, 94).getRGB());
        FontLoaders.jello14.drawString("+", lx + ClickUIScreen.rightWidth - 12, y + 11, new Color(66, 66, 66).getRGB());

        float width = ClickUIScreen.rightWidth - 30f;
        RenderUtil.drawRoundedRect(lx + 10, y + 15, lx + 10 + (width * (value.getMaxF() - value.getMinF()) / (value.getMaxF() - value.getMinF())), y + 18, 1, new Color(222, 222, 222).getRGB());
        RenderUtil.drawRoundedRect(lx + 10, y + 15, lx + 10 + (width * (value.getValueF() - value.getMinF()) / (value.getMaxF() - value.getMinF())), y + 18, 1, new Color(87, 175, 255).getRGB());
        if (dragging) {
            if (isHovered(lx + 10, y + 15f, width, 3, mouseX, mouseY)) {
                float percent = (mouseX - (lx + 10f)) / width;
                value.setValue((double) (value.getMinF() + (value.getMaxF() - value.getMinF()) * percent));
            }
            if (mouseX >= lx + 10 + width) {
                value.setValue((double) value.getMaxF());
            }
            if (mouseX <= lx + 10) {
                value.setValue((double) value.getMinF());
            }
        }
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 30f;
        if (isHovered(lx + 10, y + 15f, width, 3, mouseX, mouseY) && mouseButton == 0) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
        this.dragging = false;
    }
}
