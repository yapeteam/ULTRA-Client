package cn.timer.ultra.gui.ClickUI.component.impl.values;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.values.Booleans;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class BooleanValueComponent implements Component {
    Booleans<Boolean> value;
    float x, y;

    public BooleanValueComponent(Booleans<Boolean> setting) {
        this.value = setting;
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        RenderUtil.drawImage(new ResourceLocation("client/icons/clickgui/" + (value.getValue() ? "enabled" : "disabled") + ".png"), lx, y, 8, 8);
        FontLoaders.jigsaw14.drawString(value.getName(), lx + 12, y, new Color(0, 0, 0).getRGB());
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        if (mouseButton == 0 && isHovered(lx, y, 8, 8, mouseX, mouseY)) {
            value.setValue(!value.getValue());
        }
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
    }
}
