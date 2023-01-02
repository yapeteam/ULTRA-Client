package cn.timer.ultra.gui.cloudmusic.ui;

import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.gui.Font.FontRenderer;
import cn.timer.ultra.gui.cloudmusic.MusicManager;
import cn.timer.ultra.utils.ColorUtils;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public enum MusicOverlayRenderer {
    INSTANCE;

    public String downloadProgress = "0";

    public long readedSecs = 0;
    public long totalSecs = 0;

    public float animation = 0;

    public TimerUtil timer = new TimerUtil();

    public boolean firstTime = true;


    public void renderOverlay() {
        int addonX = 10;
        int addonY = 60;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            readedSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds();
            totalSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds();
        }

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            FontLoaders.joystick18.drawString(MusicManager.INSTANCE.getCurrentTrack().name + " - " + MusicManager.INSTANCE.getCurrentTrack().artists, 36f + addonX, 10 + addonY, Color.WHITE.getRGB());
            FontLoaders.joystick18.drawString(formatSeconds((int) readedSecs) + "/" + formatSeconds((int) totalSecs), 36f + addonX, 20f + addonY, 0xffffffff);

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                ResourceLocation icon = MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
                RenderUtil.drawImage(icon, 4 + addonX, 6 + addonY, 28, 28);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }

            try {
                float currentProgress = (float) (MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / Math.max(1, MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds())) * 100;
                RenderUtil.drawArc(18 + addonX, 19 + addonY, 14, Color.WHITE.getRGB(), 0, 360, 4);
                RenderUtil.drawArc(18 + addonX, 19 + addonY, 14, Color.BLUE.getRGB(), 180, 180 + (currentProgress * 3.6f), 4, true);
            } catch (Exception ignored) {
            }
        }

        if (MusicManager.INSTANCE.lyric) {
            {
                FontRenderer font = FontLoaders.arial18;
                //int addonYlyr = 50;
                //Lyric
                int col = new Color(255, 255, 255).getRGB();
                GlStateManager.disableBlend();
                //lyricFont.drawCenteredString(MusicManager.INSTANCE.lrcCur.contains("_EMPTY_") ? "等待中......." : MusicManager.INSTANCE.lrcCur, (int) (sr.getScaledWidth() / 2f - 0.5f), sr.getScaledHeight() - 140 - 80 + addonYlyr, col);
                //lyricFont.drawCenteredString(MusicManager.INSTANCE.tlrcCur.contains("_EMPTY_") ? "Waiting......." : MusicManager.INSTANCE.tlrcCur, (int) (sr.getScaledWidth() / 2f), (int) (sr.getScaledHeight() - 125 + 0.5f - 80 + addonYlyr), col);
                String s = MusicManager.INSTANCE.lrcCur.contains("_EMPTY_") ? "等待中......." : MusicManager.INSTANCE.lrcCur;
                System.out.println(MusicManager.INSTANCE.lrcCur);
                font.drawString(s, (sr.getScaledWidth() - font.getStringWidth(s)) / 2f, sr.getScaledHeight() - 40 - 10, col, true);
                s = MusicManager.INSTANCE.tlrcCur.contains("_EMPTY_") ? "Waiting......." : MusicManager.INSTANCE.tlrcCur;
                font.drawString(s, (sr.getScaledWidth() - font.getStringWidth(s)) / 2f, sr.getScaledHeight() - 30 - 10, col, true);
                GlStateManager.enableBlend();
            }
        }

        if ((MusicManager.showMsg)) {
            if (firstTime) {
                timer.reset();
                firstTime = false;
            }

            FontRenderer wqy = FontLoaders.msFont18;
            FontRenderer sans = FontLoaders.msFont18;

            float width1 = wqy.getStringWidth(MusicManager.INSTANCE.getCurrentTrack().name);
            float width2 = sans.getStringWidth("Now playing");
            float allWidth = (Math.max(Math.max(width1, width2), 150));

            RenderUtil.drawRect(sr.getScaledWidth() - animation, 5, sr.getScaledWidth(), 40, ColorUtils.reAlpha(Color.BLACK.getRGB(), 255 * 0.7f));

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                ResourceLocation icon = MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
                RenderUtil.drawImage(icon, sr.getScaledWidth() - animation + 5, 8, 28, 28);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }

            RenderUtil.drawArc(sr.getScaledWidth() - animation - 31 + 50, 22, 14, Color.WHITE.getRGB(), 0, 360, 2);
            sans.drawString("Now playing", (int) (sr.getScaledWidth() - animation - 12 + 50), 8, Color.WHITE.getRGB());
            wqy.drawString(MusicManager.INSTANCE.getCurrentTrack().name, (int) (sr.getScaledWidth() - animation - 12 + 50), 26, Color.WHITE.getRGB());

            if (timer.delay(5000)) {
                this.animation = (float) getAnimationStateSmooth(0, animation, 10.0f / Minecraft.getDebugFPS());
                if (this.animation <= 0) {
                    MusicManager.showMsg = false;
                    firstTime = true;
                }
            } else {
                this.animation = (float) getAnimationStateSmooth(allWidth, animation, 10.0f / Minecraft.getDebugFPS());
            }

        }

        GlStateManager.resetColor();
    }

    private static double getAnimationStateSmooth(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }
        if (target == current) {
            return target;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            if (current + factor > target) {
                current = target;
            } else {
                current += factor;
            }
        } else {
            if (current - factor < target) {
                current = target;
            } else {
                current -= factor;
            }
        }
        return current;
    }

    public String formatSeconds(int seconds) {
        String rstl = "";
        int mins = seconds / 60;
        if (mins < 10) {
            rstl += "0";
        }
        rstl += mins + ":";
        seconds %= 60;
        if (seconds < 10) {
            rstl += "0";
        }
        rstl += seconds;
        return rstl;
    }
}
