package cn.timer.ultra.gui.cloudmusic.ui;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontRenderer;
import cn.timer.ultra.gui.cloudmusic.MusicManager;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MusicOverlayRenderer {
    public static MusicOverlayRenderer INSTANCE = new MusicOverlayRenderer();

    public long readSecs = 0;
    public long totalSecs = 0;

    public float animation = 0;

    public TimerUtil timer = new TimerUtil();

    public final float[] VisWidth = new float[100];

    public void renderOverlay(float x, float y) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            readSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds();
            totalSecs = (int) MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds();
        }

        if (MusicManager.INSTANCE.getCurrentTrack() != null && MusicManager.INSTANCE.getMediaPlayer() != null) {
            UniFontLoaders.PingFangMedium18.drawString(MusicManager.INSTANCE.getCurrentTrack().name + " - " + MusicManager.INSTANCE.getCurrentTrack().artists, x + 32, y + 5, Color.WHITE.getRGB());
            UniFontLoaders.PingFangMedium18.drawString(formatSeconds((int) readSecs) + "/" + formatSeconds((int) totalSecs), x + 32, y + 15, 0xffffffff);

            if (MusicManager.INSTANCE.circleLocations.containsKey(MusicManager.INSTANCE.getCurrentTrack().id)) {
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                ResourceLocation icon = MusicManager.INSTANCE.circleLocations.get(MusicManager.INSTANCE.getCurrentTrack().id);
                RenderUtil.drawImage(icon, x, y + 1, 28, 28);
                GL11.glPopMatrix();
            } else {
                MusicManager.INSTANCE.getCircle(MusicManager.INSTANCE.getCurrentTrack());
            }
            try {
                float currentProgress = (float) (MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / Math.max(1, MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds())) * 100;
                RenderUtil.drawArc(x + 14, y + 14, 14, Color.WHITE.getRGB(), 0, 360, 4);
                RenderUtil.drawArc(x + 14, y + 14, 14, Color.BLUE.getRGB(), 180, 180 + (currentProgress * 3.6f), 4, true);
            } catch (Exception ignored) {
            }
        }

        if (MusicManager.INSTANCE.lyric) {
            UniFontRenderer font = UniFontLoaders.PingFangMedium18;
            //Lyric
            int col = new Color(255, 255, 255).getRGB();

            GlStateManager.disableBlend();
            String s = MusicManager.INSTANCE.lrcCur.contains("_EMPTY_") ? "等待中......." : MusicManager.INSTANCE.lrcCur;
            font.drawString(s, (sr.getScaledWidth() - font.getStringWidth(s)) / 2f, sr.getScaledHeight() - 40 - 10, col, true);
            s = MusicManager.INSTANCE.tlrcCur.contains("_EMPTY_") ? "Waiting......." : MusicManager.INSTANCE.tlrcCur;
            font.drawString(s, (sr.getScaledWidth() - font.getStringWidth(s)) / 2f, sr.getScaledHeight() - 30 - 10, col, true);

            GlStateManager.enableBlend();
        }

        if (MusicManager.INSTANCE.visualize) {
            if (MusicManager.INSTANCE.magnitudes != null) {
                float width = sr.getScaledWidth() / 100f;
                RenderUtil.drawRect2(0, 0, 0, 0, 0);
                for (int i = 0; i < 100; i += 1) {
                    if (Float.isNaN(VisWidth[i]) || VisWidth[i] - MusicManager.INSTANCE.magnitudes[i] > 20 || MusicManager.INSTANCE.magnitudes[i] - VisWidth[i] > 20) {
                        VisWidth[i] = MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING ? MusicManager.INSTANCE.magnitudes[i] : VisWidth[i];
                    }
                    RenderUtil.drawRect2(i * width, sr.getScaledHeight(), width, -VisWidth[i], new Color(0x1FFFFFFF, true).getRGB());
                    if (MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
                        VisWidth[i] += (MusicManager.INSTANCE.magnitudes[i] - VisWidth[i]) / (1.7 * (Minecraft.getDebugFPS() / 60f));
                    else VisWidth[i] -= VisWidth[i] / 10;
                }
            }
        }

        GlStateManager.color(1, 1, 1, 1);
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

    public String[] getDisplay() {
        if (MusicManager.INSTANCE.getCurrentTrack() == null)
            return null;
        return new String[]{
                MusicManager.INSTANCE.getCurrentTrack().name + " - " + MusicManager.INSTANCE.getCurrentTrack().artists,
                formatSeconds((int) readSecs) + "/" + formatSeconds((int) totalSecs)
        };
    }
}
