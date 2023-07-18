package cn.timer.ultra.alt.devlogin;

import cn.timer.ultra.alt.MicrosoftOAuthLogin;
import cn.timer.ultra.alt.devlogin.data.Account;
import cn.timer.ultra.alt.devlogin.data.AuthenticationResponse;
import cn.timer.ultra.alt.devlogin.http.HttpEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class DevLogin {
    private static void login() throws Throwable {
        Account account;

        HttpEngine engine = HttpEngine.selectEngine();

        // New account!
        AuthenticationResponse msAuth = MicrosoftOAuth.deviceAuth(engine);
        account = MicrosoftOAuth.loginToAccount(engine, msAuth);

        engine.shutdown();

        Session session = new Session(
                account.username,
                account.uuid.toString().replace("-", ""),
                account.mcTokens.accessToken,
                "msa");
        setSession(session);
    }

    public static void setSession(Session session) {
        Minecraft.getMinecraft().session = session;
    }

    private static Thread loginThread = null;

    public static Thread getLoginThread() {
        return loginThread;
    }

    public static void StartLoginThread() {
        if (loginThread == null) {
            loginThread = new Thread(() -> {
                try {
                    login();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                MicrosoftOAuthLogin.browser.close();
                loginThread = null;
            });
            loginThread.start();
        }
    }

    public static void StopLoginThread() {
        if (loginThread != null) {
            loginThread.stop();
        }
        loginThread = null;
    }
}
