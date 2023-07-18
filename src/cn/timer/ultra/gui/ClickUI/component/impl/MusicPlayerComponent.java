package cn.timer.ultra.gui.ClickUI.component.impl;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.ClickUI.component.impl.slots.TrackSlotComponent;
import cn.timer.ultra.gui.Font.CFontRenderer.CFontLoaders;
import cn.timer.ultra.gui.Font.CFontRenderer.CFontRenderer;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontRenderer;
import cn.timer.ultra.gui.cloudmusic.MusicManager;
import cn.timer.ultra.gui.cloudmusic.api.CloudMusicAPI;
import cn.timer.ultra.gui.cloudmusic.impl.Track;
import cn.timer.ultra.gui.cloudmusic.ui.QRLoginScreen;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.render.GradientUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.RoundedUtil;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class MusicPlayerComponent implements Component {
    private final CFontRenderer icon = CFontLoaders.icon30;
    public boolean showList = false;
    private final CopyOnWriteArrayList<TrackSlotComponent> SubComponent = new CopyOnWriteArrayList<>();

    public boolean isHoveredIn(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private float x, y;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final CustomTextField textField = new CustomTextField("", 0, 0);
    private search searchMode = search.name;
    float scrollY = 0, AniScrollY = 0;
    private final float[] VisWidth = new float[100];
    private final AnimationUtils animation = new AnimationUtils();
    private Color globalBlack = new Color(0);
    private Color globalWhite = new Color(0);

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float wheel = Mouse.getDWheel();
        if (wheel > 0) scrollY += 10;
        if (wheel < 0) scrollY -= 10;
        if ((SubComponent.size() + 1) * 23 + scrollY + ClickUIScreen.topHeight < ClickUIScreen.height - 50 - 20 - 25 + ClickUIScreen.topHeight + 22)
            scrollY = ClickUIScreen.height - 50 - 20 - 25 + 22 - (SubComponent.size() + 1) * 23;
        if (ClickUIScreen.topHeight + 22 + scrollY > ClickUIScreen.topHeight + 22) scrollY = 0;
        AniScrollY = animation.animate(scrollY, AniScrollY, 0.2f);
        RoundedUtil.drawGradientHorizontal(x, y + (ClickUIScreen.height - 50), ClickUIScreen.width - ClickUIScreen.leftWidth - 10, 45, 5, setAlpha(Client.instance.getClientColor()), setAlpha(Client.instance.getAlternateClientColor()));
        if (MusicManager.INSTANCE.getMediaPlayer() != null) { // play/pause
            icon.drawString(MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING ? "K" : "J", x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth(MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING ? "K" : "J")) / 2f, y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB());
        } else
            icon.drawString("J", x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth("J")) / 2f, y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB());
        icon.drawString("L", x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth("J")) / 2f - 5 - icon.getStringWidth("L"), y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB()); // prev
        icon.drawString("M", x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth("J")) / 2f + 5 + icon.getStringWidth("M"), y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB()); // next
        icon.drawString("Q", x + ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - 5 - icon.getStringWidth("Q"), y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB()); // list
        icon.drawString("P", x + 5, y + 5 + (ClickUIScreen.height - 50), MusicManager.INSTANCE.lyric ? globalBlack.getRGB() : globalWhite.getRGB()); // 词
        icon.drawString(MusicManager.INSTANCE.repeat ? "O" : "N", x + ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - 5 - icon.getStringWidth("Q") - 2 - icon.getStringWidth(MusicManager.INSTANCE.repeat ? "O" : "P"), y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB()); // repeat
        icon.drawString("R", x + 5 + 2 + icon.getStringWidth("P"), y + 5 + (ClickUIScreen.height - 50), globalBlack.getRGB()); // QRCode
        //Cover
        if (!showList) {
            if (MusicManager.INSTANCE.magnitudes != null) {
                for (int i = 0; i < 100; i++) {
                    if (Float.isNaN(VisWidth[i]) || VisWidth[i] - MusicManager.INSTANCE.magnitudes[i] > 20 || MusicManager.INSTANCE.magnitudes[i] - VisWidth[i] > 20) {
                        VisWidth[i] = MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING ? MusicManager.INSTANCE.magnitudes[i] : VisWidth[i];
                    }
                    RenderUtil.drawRect2(x, y + 20 + 5 * i, VisWidth[i], 3, 0x80000000);
                    if (MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING)
                        VisWidth[i] += (MusicManager.INSTANCE.magnitudes[i] - VisWidth[i]) / (1.7 * (Minecraft.getDebugFPS() / 60f));
                    else VisWidth[i] += -VisWidth[i] / 10;
                    if (y + 20 + 5 * i > ClickUIScreen.y + ClickUIScreen.height) break;
                }
            }
            String songName = MusicManager.INSTANCE.currentTrack == null ? "当前未在播放" : MusicManager.INSTANCE.currentTrack.name;
            String songArtist = MusicManager.INSTANCE.currentTrack == null ? "N/A" : MusicManager.INSTANCE.currentTrack.artists;
            GradientUtil.applyGradientHorizontal(x + ((ClickUIScreen.width - ClickUIScreen.leftWidth - UniFontLoaders.arial18.getStringWidth(songName + " | " + songArtist)) / 2f), y + (ClickUIScreen.height - 100) / 2f - 50 + 100 + 5, UniFontLoaders.arial18.getStringWidth(songName + " | " + songArtist), UniFontLoaders.arial18.getHeight(), ClickUIScreen.globalAlpha / 255f, Client.instance.getClientColor(), Client.instance.getAlternateClientColor(), () ->
                    UniFontLoaders.arial18.drawString(songName + " | " + songArtist, x + ((ClickUIScreen.width - ClickUIScreen.leftWidth - UniFontLoaders.arial18.getStringWidth(songName + " | " + songArtist)) / 2f), y + (ClickUIScreen.height - 100) / 2f - 50 + 100 + 5, globalBlack.getBlue())
            );

            if (MusicManager.INSTANCE.currentTrack != null) {
                if (MusicManager.INSTANCE.getArt(MusicManager.INSTANCE.currentTrack.id) != null) {
                    GL11.glPushMatrix();
                    RenderUtil.drawImage(MusicManager.INSTANCE.getArt(MusicManager.INSTANCE.currentTrack.id), x + ((ClickUIScreen.width - ClickUIScreen.leftWidth - 100) / 2f), y + (ClickUIScreen.height - 100) / 2f - 50, 100, 100, ClickUIScreen.globalAlpha / 255f);
                    GL11.glPopMatrix();
                }
            }
        }
        UniFontRenderer font = UniFontLoaders.msFont18;
        //Lyric
        if (!showList) {
            GlStateManager.disableBlend();
            String s = MusicManager.INSTANCE.lrcCur.contains("_EMPTY_") ? "等待中......." : MusicManager.INSTANCE.lrcCur;
            font.drawString(s, x + (ClickUIScreen.width - ClickUIScreen.leftWidth - font.getStringWidth(s)) / 2f, y + (ClickUIScreen.height - 100) / 2f + 50 + 20, globalBlack.getRGB());
            s = MusicManager.INSTANCE.tlrcCur.contains("_EMPTY_") ? "Waiting......." : MusicManager.INSTANCE.tlrcCur;
            font.drawString(s, x + (ClickUIScreen.width - ClickUIScreen.leftWidth - font.getStringWidth(s)) / 2f, y + (ClickUIScreen.height - 100) / 2f + 50 + 20 + 2 + font.getHeight(), globalBlack.getRGB());
            GlStateManager.enableBlend();
        }
        //Search
        if (showList) {
            textField.setWidth(ClickUIScreen.width - ClickUIScreen.leftWidth - 15);
            textField.setHeight(20);
            textField.draw(x + 5, y + 20, ClickUIScreen.globalAlpha);
            RenderUtil.drawRoundedRect2(x + ClickUIScreen.width - ClickUIScreen.leftWidth - 37, y + 20 + 3 + 1.5f, 26, 14, 2, isHoveredIn(x + ClickUIScreen.width - ClickUIScreen.leftWidth - 37, y + 20 + 3 + 1.5f, 26, 14, mouseX, mouseY) ? setAlpha(new Color(206, 206, 206)).getRGB() : setAlpha(new Color(206, 206, 206)).brighter().getRGB());
            UniFontLoaders.arial14.drawString("Search", x + ClickUIScreen.width - ClickUIScreen.leftWidth - 37 + 2, y + 20 + 3 + 1.5f + 3, globalBlack.getRGB());
            if (textField.textString.isEmpty()) {
                String text = "Song name";
                switch (searchMode) {
                    case name:
                        text = "Song name";
                        break;
                    case song:
                        text = "Song ID";
                        break;
                    case list:
                        text = "PlayList ID";
                }
                UniFontLoaders.arial18.drawString(text, x + 10, y + 20 + 3 + 5, setAlpha(Color.GRAY).getRGB());
            }
        }
        //processBar
        float progress = 0;
        if (MusicManager.INSTANCE.getMediaPlayer() != null) {
            progress = (float) MusicManager.INSTANCE.getMediaPlayer().getCurrentTime().toSeconds() / (float) MusicManager.INSTANCE.getMediaPlayer().getStopTime().toSeconds();
        }
        RoundedUtil.drawRound(x + 5, y + ClickUIScreen.height - 20, ClickUIScreen.width - ClickUIScreen.leftWidth - 20, 4, 1.4f, new Color(0, 0, 0, 137 * (ClickUIScreen.globalAlpha / 255)));

        if (MusicManager.INSTANCE.loadingThread != null) {
            RoundedUtil.drawRound(x + 5, y + ClickUIScreen.height - 20, (MusicManager.INSTANCE.downloadProgress / 100 * (ClickUIScreen.width - ClickUIScreen.leftWidth - 20)), 4, 1.4f, globalWhite);
            RenderUtil.circle(x + 5 + (MusicManager.INSTANCE.downloadProgress / 100 * (ClickUIScreen.width - ClickUIScreen.leftWidth - 20)), y + ClickUIScreen.height - 18, 3, new Color(255, 255, 255, ClickUIScreen.globalAlpha).getRGB());
            RenderUtil.circle(x + 5 + (MusicManager.INSTANCE.downloadProgress / 100 * (ClickUIScreen.width - ClickUIScreen.leftWidth - 20)), y + ClickUIScreen.height - 18, 2, new Color(255, 50, 50, ClickUIScreen.globalAlpha).getRGB());
        } else {
            RoundedUtil.drawRound(x + 5, y + ClickUIScreen.height - 20, progress * (ClickUIScreen.width - ClickUIScreen.leftWidth - 20), 4, 1.4f, globalWhite);
            RenderUtil.circle(x + progress * (ClickUIScreen.width - ClickUIScreen.leftWidth - 20) + 5, y + ClickUIScreen.height - 18, 3, new Color(255, 255, 255, ClickUIScreen.globalAlpha).getRGB());
            RenderUtil.circle(x + progress * (ClickUIScreen.width - ClickUIScreen.leftWidth - 20) + 5, y + ClickUIScreen.height - 18, 2, new Color(50, 176, 255, ClickUIScreen.globalAlpha).getRGB());
        }
        globalBlack = new Color(0, 0, 0, ClickUIScreen.globalAlpha);
        globalWhite = new Color(255, 255, 255, ClickUIScreen.globalAlpha);
    }

    int count = 0;

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        if (mouseButton == 0) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
            if (isHoveredIn(x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth("J")) / 2f, y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth("J"), icon.getHeight(), mouseX, mouseY)) { // play/pause
                if (!MusicManager.INSTANCE.playlist.isEmpty()) {
                    if (MusicManager.INSTANCE.currentTrack == null) {
                        try {
                            MusicManager.INSTANCE.play(MusicManager.INSTANCE.playlist.get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (MusicManager.INSTANCE.getMediaPlayer() != null) {
                            if (MusicManager.INSTANCE.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                                MusicManager.INSTANCE.getMediaPlayer().pause();
                            } else {
                                MusicManager.INSTANCE.getMediaPlayer().play();
                            }
                        }
                    }
                }
            }
            if (isHoveredIn(x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth("J")) / 2f - 5 - icon.getStringWidth("L"), y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth("L"), icon.getHeight(), mouseX, mouseY)) { //prev
                MusicManager.INSTANCE.prev();
            }
            if (isHoveredIn(x + (ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - icon.getStringWidth("J")) / 2f + 5 + icon.getStringWidth("M"), y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth("M"), icon.getHeight(), mouseX, mouseY)) { // next
                MusicManager.INSTANCE.next();
            }
            if (isHoveredIn(x + 5, y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth("P"), icon.getHeight(), mouseX, mouseY)) { // 词
                MusicManager.INSTANCE.lyric = !MusicManager.INSTANCE.lyric;
            }
            if (isHoveredIn(x + ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - 5 - icon.getStringWidth("Q") - 2 - icon.getStringWidth(MusicManager.INSTANCE.repeat ? "O" : "P"), y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth(MusicManager.INSTANCE.repeat ? "O" : "P"), icon.getHeight(), mouseX, mouseY)) { // repeat
                MusicManager.INSTANCE.repeat = !MusicManager.INSTANCE.repeat;
            }
            if (isHoveredIn(x + ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 - 5 - icon.getStringWidth("Q"), y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth("Q"), icon.getHeight(), mouseX, mouseY)) { // list
                showList = !showList;
            }
            if (isHoveredIn(x + 5 + 2 + icon.getStringWidth("P"), y + 5 + (ClickUIScreen.height - 50), icon.getStringWidth("R"), icon.getHeight(), mouseX, mouseY)) { // QRCode
                mc.displayGuiScreen(new QRLoginScreen(Client.instance.clickGui));
            }
            if (showList && isHoveredIn(x + ClickUIScreen.width - ClickUIScreen.leftWidth - 37, y + 20 + 3 + 1.5f, 26, 14, mouseX, mouseY) && !this.textField.textString.isEmpty()) { // Search
                MusicManager.INSTANCE.analyzeThread = new Thread(() -> {
                    try {
                        scrollY = 0;
                        this.SubComponent.clear();
                        switch (searchMode) {
                            case song:
                                ArrayList<Object[]> requestSo = CloudMusicAPI.INSTANCE.requestSong(CloudMusicAPI.INSTANCE.getSongJson(Long.parseLong(this.textField.textString)));
                                ArrayList<Track> listSo = new ArrayList<>();
                                for (Object[] strings : requestSo) {
                                    listSo.add(new Track(Long.parseLong(strings[1].toString()), strings[0].toString(), strings[3].toString(), strings[2].toString()));
                                }
                                MusicManager.INSTANCE.playlist = listSo;
                                break;
                            case name:
                                ArrayList<Object[]> requestSe = CloudMusicAPI.INSTANCE.requestSearch(CloudMusicAPI.INSTANCE.getSearchJson(this.textField.textString));
                                ArrayList<Track> listSe = new ArrayList<>();
                                for (Object[] strings : requestSe) {
                                    listSe.add(new Track(Long.parseLong(strings[1].toString()), strings[0].toString(), strings[3].toString(), strings[2].toString()));
                                }
                                MusicManager.INSTANCE.playlist = listSe;
                                break;
                            case list:
                                MusicManager.INSTANCE.playlist = (ArrayList<Track>) CloudMusicAPI.INSTANCE.getPlaylistDetail(this.textField.textString)[1];
                                break;
                        }

                        for (int i = 0; i < MusicManager.INSTANCE.playlist.size(); i++) {
                            this.SubComponent.add(new TrackSlotComponent(MusicManager.INSTANCE.playlist.get(i)));
                        }

                    } catch (Exception ex) {
                        Client.renderMsg("解析歌单时发生错误!");
                        ex.printStackTrace();
                    }

                    MusicManager.INSTANCE.analyzeThread = null;
                });
                MusicManager.INSTANCE.analyzeThread.start();
            }
        }
        if (mouseButton == 1) {
            count++;
            if (count == search.values().length) {
                count = 0;
            }
            switch (count) {
                case 0:
                    searchMode = search.name;
                    break;
                case 1:
                    searchMode = search.song;
                    break;
                case 2:
                    searchMode = search.list;
                    break;
            }
        }
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
        if (showList) for (Component component : SubComponent) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void drawSubComponents(float x, float y, float mouseX, float mouseY) {
        if (showList) {
            for (TrackSlotComponent component : SubComponent) {
                component.setWidth(ClickUIScreen.width - ClickUIScreen.leftWidth - 15);
                component.setHeight(20);
                component.draw(x, y + AniScrollY, mouseX, mouseY);
                y += 23;
            }
        }
    }

    public void mouseClickedSubComponents(int mouseX, int mouseY, int mouseButton) {
        if (showList) for (Component component : SubComponent) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (showList) {
            this.textField.keyPressed(keyCode);
            this.textField.charTyped(typedChar);
        }
    }

    enum search {
        list, song, name
    }
}