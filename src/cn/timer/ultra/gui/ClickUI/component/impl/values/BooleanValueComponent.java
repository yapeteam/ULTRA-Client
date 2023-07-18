package cn.timer.ultra.gui.ClickUI.component.impl.values;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Booleans;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class BooleanValueComponent implements Component {
    Booleans value;
    float x, y;

    public BooleanValueComponent(Booleans setting) {
        this.value = setting;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        RenderUtil.drawImage(new ResourceLocation("client/icons/clickgui/" + (value.getValue() ? "enabled" : "disabled") + ".png"), lx, y, 8, 8, ClickUIScreen.globalAlpha / 255f);
        UniFontLoaders.jigsaw14.drawString(value.getName(), lx + 12, y, setAlpha(new Color(0)).getRGB());
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
