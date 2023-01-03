package cn.timer.ultra.module.modules.render.alt;

import devlogin.DevLogin;

public class LoginThread extends Thread {
    public void run(){
        try {
            DevLogin.main(new String[]{""});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
