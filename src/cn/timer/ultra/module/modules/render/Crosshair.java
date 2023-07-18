package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.color.Colors;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;


public class Crosshair extends Module {
    public static Numbers<Double> Gap = new Numbers<>("Gap", 0.25, 15.0, 3.4);
    public static Numbers<Double> Size = new Numbers<>("Size", 0.25, 15.0, 7.4);
    public static Booleans Dynamic = new Booleans("Dynamic", true);
    public static Numbers<Double> Width = new Numbers<>("Width", 0.25, 15.0, 0.3);
    public static Numbers<Integer> r = new Numbers<>("red", 0, 255, 255);
    public static Numbers<Integer> g = new Numbers<>("green", 0, 255, 255);
    public static Numbers<Integer> b = new Numbers<>("blue", 0, 255, 25);

    public Crosshair() {
        super("Crosshair", Keyboard.KEY_NONE, Category.Render);
        addValues(Gap, Size, Dynamic, Width, r, g, b);
    }

    @EventTarget
    public void onGui(final EventRender2D e) {
        final int red = r.intValue();
        final int green = g.intValue();
        final int blue = b.intValue();
        final int alph = 255;
        final double gap = Gap.getValue();
        final double width = Width.getValue();
        final double size = Size.getValue();
        final ScaledResolution scaledRes = new ScaledResolution(Crosshair.mc);
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 - gap - size - (isMoving() ? 2 : 0), scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 - gap - (isMoving() ? 2 : 0), 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - width, scaledRes.getScaledHeight() / 2 + gap + 1.0 + (isMoving() ? 2 : 0) - 0.15, scaledRes.getScaledWidth() / 2 + 1.0f + width, scaledRes.getScaledHeight() / 2 + 1 + gap + size + (isMoving() ? 2 : 0) - 0.15, 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 - gap - size - (isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 - gap - (isMoving() ? 2 : 0) + 0.15, scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(scaledRes.getScaledWidth() / 2 + 1 + gap + (isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 - width, scaledRes.getScaledWidth() / 2 + size + gap + 1.0 + (isMoving() ? 2 : 0), scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5, Colors.getColor(red, green, blue, alph), new Color(0, 0, 0, alph).getRGB());
    }

    public boolean isMoving() {
        if (Dynamic.getValue()) {
            if (!mc.thePlayer.isCollidedHorizontally) {
                if (!mc.thePlayer.isSneaking()) {
                    if (mc.thePlayer.movementInput.moveForward == 0.0f) {
                        return mc.thePlayer.movementInput.moveStrafe != 0.0f;
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
