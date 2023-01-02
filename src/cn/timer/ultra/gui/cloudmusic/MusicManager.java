package cn.timer.ultra.gui.cloudmusic;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.gui.cloudmusic.api.CloudMusicAPI;
import cn.timer.ultra.gui.cloudmusic.impl.Lyric;
import cn.timer.ultra.gui.cloudmusic.impl.Track;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Yarukon Created in 2021-4-6
 */

public class MusicManager {
    private static final String BIN_LIB = "nativedll/";
    public static MusicManager INSTANCE;
    public static boolean showMsg = false;

    static {
        INSTANCE = new MusicManager();
    }

    // 音乐封面缓存
    private final HashMap<Long, ResourceLocation> artsLocations = new HashMap<>();
    // 缓存文件夹
    private final File musicFolder;
    private final File artPicFolder;
    // 当前播放和播放列表
    public Track currentTrack = null;
    public ArrayList<Track> playlist = new ArrayList<>();
    // 用于缓存音乐的线程
    public Thread loadingThread = null;
    public Thread analyzeThread = null;
    public float downloadProgress = 0;
    public boolean repeat = false;
    public float cacheProgress = 0;
    public float[] magnitudes;
    public float[] smoothMagnitudes;
    // 歌词
    public Thread lyricAnalyzeThread = null;
    public boolean lyric = false;
    //	public ScrollingText songNameScroll;
//	public ScrollingText artistsScroll;
    public boolean noUpdate = false;
    public CopyOnWriteArrayList<Lyric> lrc = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<Lyric> tlrc = new CopyOnWriteArrayList<>();
    public HashMap<Long, ResourceLocation> circleLocations = new HashMap<>();
    public String lrcCur = "_EMPTY_";
    public String tlrcCur = "_EMPTY_";
    public int lrcIndex = 0;
    public int tlrcIndex = 0;
    public File circleImage;
    // I'm stuck with JavaFX MediaPlayer :(
    private MediaPlayer mediaPlayer;
    private static File libs;

    private synchronized static void loadLib(String libName) throws IOException {
        String systemType = System.getProperty("os.name");
        String libExtension = (systemType.toLowerCase().contains("win")) ? ".dll" : ".so";
        String libFullName = libName + libExtension;
        //String nativeTempDir = System.getProperty("java.io.tmpdir");
        InputStream in = null;
        BufferedInputStream reader;
        FileOutputStream writer = null;
        libs = new File(System.getProperty("java.library.path"));
        if (!libs.exists())
            libs.mkdirs();
        File libfile = new File(libs.getAbsolutePath() + File.separator + libFullName);
        if (libfile.exists())
            return;
        if (!libfile.exists()) {
            try {
                libfile.createNewFile();
                in = MusicManager.class.getResourceAsStream(BIN_LIB + libFullName);
                if (in == null)
                    in = MusicManager.class.getResourceAsStream(BIN_LIB + libFullName);
                MusicManager.class.getResource(BIN_LIB + libFullName);
                reader = new BufferedInputStream(in);
                writer = new FileOutputStream(libfile);
                byte[] buffer = new byte[1024];
                while (reader.read(buffer) > 0) {
                    writer.write(buffer);
                    buffer = new byte[1024];
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null)
                    in.close();
                if (writer != null)
                    writer.close();
            }
        }
        //System.load(libfile.toString());
    }

