/*
 * Decompiled with CFR 0_132.
 */
package cn.timer.ultra.gui.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontLoaders {
    public static FontRenderer arial14 = getArial(14, true);
    public static FontRenderer arial16 = getArial(16, true);
    public static FontRenderer arial18 = getArial(18, true);
    public static FontRenderer arial22 = getArial(22, true);
    public static FontRenderer arial24 = getArial(24, true);
    public static FontRenderer msFont18 = getSyyh(18, true);
    public static FontRenderer msFont36 = getSyyh(36, true);
    public static FontRenderer msFont72 = getSyyh(72, true);
    public static FontRenderer jello18 = getFont("jellolight.ttf", 18, true);
    public static FontRenderer jello14 = getFont("jellolight.ttf", 14, true);
    public static FontRenderer jello16 = getFont("jellolight.ttf", 16, true);
    public static FontRenderer jello25 = getFont("jellolight.ttf", 25, true);
    public static FontRenderer jello30 = getFont("jellolight.ttf", 30, true);
    public static FontRenderer jelloB18 = getFont("jellomedium.ttf", 18, true);
    public static FontRenderer jelloC18 = getFont("jelloregular.ttf", 18, true);
    public static FontRenderer icon20 = getFont("icon.ttf", 20, true);
    public static FontRenderer jigsaw18 = getFont("Jigsaw-Regular.ttf", 18, true);
    public static FontRenderer jigsaw14 = getFont("Jigsaw-Regular.ttf", 14, true);
    public static FontRenderer joystick18 = getArial(18, true);
    public static FontRenderer logo = getFont("payback.ttf", 68, true);

    public static FontRenderer getSyyh(int size, boolean antiAlias) { //only use .ttf Font (java8)
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Fonts/syyh.otf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new FontRenderer(font, size, antiAlias);
    }

    public static FontRenderer getArial(int size, boolean antiAlias) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Fonts/Arial.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }

        return new FontRenderer(font, size, antiAlias);
    }

    public static FontRenderer getFont(String name, int size, boolean antiAlias) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Fonts/" + name)).getInputStream();
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

