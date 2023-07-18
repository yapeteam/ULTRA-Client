package cn.timer.ultra.protection.lk;

import cn.timer.ultra.gui.loadFrame.LoadFrame;
import cn.timer.ultra.protection.ProtectionLoader;
import com.formdev.flatlaf.FlatDarkLaf;
import net.minecraft.client.Minecraft;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;

public class AntiLeak {
    private static final boolean onlyForMac = false;

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static boolean isMacOs() {
        String osName = getOsName();
        return osName != null && osName.startsWith("Mac") || onlyForMac;
    }

    /**
     * Message Windows Warning… don't change it
     */
    public static void windowMessage(final String title, final String message) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        if (title != null || message != null) {
            if (Objects.requireNonNull(title).isEmpty() || message.isEmpty()) {
                crashClient(title, message);
            }
            LoadFrame frame = new LoadFrame();
            frame.display(title);
            JOptionPane.showMessageDialog(frame.getjFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
            frame.close();
        }
    }

    public static void crashClient(final String title, final String context) {
        windowMessage(title, context);
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Minecraft.getMinecraft().shutdown();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        System.exit(1);
        System.exit(1);
        System.exit(1);
        System.exit(1);
        System.exit(1);
    }

    public static void Check() throws Exception {
        System.out.println(Arrays.toString("Ultra-Client".toCharArray()));
        if (getOsName().toLowerCase().contains("windows") && onlyForMac) {
            crashClient("Error", "Only for MacOS");
        }
        //ProtectionLoader.getLoadedClass().get("CheckMD5").getMethod("CheckMd5").invoke(null);
        if (isMacOs()) return;
        ProtectionLoader.getLoadedClass().get("CheckQQgroup").getMethod("CheckQQ").invoke(null);
    }
}
