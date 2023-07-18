package cn.timer.ultra.alt;

import cn.timer.ultra.alt.login.LoginScreen;
import cn.timer.ultra.alt.login.OAuth;
import cn.timer.ultra.cef.Browser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MicrosoftOAuthLogin {
    public static Browser browser;
    private static final Logger logger = LogManager.getLogger();

    public static void login() {
        LoginScreen.setRender(true);
        OAuth.login(((username, uuid, access_token) -> {
            Minecraft.getMinecraft().session = new Session(username, uuid, access_token, "msa");
            LoginScreen.setRender(false);
            System.out.println("Successfully");
            logger.info(String.format("Login Successfully! Name: %s, uuid: %s", username, uuid));
        }));
        /*
        if (DevLogin.getLoginThread() != null) return;
        DevLogin.StartLoginThread();
        try {
            browser = new Browser("https://www.microsoft.com/link");
            browser.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }
}
