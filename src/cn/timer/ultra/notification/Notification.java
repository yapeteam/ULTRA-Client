package cn.timer.ultra.notification;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.render.GlUtils;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.RoundedUtil;
import cn.timer.ultra.utils.ultra.render.ShadowUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

import java.awt.*;

public class Notification {
    public Notification(String text, Type type) {
        this.text = text;
        this.type = type;
        this.width = UniFontLoaders.PingFangMedium13.getStringWidth(text) + 10;
        timer.reset();
    }

    private float Tx, Ty;
    private float Cx, Cy;
    private final Type type;
    private final String text;
    private int index;
    private final int width;

    public float getCurrentX() {
        return Cx;
    }

    public float getCurrentY() {
        return Cy;
    }

    public void setTargetX(float tx) {
        Tx = tx;
    }

    public void setTargetY(float ty) {
        Ty = ty;
    }

    public void setCurrentX(float cx) {
        Cx = cx;
    }

    public void setCurrentY(float cy) {
        Cy = cy;
    }

    private final AnimationUtils aniX = new AnimationUtils();
    private final AnimationUtils aniY = new AnimationUtils();
    private final TimerUtil timer = new TimerUtil();
    private float speed = 0.2f;
    private boolean back = false;

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setBack(boolean back) {
        this.back = back;
    }

    public boolean isBack() {
        return back;
    }

    public void update(int index) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (open > 0.99) {
            this.Cx = aniX.animate(this.Tx, this.Cx, this.speed);
            this.Cy = aniY.animate(this.Ty, this.Cy, this.speed);
        } else {
            this.Cx = this.Tx;
            this.Cy = this.Ty;
        }
        setTargetY(sr.getScaledHeight() - ((NotificationManager.instance.getHeight() + 5) * (index + 1) + 2));
        if (isBack()) {
            setTargetX(sr.getScaledWidth() + 2);
        }
    }

    private float open = 0;
    Framebuffer shadowFramebuffer = new Framebuffer(1, 1, false);

    public void render() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Color color, textColor = new Color(255, 255, 255);
        switch (getType()) {
            case Info:
                color = new Color(0, 187, 255, 155);
                textColor = new Color(0, 0, 0);
                break;
            case Error:
                color = new Color(255, 0, 0, 155);
                break;
            case Success:
                color = new Color(0, 255, 42, 155);
                break;
            case Warning:
                color = new Color(238, 255, 0, 155);
                textColor = new Color(0, 0, 0);
                break;
            default:
                color = new Color(0, 0, 0, 155);
        }
        float centreX = sr.getScaledWidth();
        float centreY = getCurrentY();
        open += (1 - open) / (10f * (Minecraft.getDebugFPS() / 70f));
        GlStateManager.pushMatrix();
        GlStateManager.translate(centreX, centreY, 0);
        GlStateManager.scale(open, open, open);
        GlStateManager.translate(-centreX, -centreY, 0);
        RoundedUtil.drawRound(getCurrentX(), getCurrentY(), this.width, NotificationManager.instance.getHeight(), 3, color);
        shadowFramebuffer = GlUtils.createFrameBuffer(shadowFramebuffer);
        shadowFramebuffer.framebufferClear();
        shadowFramebuffer.bindFramebuffer(true);
        RenderUtil.drawRoundedRect2(getCurrentX(), getCurrentY(), this.width, NotificationManager.instance.getHeight(), 3, color.getRGB());
        shadowFramebuffer.unbindFramebuffer();
        ShadowUtils.renderShadow(shadowFramebuffer.framebufferTexture, 8, 2, color.brighter().brighter());
        RenderUtil.drawRect2(getCurrentX() + 4, getCurrentY() + NotificationManager.instance.getHeight() - 4, timer.time() / (float) NotificationManager.instance.getTime() * (this.width - 8), 2, -1);
        UniFontLoaders.PingFangMedium18.drawString(getType().name(), getCurrentX() + 3, getCurrentY() + 2, textColor.getRGB());
        UniFontLoaders.PingFangMedium13.drawString(text, getCurrentX() + 5, getCurrentY() + 9, textColor.getRGB());
        GlStateManager.popMatrix();
    }

    public TimerUtil getTimer() {
        return timer;
    }

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getWidth() {
        return width;
    }

    public enum Type {
        Warning,
        Info,
        Success,
        Error
    }
}
