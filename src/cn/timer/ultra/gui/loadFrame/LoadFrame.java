package cn.timer.ultra.gui.loadFrame;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class LoadFrame {
    private JPanel JP;
    private JLabel text;
    private JProgressBar progressBar;
    private JLabel logo;
    private final JFrame jFrame;

    public LoadFrame() {
        int width = 300, height = 160;
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenW = gd.getDisplayMode().getWidth();
        int screenH = gd.getDisplayMode().getHeight();
        jFrame = new JFrame("Ultra Client Loading...");
        try {
            logo.setFont(getFont(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("client/Fonts/payback.ttf")), 42));
        } catch (IOException e) {
            System.err.println("Font load failed.");
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(Arrays.toString(new int[]{screenSize.width, screenSize.height}));
        jFrame.setSize(width * (screenSize.width / 1920), height * (screenSize.height / 1080));
        JP.setSize(width, height);
        jFrame.setLocation((screenW - width) / 2, (screenH - height) / 2);
        jFrame.add(JP);
        jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                jFrame.setVisible(false);
                Minecraft.getMinecraft().shutdown();
                System.exit(0);
            }
        });
    }

    public void display(String text) {
        this.setText(text);
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("loading please wait...");
        this.jFrame.setVisible(true);
        this.jFrame.setAlwaysOnTop(true);
    }

    public void close() {
        this.jFrame.setVisible(false);
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public static Font getFont(InputStream is, int size) {
        Font font;
        try {
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }
}
