package cn.timer.ultra.cef;

import cn.timer.ultra.protection.ProtectionLoader;
import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

public class Browser extends JFrame {
    private final CefClient client;
    private final CefBrowser browser;

    static {
        try {
            if (!new File(System.getProperty("java.library.path"), "libcef.dll").exists() && !((boolean) ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("isMacOs").invoke(null))) {
                try {
                    NativeLoader.loadLibrary();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    CefApp cefApp;

    boolean closed;

    public Browser(String url) {
        try {
            if ((boolean) ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("isMacOs").invoke(null)) {
                client = null;
                browser = null;
                closed = true;
                try {
                    Desktop.getDesktop().browse(URI.create(url));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = false;
        if (CefApp.getState() != CefApp.CefAppState.NONE && CefApp.getState() != CefApp.CefAppState.NEW)
            cefApp = CefApp.getInstance();
        else cefApp = CefApp.getInstance(settings);
        // 创建 CefClient 和 CefBrowser 实例
        client = cefApp.createClient();
        browser = client.createBrowser(url, false, false);
        Component browserUI = browser.getUIComponent();
        getContentPane().add(browserUI, BorderLayout.CENTER);
        pack();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        System.out.println(CefApp.getInstance().getVersion());
        System.out.println(CefApp.getState().name());
    }

    public CefClient getClient() {
        return client;
    }

    public CefBrowser getBrowser() {
        return browser;
    }

    public void open() {
        if (closed) return;
        if (!isVisible()) {
            setVisible(true);
        }
    }

    public void close() {
        if (closed) return;
        browser.close(false);
        client.dispose();
        closed = true;
        setVisible(false);
    }
}
