package cn.timer.ultra.gui.lunar.ui.buttons;

import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.lunar.util.ClientGuiUtils;
import cn.timer.ultra.utils.ultra.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class MainButton extends GuiScreen {

    protected String text;
    protected float x, y;
    protected float width, height;
    protected final Minecraft mc = Minecraft.getMinecraft();

    protected int hoverFade = 0;
    protected Runnable action;
    protected final TimerUtil timer = new TimerUtil();

    public MainButton(String text, float x, float y, Runnable action) {
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

        FontUtil.TEXT_BOLD.getFont().drawString(this.text, this.x + (this.width - FontUtil.TEXT_BOLD.getFont().getStringWidth(text)) / 2f + 0.5F, this.y + (this.height - FontUtil.TEXT_BOLD.getFont().getHeight()) / 2f + 0.5f, new Color(30, 30, 30, 50).getRGB());
        FontUtil.TEXT_BOLD.getFont().drawString(this.text, this.x + (this.width - FontUtil.TEXT_BOLD.getFont().getStringWidth(text)) / 2f, this.y + (this.height - FontUtil.TEXT_BOLD.getFont().getHeight()) / 2f, new Color(190, 195, 189).getRGB());
    }
}
