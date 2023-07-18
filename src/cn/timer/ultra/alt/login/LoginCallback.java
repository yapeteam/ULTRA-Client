package cn.timer.ultra.alt.login;

public interface LoginCallback {
    void run(String username, String uuid, String access_token);
}