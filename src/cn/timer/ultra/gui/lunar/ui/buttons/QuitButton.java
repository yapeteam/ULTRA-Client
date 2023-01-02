package cn.timer.ultra.gui.lunar.ui.buttons;

import java.awt.Color;
import java.io.IOException;

import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.lunar.util.ClientGuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class QuitButton extends ImageButton {

    public QuitButton(int x, int y) {
        super("QUIT", new ResourceLocation("lunar/icons/exit.png"), x, y, () -> {
            Minecraft.getMinecraft().shutdown();
        });
    }

    @Override
    public void drawButton(int mouseX, int mouseY) {
        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (hovered) {
            if (hoverFade < 40) hoverFade += 10;
            drawHoverEffect();
            if (Mouse.isButtonDown(0) && timer.delay(200)) {
                this.action.run();
                timer.reset();
            }
        } else {
            if (hoverFade > 0) hoverFade -= 10;
        }

        ClientGuiUtils.drawRoundedRect(this.x - 1, this.y - 1, this.width + 2, this.height + 2, 2, new Color(30, 30, 30, 60));
        ClientGuiUtils.drawRoundedRect(this.x, this.y, this.width, this.height, 2, new Color(255, 255 - hoverFade * 4, 255 - hoverFade * 4, 38 + hoverFade));

        ClientGuiUtils.drawRoundedOutline(this.x, this.y, this.x + this.width, this.y + this.height, 2, 3, new Color(255, 255, 255, 30).getRGB());

        int color = new Color(232, 232, 232, 183).getRGB();
        float f1 = (color >> 24 & 0xFF) / 255.0F;
        float f2 = (color >> 16 & 0xFF) / 255.0F;
        float f3 = (color >> 8 & 0xFF) / 255.0F;
        float f4 = (color & 0xFF) / 255.0F;
        GL11.glColor4f(f2, f3, f4, f1);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(this.x + 3, this.y + 3, 0, 0, 6, 6, 6, 6);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    @Override
    protected void drawHoverEffect() {
        int w = FontUtil.TEXT.getFont().getWidth(this.text);
        ClientGuiUtils.drawRoundedRect(this.x + (this.width - w) / 2, this.y + 17, w, 7, 2, new Color(0, 0, 0, 126));
        FontUtil.TEXT_BOLD.getFont().drawCenteredTextScaled(this.text, this.x + this.width / 2, this.y + 17 - (this.height - FontUtil.TEXT_BOLD.getFont().FONT_HEIGHT) / 2, new Color(255, 255, 255, 135).getRGB(), 0.9F);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isHovered(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            this.action.run();
        }
    }

    private boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }
}
