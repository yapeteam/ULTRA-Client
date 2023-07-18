package cn.timer.ultra.gui.lunar.ui.buttons;

import cn.timer.ultra.alt.login.LoginScreen;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.lunar.util.ClientGuiUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class LoginButton extends MainButton {
    public LoginButton(String text, int x, int y, Runnable action) {
        super(text, x, y, action);
        this.width = 20;
        this.height = 20;
    }

    private float addWidth = 0;

    @Override
    public void drawButton(int mouseX, int mouseY) {
        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        float target;
        if (hovered) {
            if (hoverFade < 40) hoverFade += 10;
            drawHoverEffect();
            if (Mouse.isButtonDown(0) && timer.delay(200)) {
                this.action.run();
                timer.reset();
            }
            target = 50;
        } else {
            if (hoverFade > 0) hoverFade -= 10;
            target = 0;
        }
        addWidth += (target - addWidth) / 10;

        ClientGuiUtils.drawRoundedRect(this.x - 1, this.y - 1, (int) (this.width + 2 + addWidth), this.height + 2, 2, new Color(30, 30, 30, 60));
        ClientGuiUtils.drawRoundedRect(this.x, this.y, (int) (this.width + addWidth), this.height, 2, new Color(255, 255, 255, 38 + hoverFade));
        ClientGuiUtils.drawRoundedOutline(this.x, this.y, (int) (this.x + this.width + addWidth), this.y + this.height, 2, 3, new Color(255, 255, 255, 30).getRGB());
        {
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/entity/steve.png"));
            Gui.drawModalRectWithCustomSizedTexture(this.x + 2, this.y + 2, this.width - 4, this.height - 4, this.width - 4, this.height - 4, (float) (8 * (this.width - 4)), (float) (8 * (this.height - 4)));
        }
        /*
        if (DevLogin.getLoginThread() != null) {
            LoginScreen.render();
            LoginScreen.update();
        }*/

        LoginScreen.render();
        LoginScreen.update();

        super.drawButton(mouseX, mouseY);
    }

    protected void drawHoverEffect() {
        int w = (int) (FontUtil.TEXT.getFont().getStringWidth(this.text) * 0.9F);
        ClientGuiUtils.drawRoundedRect(this.x + (this.width - w) / 2, this.y - 12, w, 7, 2, new Color(0, 0, 0, 126));
        UniFontLoaders.PingFangMedium14.drawString(mc.getSession().getUsername(), this.x + 20 + (addWidth - UniFontLoaders.PingFangMedium14.getStringWidth(mc.getSession().getUsername())) / 2, this.y + 5, -1);
    }
}
