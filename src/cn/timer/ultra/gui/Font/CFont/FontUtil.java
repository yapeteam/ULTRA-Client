package cn.timer.ultra.gui.Font.CFont;

import net.minecraft.util.*;
import java.awt.*;
import net.minecraft.client.*;

public class FontUtil
{
    public static Font getFontFromTTF(final ResourceLocation fontLocation, final float fontSize, final int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
