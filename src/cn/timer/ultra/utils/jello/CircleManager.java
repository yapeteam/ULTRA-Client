package cn.timer.ultra.utils.jello;

import cn.timer.ultra.utils.ultra.render.RenderingUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircleManager {
    public final List<Circle> circles = new ArrayList<>();

    public void addCircle(double x, double y, double rad, double speed, int key, KEY_TYPE type) {
        circles.add(new Circle(x, y, rad, speed, key, type));
    }

    public void runCircle(Circle c) {
        c.lastProgress = c.progress;
        if (c.getType() == KEY_TYPE.KeyBoard && Keyboard.isKeyDown(c.keyCode) && c.progress > c.topRadius * 0.67)
            return;
        if (c.getType() == KEY_TYPE.Mouse && Mouse.isButtonDown(c.keyCode + 100) && c.progress > c.topRadius * 0.89)
            return;
        c.progress += (c.topRadius - c.progress) / (c.speed) + 0.01;
        if (c.progress >= c.topRadius) {
            c.complete = true;
        }
    }

    public void runCircles() {
        List<Circle> completes = new ArrayList<>();
        for (Circle c : circles) {
            if (!c.complete) {
                runCircle(c);
            } else {
                completes.add(c);
            }
        }
        synchronized (circles) {
            circles.removeAll(completes);
        }
    }

    public void drawCircles() {
        for (Circle c : circles) {
            if (!c.complete)
                drawCircle(c);
        }
    }

    public void drawCircle(Circle c) {
        float progress = (float) (c.progress * Minecraft.getMinecraft().timer.renderPartialTicks + (c.lastProgress * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
        if (!c.complete)
            RenderingUtil.drawBorderedCircle((int) c.x, (int) c.y, progress, new Color(1f, 1f, 1f, (1 - Math.min(1f, Math.max(0f, (float) (progress / c.topRadius)))) / 2).getRGB(), new Color(1f, 1f, 1f, (1 - Math.min(1f, Math.max(0f, (float) (progress / c.topRadius)))) / 2).getRGB());
    }

    public enum KEY_TYPE {
        KeyBoard,
        Mouse
    }
}

