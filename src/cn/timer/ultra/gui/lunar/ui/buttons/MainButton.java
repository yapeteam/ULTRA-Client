package cn.timer.ultra.gui.lunar.ui.buttons;

import java.awt.Color;

import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.lunar.util.ClientGuiUtils;
import cn.timer.ultra.utils.TimerUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class MainButton extends GuiScreen {

    protected String text;
    protected int x, y;
    protected int width, height;

    protected int hoverFade = 0;
    protected Runnable action;
    protected final TimerUtil timer = new TimerUtil();

    public MainButton(String text, int x, int y, Runnable action) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = 132;
        this.height = 11;
        this.action = action;
    }

    public void drawButton(int mouseX, int mouseY) {
        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (hovered) {
            if (hoverFade < 40) hoverFade += 10;
            if (Mouse.isButtonDown(0) && timer.delay(200)) {
                this.action.run();
                timer.reset();
            }
        } else {
            if (hoverFade > 0) hoverFade -= 10;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F);
        ClientGuiUtils.drawRoundedRect(this.x - 1, this.y - 1, this.width + 2, this.height + 2, 2, new Color(30, 30, 30, 60));
        ClientGuiUtils.drawRoundedRect(this.x, this.y, this.width, this.height, 2, new Color(255, 255, 255, 38 + hoverFade));

        ClientGuiUtils.drawRoundedOutline(this.x, this.y, this.x + this.width, this.y + this.height, 2, 3, new Color(255, 255, 255, 30).getRGB());

        FontUtil.TEXT_BOLD.getFont().drawCenteredString(this.text, this.x + this.width / 2f + 0.5F, this.y + (this.height - FontUtil.TEXT_BOLD.getFont().FONT_HEIGHT) / 2f + 0.5F - 1f, new Color(30, 30, 30, 50).getRGB());
        FontUtil.TEXT_BOLD.getFont().drawCenteredString(this.text, this.x + this.width / 2f, this.y + (this.height - FontUtil.TEXT_BOLD.getFont().FONT_HEIGHT) / 2f - 1f, new Color(190, 195, 189).getRGB());
    }

}
