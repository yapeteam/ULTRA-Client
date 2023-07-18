package cn.timer.ultra;

import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class InjectChecker {
    public static StringBuilder data;

    public static boolean doCheck() {
        /*
        try {
            InputStream inputStream = Minecraft.class.getResourceAsStream("Minecraft.class");
            int len;
            StringBuilder data2 = new StringBuilder();
            while (true) {
                try {
                    assert inputStream != null;
                    if ((len = inputStream.read()) == -1) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                data2.append(len);
            }
            inputStream.close();
            if (!data2.toString().equals(data.toString())) {
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        return true;
    }

    public static void init() throws Exception {
        InputStream inputStream = Minecraft.class.getResourceAsStream("Minecraft.class");
        int len;
        data = new StringBuilder();
        try {
            while ((len = Objects.requireNonNull(inputStream).read()) != -1) {
                data.append(len);
            }
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