    public MusicManager() {
        // 实例化缓存文件夹
        // Minecraft 实例
        EventManager.instance.register(this);
        Minecraft mc = Minecraft.getMinecraft();
        musicFolder = new File(mc.mcDataDir, "Simple/musicCache");
        artPicFolder = new File(mc.mcDataDir, "Simple/artCache");
        File cookie = new File(mc.mcDataDir, "Simple/cookies.txt");
        File cache = new File(mc.mcDataDir, ".cache");
        if (!cache.exists()) cache.mkdirs();
        /*try {
            /*
            * Mode                 LastWriteTime         Length Name
----                 -------------         ------ ----
-a----         2018/9/13     22:57          19208 api-ms-win-core-console-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-datetime-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-debug-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-errorhandling-l1-1-0.dll
-a----         2018/9/13     22:57          22280 api-ms-win-core-file-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-file-l1-2-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-file-l2-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-handle-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-core-heap-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-interlocked-l1-1-0.dll
-a----         2018/9/13     22:57          19720 api-ms-win-core-libraryloader-l1-1-0.dll
-a----         2018/9/13     22:57          21256 api-ms-win-core-localization-l1-2-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-core-memory-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-namedpipe-l1-1-0.dll
-a----         2018/9/13     22:57          19720 api-ms-win-core-processenvironment-l1-1-0.dll
-a----         2018/9/13     22:57          20744 api-ms-win-core-processthreads-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-core-processthreads-l1-1-1.dll
-a----         2018/9/13     22:57          18184 api-ms-win-core-profile-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-core-rtlsupport-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-string-l1-1-0.dll
-a----         2018/9/13     22:57          20744 api-ms-win-core-synch-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-core-synch-l1-2-0.dll
-a----         2018/9/13     22:57          19720 api-ms-win-core-sysinfo-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-core-timezone-l1-1-0.dll
-a----         2018/9/13     22:57          18696 api-ms-win-core-util-l1-1-0.dll
-a----         2018/9/13     22:57          19720 api-ms-win-crt-conio-l1-1-0.dll
-a----         2018/9/13     22:57          22792 api-ms-win-crt-convert-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-crt-environment-l1-1-0.dll
-a----         2018/9/13     22:57          20744 api-ms-win-crt-filesystem-l1-1-0.dll
-a----         2018/9/13     22:57          19720 api-ms-win-crt-heap-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-crt-locale-l1-1-0.dll
-a----         2018/9/13     22:57          27912 api-ms-win-crt-math-l1-1-0.dll
-a----         2018/9/13     22:57          26888 api-ms-win-crt-multibyte-l1-1-0.dll
-a----         2018/9/13     22:57          71432 api-ms-win-crt-private-l1-1-0.dll
-a----         2018/9/13     22:57          19720 api-ms-win-crt-process-l1-1-0.dll
-a----         2018/9/13     22:57          23304 api-ms-win-crt-runtime-l1-1-0.dll
-a----         2018/9/13     22:57          24840 api-ms-win-crt-stdio-l1-1-0.dll
-a----         2018/9/13     22:57          24840 api-ms-win-crt-string-l1-1-0.dll
-a----         2018/9/13     22:57          21256 api-ms-win-crt-time-l1-1-0.dll
-a----         2018/9/13     22:57          19208 api-ms-win-crt-utility-l1-1-0.dll
-a----         2018/9/13     22:57         334640 concrt140.dll
-a----         2018/9/13     22:57          81408 decora_sse.dll
-a----         2018/9/13     22:57         118784 fxplugins.dll
-a----         2018/9/13     22:57         264192 glass.dll
-a----         2018/9/13     22:57         643584 glib-lite.dll
-a----         2018/9/13     22:57         884736 gstreamer-lite.dll
-a----         2018/9/13     22:57          67072 javafx_font.dll
-a----         2018/9/13     22:57         147968 javafx_iio.dll
-a----         2018/9/13     22:57         138240 jfxmedia.dll
-a----         2018/9/13     22:57       59174400 jfxwebkit.dll
-a----         2018/9/13     22:57         675112 msvcp140.dll
-a----         2018/9/13     22:57          55808 prism_common.dll
-a----         2018/9/13     22:57         125952 prism_d3d.dll
-a----         2018/9/13     22:57          92672 prism_sw.dll
-a----         2018/9/13     22:57        1016584 ucrtbase.dll
-a----         2018/9/13     22:57          87864 vcruntime140.dll
            loadLib("api-ms-win-core-console-l1-1-0");
            loadLib("api-ms-win-core-datetime-l1-1-0");
            loadLib("api-ms-win-core-debug-l1-1-0");
            loadLib("api-ms-win-core-errorhandling-l1-1-0");
            loadLib("api-ms-win-core-file-l1-1-0");
            loadLib("api-ms-win-core-file-l1-2-0");
            loadLib("api-ms-win-core-file-l2-1-0");
            loadLib("api-ms-win-core-handle-l1-1-0");
            loadLib("api-ms-win-core-heap-l1-1-0");
            loadLib("api-ms-win-core-interlocked-l1-1-0");
            loadLib("api-ms-win-core-libraryloader-l1-1-0");
            loadLib("api-ms-win-core-localization-l1-2-0");
            loadLib("api-ms-win-core-memory-l1-1-0");
            loadLib("api-ms-win-core-namedpipe-l1-1-0");
            loadLib("api-ms-win-core-processenvironment-l1-1-0");
            loadLib("api-ms-win-core-processthreads-l1-1-0");
            loadLib("api-ms-win-core-processthreads-l1-1-1");
            loadLib("api-ms-win-core-profile-l1-1-0");
            loadLib("api-ms-win-core-rtlsupport-l1-1-0");
            loadLib("api-ms-win-core-string-l1-1-0");
            loadLib("api-ms-win-core-synch-l1-1-0");
            loadLib("api-ms-win-core-synch-l1-2-0");
            loadLib("api-ms-win-core-sysinfo-l1-1-0");
            loadLib("api-ms-win-core-timezone-l1-1-0");
            loadLib("api-ms-win-core-util-l1-1-0");
            loadLib("api-ms-win-crt-conio-l1-1-0");
            loadLib("api-ms-win-crt-convert-l1-1-0");
            loadLib("api-ms-win-crt-environment-l1-1-0");
            loadLib("api-ms-win-crt-filesystem-l1-1-0");
            loadLib("api-ms-win-crt-heap-l1-1-0");
            loadLib("api-ms-win-crt-locale-l1-1-0");
            loadLib("api-ms-win-crt-math-l1-1-0");
            loadLib("api-ms-win-crt-multibyte-l1-1-0");
            loadLib("api-ms-win-crt-private-l1-1-0");
            loadLib("api-ms-win-crt-process-l1-1-0");
            loadLib("api-ms-win-crt-runtime-l1-1-0");
            loadLib("api-ms-win-crt-stdio-l1-1-0");
            loadLib("api-ms-win-crt-string-l1-1-0");
            loadLib("api-ms-win-crt-time-l1-1-0");
            loadLib("api-ms-win-crt-utility-l1-1-0");
            loadLib("concrt140");
            loadLib("decora_sse");
            loadLib("fxplugins");
            loadLib("glass");
            loadLib("glib-lite");
            loadLib("gstreamer-lite");
            loadLib("javafx_font");
            loadLib("javafx_iio");
            loadLib("jfxmedia");
            loadLib("jfxwebkit");
            loadLib("msvcp140");
            loadLib("prism_common");
            loadLib("prism_d3d");
            loadLib("prism_sw");
            loadLib("ucrtbase");
            loadLib("vcruntime140");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        if (!artPicFolder.exists())
            artPicFolder.mkdirs();

        if (!musicFolder.exists())
            musicFolder.mkdirs(); // 文件夹不存在时创建

        circleImage = new File(Minecraft.getMinecraft().mcDataDir.toString() + File.separator + Client.CLIENT_NAME + File.separator + "circleImage");
        if (!circleImage.exists()) {
            circleImage.mkdirs();
        }

//		songNameScroll = new ScrollingText(Helper.INSTANCE.fonts.siyuan16, "", Colors.WHITE.c);
//		songNameScroll.setWidth(112);
//		songNameScroll.setStepSize(.5f);

//		artistsScroll = new ScrollingText(Helper.INSTANCE.fonts.siyuan13, "", Colors.WHITE.c);
//		artistsScroll.setWidth(112);
//		artistsScroll.setStepSize(.5f);

        // JavaFX 初始化
        if (cookie.exists()) {
            try {
                String[] split = FileUtils.readFileToString(cookie).split(";");

                CloudMusicAPI.INSTANCE.cookies = new String[split.length][2];

                for (int i = 0; i < split.length; ++i) {
                    CloudMusicAPI.INSTANCE.cookies[i][0] = split[i].split("=")[0];
                    CloudMusicAPI.INSTANCE.cookies[i][1] = split[i].split("=")[1];
                }

                new Thread(() -> {
                    try {
                        CloudMusicAPI.INSTANCE.refreshState();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 加载本地音乐封面缓存
    public void loadFromCache(long id) {

        if (artsLocations.containsKey(id)) {
            return;
        }

        File path = new File(artPicFolder.getAbsolutePath() + File.separator + id);
        if (!path.exists())
            return;

        new Thread(() -> {
            artsLocations.put(id, null);
            ResourceLocation rl = new ResourceLocation("cloudMusicCache/" + id);
            IImageBuffer iib = new IImageBuffer() {
                final ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage parseUserSkin(BufferedImage image) {
                    return image;
                }

                @Override
                public void skinAvailable() {
                    artsLocations.put(id, rl);
                }
            };

            ThreadDownloadImageData textureArt = new ThreadDownloadImageData(path, null, null, iib);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
        }).start();
    }

    public ResourceLocation getArt(long id) {
        return artsLocations.get(id);
    }

    public void play(Track track) throws Exception {
        this.noUpdate = false;
        this.lrcIndex = 0;
        this.tlrcIndex = 0;
        if (this.currentTrack != null && this.currentTrack.id == track.id) {
            this.noUpdate = true;
        } else {
            this.lrc.clear();
            this.tlrc.clear();
            this.lrcCur = "等待歌词解析回应...";
            this.tlrcCur = "等待歌词解析回应...";
        }

        this.currentTrack = track;
        MusicManager.INSTANCE.loadFromCache(track.id);


//		songNameScroll.updateText();
//		artistsScroll.updateText();

        this.downloadProgress = 0;

        if (!showMsg) {
            showMsg = true;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        File mp3File = new File(musicFolder, track.id + ".mp3");
        File flacFile = new File(musicFolder, track.id + ".flac");
        File artFile = new File(artPicFolder, "" + track.id);

        if (!mp3File.exists() && !flacFile.exists()) {

            if (loadingThread != null) {
                loadingThread.interrupt();
            }

            loadingThread = new Thread(() -> {
                try {
                    String addr = (String) CloudMusicAPI.INSTANCE.getDownloadUrl(String.valueOf(track.id), 128000)[1];
                    CloudMusicAPI.INSTANCE.downloadFile(addr, addr.endsWith(".flac") ? flacFile.getAbsolutePath() : mp3File.getAbsolutePath());
                    MusicManager.INSTANCE.downloadFile(track.picUrl, artFile.getAbsolutePath());
                    play(track);
                } catch (Exception ex) {
                    Client.renderMsg("缓存音乐时发生错误, 可能是因为该歌曲已被下架或需要VIP!");
                    if (mp3File.exists())
                        mp3File.delete();

                    if (flacFile.exists())
                        flacFile.delete();

                    ex.printStackTrace();
                }

                loadingThread = null;
            });

            loadingThread.start();
        } else {
            Media hit = new Media(mp3File.exists() ? mp3File.toURI().toString() : flacFile.toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(1.0f);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setAudioSpectrumNumBands(128);
            mediaPlayer.setAudioSpectrumListener((double timestamp, double duration, float[] magnitudes, float[] phases) -> {
                if (this.magnitudes == null || this.magnitudes.length < magnitudes.length || this.magnitudes.length > magnitudes.length) {
                    this.magnitudes = new float[magnitudes.length];
                    this.smoothMagnitudes = new float[magnitudes.length];
                }

                for (int i = 0; i < magnitudes.length; i++) {
                    this.magnitudes[i] = magnitudes[i] - mediaPlayer.getAudioSpectrumThreshold();
                }
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                if (repeat) {
                    try {
                        play(currentTrack);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    next();
                }
            });
        }

        if (!this.noUpdate) {

            if (this.lyricAnalyzeThread != null) {
                this.lyricAnalyzeThread.interrupt();
            }

            this.lyricAnalyzeThread = new Thread(() -> {
                try {
                    String[] lyrics = CloudMusicAPI.INSTANCE.requestLyric(CloudMusicAPI.INSTANCE.getLyricJson(String.valueOf(track.id)));

                    this.lrc.clear();
                    this.tlrc.clear();

                    if (!lyrics[0].equals("")) {
                        if (lyrics[0].equals("_NOLYRIC_")) {
                            this.lrcCur = currentTrack.name;
                        } else {
                            CloudMusicAPI.INSTANCE.analyzeLyric(this.lrc, lyrics[0]);
                        }
                    } else {
                        this.lrcCur = "(解析时发生错误或歌词不存在)";
                        this.lrc.clear();
                    }

                    if (!lyrics[1].equals("")) {
                        if (lyrics[1].equals("_NOLYRIC_")) {
                            this.tlrcCur = "纯音乐, 请欣赏";
                        } else if (lyrics[1].equals("_UNCOLLECT_")) {
                            this.tlrcCur = "该歌曲暂无歌词";
                        } else {
                            CloudMusicAPI.INSTANCE.analyzeLyric(this.tlrc, lyrics[1]);
                        }
                    } else {
                        this.tlrcCur = "(解析时发生错误或翻译歌词不存在)";
                        this.tlrc.clear();
                    }

                } catch (Exception ex) {
                    this.lrc.clear();
                    this.tlrc.clear();
                    this.lrcCur = currentTrack.name;
                    this.tlrcCur = "(获取歌词时出现错误)";
                    ex.printStackTrace();
                }

            });

            this.lyricAnalyzeThread.start();
        }

    }

    @EventTarget
    public void onTick(EventTick e) {
        if (this.getMediaPlayer() != null) {
            long mill = (long) this.getMediaPlayer().getCurrentTime().toMillis();
            if (!this.lrc.isEmpty()) {
                if (lrcIndex >= lrc.size()) {
                    return;
                }
                if (this.lrc.get(this.lrcIndex).time < mill) {
                    lrcIndex += 1;

                    this.lrcCur = this.lrc.get(lrcIndex - 1).text;

                    if (this.tlrc.isEmpty()) {
                        this.tlrcCur = lrcIndex > this.lrc.size() - 1 ? "" : this.lrc.get(lrcIndex).text;
                    }
                }
            }

            if (!this.tlrc.isEmpty()) {
                if (tlrcIndex >= tlrc.size()) {
                    return;
                }
                if (this.tlrc.get(this.tlrcIndex).time < mill) {
                    tlrcIndex += 1;
                    this.tlrcCur = tlrcIndex - 1 > this.tlrc.size() - 1 ? "" : this.tlrc.get(tlrcIndex - 1).text;
                }
            }
        }
    }


    public void getCircle(Track track) {

        if (circleLocations.containsKey(track.id)) {
            return;
        }

        try {
            if (!new File(this.circleImage.getAbsolutePath() + File.separator + track.id).exists()) {
                this.makeCirclePicture(track, 128, circleImage.getAbsolutePath() + File.separator + track.id);
            }

            ResourceLocation rl2 = new ResourceLocation("circle/" + track.id);
            IImageBuffer iib2 = new IImageBuffer() {

                public BufferedImage parseUserSkin(BufferedImage a) {
                    return a;
                }

                @Override
                public void skinAvailable() {
                    circleLocations.put(track.id, rl2);
                }
            };
            ThreadDownloadImageData textureArt2 = new ThreadDownloadImageData(new File(circleImage.getAbsolutePath() + File.separator + track.id), null, null, iib2);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl2, textureArt2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //TODO Make Circle Picture
    public void makeCirclePicture(Track track, int wid, String path) {
        try {
            BufferedImage avatarImage = ImageIO.read(new URL(track.picUrl));

            BufferedImage formatAvatarImage = new BufferedImage(wid, wid, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D graphics = formatAvatarImage.createGraphics();
            {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int border = 0;
                Ellipse2D.Double shape = new Ellipse2D.Double(border, border, wid - border * 2, wid - border * 2);

                graphics.setClip(shape);
                graphics.drawImage(avatarImage, border, border, wid - border * 2, wid - border * 2, null);
                graphics.dispose();
            }

            try (OutputStream os = new FileOutputStream(path)) {
                ImageIO.write(formatAvatarImage, "png", os);
            } catch (Exception ignored) {
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void downloadFile(String url, String filepath) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            File file = new File(filepath);
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[10 * 1024];
            int ch = 0;

            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }

            is.close();
            fileout.flush();
            fileout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next() {
        try {
            if (!playlist.isEmpty()) {
                if (currentTrack == null) {
                    play(playlist.get(0));
                } else {
                    boolean playNext = false;
                    for (Track t : playlist) {
                        if (playNext) {
                            play(t);
                            break;
                        } else if (t.id == currentTrack.id) {
                            playNext = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prev() {
        try {
            if (!playlist.isEmpty()) {
                if (currentTrack == null) {
                    play(playlist.get(0));
                } else {
                    boolean playPrev = false;
                    for (int i = 0; i < playlist.size(); ++i) {
                        Track t = playlist.get(i);
                        if (playPrev) {

                            if (i - 2 < 0) {
                                play(playlist.get(playlist.size() - 1));
                                break;
                            }

                            play(playlist.get(i - 2));
                            break;
                        } else if (t.id == currentTrack.id) {
                            playPrev = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Thread getLoadingThread() {
        return loadingThread;
    }
}
