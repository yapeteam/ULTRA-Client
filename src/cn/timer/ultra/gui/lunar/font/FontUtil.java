package cn.timer.ultra.gui.lunar.font;

import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.gui.Font.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public enum FontUtil {

    TITLE(getFontRender("raleway-extrabold.ttf", 30, true)),
    TEXT(getFontRender("roboto-regular.ttf", 11, true)),
    TEXT_BOLD(getFontRender("roboto-black.ttf", 9, true)),
    LOGO(getFontRender("payback.ttf", 78, true));

    private final FontRenderer font;

    FontUtil(FontRenderer font) {
        this.font = font;
    }

    public FontRenderer getFont() {
        return font;
    }

    public static FontRenderer getFontRender(String name, int size, boolean antiAlias) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("lunar/font/" + name)).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new FontRenderer(font, size, antiAlias);
    }
}
