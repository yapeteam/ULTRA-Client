package cn.timer.ultra.utils.soar;

import cn.timer.ultra.utils.soar.animation.SimpleAnimation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ClickEffect {

    private final float x;
    private final float y;

    private final SimpleAnimation animation = new SimpleAnimation(0.0F);

    public ClickEffect(float x, float y) {
        this.x = x;
        this.y = y;
        animation.setValue(0);
    }

    public void draw() {
        animation.setAnimation(100, 12);
        double radius = 8 * animation.getValue() / 100;
        int alpha = (int) (255 - 255 * animation.getValue() / 100);
        int color = new Color(255, 255, 255, alpha).getRGB();
        drawArc(x, y, radius, color, 0, 360, 5);
    }

    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
        r *= 2.0D;
        x1 *= 2;
        y1 *= 2;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glLineWidth(linewidth);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = startPoint; i <= arc; i += 1) {
            double x = Math.sin(i * Math.PI / 180.0D) * r;
            double y = Math.cos(i * Math.PI / 180.0D) * r;
            GL11.glVertex2d(x1 + x, y1 + y);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public boolean canRemove() {
        return animation.getValue() > 99;
    }
}
