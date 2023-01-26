package cn.timer.ultra.gui.lunar.ui.buttons;

import cn.timer.ultra.gui.lunar.font.FontUtil;
import cn.timer.ultra.gui.lunar.util.ClientGuiUtils;
import cn.timer.ultra.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CustomImageButton extends MainButton {

    protected ResourceLocation image;

    public CustomImageButton(String text, ResourceLocation image, int x, int y, int width, int height, Runnable action) {
        super(text, x, y, action);
        this.width = width;
        this.height = height;
        this.image = image;
    }

    @Override
    public void drawButton(int mouseX, int mouseY) {
        boolean hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (hovered) {
            RenderUtil.drawShadow(x, y, width, height);
            drawHoverEffect();
            if (Mouse.isButtonDown(0) && timer.delay(200)) {
                this.action.run();
                timer.reset();
            }
        }

        int color = new Color(232, 232, 232, 255).getRGB();
        float f1 = (color >> 24 & 0xFF) / 255.0F;
        float f2 = (color >> 16 & 0xFF) / 255.0F;
        float f3 = (color >> 8 & 0xFF) / 255.0F;
        float f4 = (color & 0xFF) / 255.0F;
        GL11.glColor4f(f2, f3, f4, f1);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        drawModalRectWithCustomSizedTexture(this.x + 3, this.y + 3, 0, 0, width, height, width, height);

        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
    }

    protected void drawHoverEffect() {
        int w = (int) (FontUtil.TEXT.getFont().getWidth(this.text) * 0.9F);
        ClientGuiUtils.drawRoundedRect(this.x + (this.width - w) / 2, this.y - 12, w, 7, 2, new Color(0, 0, 0, 126));
        FontUtil.TEXT_BOLD.getFont().drawCenteredTextScaled(this.text, this.x + this.width / 2, this.y - 12 - (this.height - FontUtil.TEXT_BOLD.getFont().FONT_HEIGHT) / 2, new Color(255, 255, 255, 135).getRGB(), 0.9F);
    }
}
