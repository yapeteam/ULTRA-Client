package cn.timer.ultra.cef;

import cn.timer.ultra.protection.ProtectionLoader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class NativeLoader {
    public static void unzip(InputStream zipFile, String desDirectory) throws Exception {
        File desDir = new File(desDirectory);
        if (!desDir.exists()) {
            boolean mkdirSuccess = desDir.mkdir();
            if (!mkdirSuccess) {
                throw new Exception("Failed to mkdir!");
            }
        }
        // 读入流
        ZipInputStream zipInputStream = new ZipInputStream(zipFile);
        // 遍历每一个文件
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            String unzipFilePath = desDirectory + File.separator + zipEntry.getName();
            if (zipEntry.isDirectory()) { // 文件夹
                // 直接创建
                mkdir(new File(unzipFilePath));
            } else { // 文件
                File file = new File(unzipFilePath);
                // 创建父目录
                mkdir(file.getParentFile());
                // 写出文件流
                BufferedOutputStream bufferedOutputStream =
                        new BufferedOutputStream(Files.newOutputStream(Paths.get(unzipFilePath)));
                byte[] bytes = new byte[1024];
                int readLen;
                while ((readLen = zipInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, readLen);
                }
                bufferedOutputStream.close();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    // 如果父目录不存在则创建
    private static void mkdir(File file) {
        if (null == file || file.exists()) {
            return;
        }
        mkdir(file.getParentFile());
        file.mkdir();
    }

    public static void loadLibrary() throws Exception {
        if ((boolean) ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("isMacOs").invoke(null)) return;
        unzip(NativeLoader.class.getResourceAsStream("jcef-native-windows-amd64.jar"), System.getProperty("java.library.path"));
    }
}
