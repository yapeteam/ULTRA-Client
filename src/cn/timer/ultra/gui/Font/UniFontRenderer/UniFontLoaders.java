/*
 * Decompiled with CFR 0_132.
 */
package cn.timer.ultra.gui.Font.UniFontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class UniFontLoaders { //only use .ttf Font (java8)
    public static UniFontRenderer arial14 = getArial(14, true);
    public static UniFontRenderer arial16 = getArial(16, true);
    public static UniFontRenderer arial18 = getArial(18, true);
    public static UniFontRenderer msFont16 = getSyyh(16, true);
    public static UniFontRenderer msFont18 = getSyyh(18, true);
    public static UniFontRenderer jello18 = getFont("jellolight.ttf", 18, true);
    public static UniFontRenderer jello14 = getFont("jellolight.ttf", 14, true);
    public static UniFontRenderer jello16 = getFont("jellolight.ttf", 16, true);
    public static UniFontRenderer icon20 = getFont("icon.ttf", 20, true);
    public static UniFontRenderer jigsaw18 = getFont("Jigsaw-Regular.ttf", 18, true);
    public static UniFontRenderer jigsaw14 = getFont("Jigsaw-Regular.ttf", 14, true);
    public static UniFontRenderer joystick18 = getArial(18, true);
    public static UniFontRenderer logo = getFont("payback.ttf", 68, true);
    public static UniFontRenderer PingFangMedium14 = getFont("PingFang/PingFang Medium.ttf", 14, true);
    public static UniFontRenderer PingFangMedium16 = getFont("PingFang/PingFang Medium.ttf", 16, true);
    public static UniFontRenderer PingFangMedium18 = getFont("PingFang/PingFang Medium.ttf", 18, true);
    public static UniFontRenderer PingFangMedium20 = getFont("PingFang/PingFang Medium.ttf", 20, true);
    public static UniFontRenderer PingFangMedium13 = getFont("PingFang/PingFang Medium.ttf", 13, true);
    public static UniFontRenderer PingFangLight18 = getFont("PingFang/PingFang Light.ttf", 18, true);
    public static UniFontRenderer PingFangLight14 = getFont("PingFang/PingFang Light.ttf", 14, true);
    public static UniFontRenderer PingFangMedium12 = getFont("PingFang/PingFang Medium.ttf", 13, true);

    public static UniFontRenderer getSyyh(int size, boolean antiAlias) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Fonts/syyh.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new UniFontRenderer(font, size, antiAlias);
    }

    public static UniFontRenderer getArial(int size, boolean antiAlias) {
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

        return new UniFontRenderer(font, size, antiAlias);
    }

    public static UniFontRenderer getFont(String name, int size, boolean antiAlias) {
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
        return new UniFontRenderer(font, size, antiAlias);
    }
}

