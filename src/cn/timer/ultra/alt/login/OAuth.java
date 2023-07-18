package cn.timer.ultra.alt.login;

import cn.timer.ultra.Client;
import cn.timer.ultra.alt.login.http.HttpResponse;
import cn.timer.ultra.alt.login.utils.ColUtils;
import cn.timer.ultra.alt.login.utils.HttpUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author TIMER_err
 */
public class OAuth {
    private static String readResponse(HttpResponse response) {
        return HttpUtils.getStringFromInputStream(new ByteArrayInputStream(response.body));
    }

    public static void login(LoginCallback callback) {
        try {
            String authorize = HttpUtils.buildUrl("https://login.live.com/oauth20_authorize.srf", ColUtils.mapOf(
                    "client_id", Client.CLIENT_ID,
                    "response_type", "code",
                    "redirect_url", "http://127.0.0.1:30828",
                    "scope", "XboxLive.signin offline_access"
            ));
            Desktop.getDesktop().browse(new URI(authorize));

            //Let's get the Code
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(30828), 0);
            httpServer.createContext("/", httpExchange -> {
                JsonParser parser = new JsonParser();
                String oauth_access_token =
                        //Parse the Response
                        parser.parse(
                                readResponse(
                                        //Let's get the Token
                                        HttpUtils.getEngine().postForm(
                                                "https://login.live.com/oauth20_token.srf", ColUtils.mapOf(
                                                        "client_id", Client.CLIENT_ID,
                                                        "code", httpExchange.getRequestURI().toString().
                                                                substring(
                                                                        httpExchange.getRequestURI().toString().
                                                                                lastIndexOf('=') + 1
                                                                ),
                                                        "grant_type", "authorization_code",
                                                        "redirect_url", "http://127.0.0.1:30828"
                                                )
                                        )
                                )
                        ).getAsJsonObject().get("access_token").getAsString();

                System.out.println("OAuthToken");

                //The XBL
                JsonObject xbl = HttpUtils.gson().fromJson(readResponse(
                                HttpUtils.getEngine().postJson(
                                        "https://user.auth.xboxlive.com/user/authenticate",
                                        HttpUtils.gson(), ColUtils.mapOf(
                                                "Properties", ColUtils.mapOf(
                                                        "AuthMethod", "RPS",
                                                        "SiteName", "user.auth.xboxlive.com",
                                                        "RpsTicket", "d=" + oauth_access_token
                                                ),
                                                "RelyingParty", "http://auth.xboxlive.com",
                                                "TokenType", "JWT"
                                        )
                                )
                        ), JsonObject.class
                );

                System.out.println("XBOX Live");

                String xbl_token = xbl.get("Token").getAsString();
                String xbl_uhs = xbl.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();

                //The XSTS
                JsonObject xsts = HttpUtils.gson().fromJson(readResponse(
                                HttpUtils.getEngine().postJson(
                                        "https://xsts.auth.xboxlive.com/xsts/authorize",
                                        HttpUtils.gson(), ColUtils.mapOf(
                                                "Properties", ColUtils.mapOf(
                                                        "SandboxId", "RETAIL",
                                                        "UserTokens", new String[]{xbl_token}
                                                ),
                                                "RelyingParty", "rp://api.minecraftservices.com/",
                                                "TokenType", "JWT"
                                        )
                                )
                        ), JsonObject.class
                );

                System.out.println("XSTS");

                String xsts_token = xsts.get("Token").getAsString();
                String xsts_uhs = xsts.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString();

                //Login with XBOX
                JsonObject xbox = HttpUtils.gson().fromJson(readResponse(
                                HttpUtils.getEngine().postJson(
                                        "https://api.minecraftservices.com/authentication/login_with_xbox",
                                        HttpUtils.gson(), ColUtils.mapOf(
                                                "identityToken", String.format("XBL3.0 x=%s;%s", xsts_uhs, xsts_token)
                                        )
                                )
                        ), JsonObject.class
                );

                System.out.println("Login XBOX");

                //McStore
                JsonObject mcstore = HttpUtils.gson().fromJson(readResponse(
                                HttpUtils.getEngine().getJson(
                                        "https://api.minecraftservices.com/entitlements/mcstore", ColUtils.mapOf(
                                                "Authorization", "Bearer " + xbox.get("access_token").getAsString()
                                        )
                                )
                        ), JsonObject.class
                );

                System.out.println("McStore");

                AtomicBoolean game_minecraft = new AtomicBoolean(false);
                AtomicBoolean product_minecraft = new AtomicBoolean(false);

                mcstore.get("items").getAsJsonArray().forEach(jsonElement -> {
                    if (jsonElement.getAsJsonObject().get("name").getAsString().equals("game_minecraft"))
                        game_minecraft.set(true);

                    if (jsonElement.getAsJsonObject().get("name").getAsString().equals("product_minecraft"))
                        product_minecraft.set(true);
                });

                boolean hasMinecraft = game_minecraft.get() && product_minecraft.get();

                if (hasMinecraft) {
                    //Get Minecraft profile!
                    JsonObject profile = HttpUtils.gson().fromJson(readResponse(
                                    HttpUtils.getEngine().getJson(
                                            "https://api.minecraftservices.com/minecraft/profile", ColUtils.mapOf(
                                                    "Authorization", "Bearer " + xbox.get("access_token").getAsString()
                                            )
                                    )
                            ), JsonObject.class
                    );

                    System.out.println("GetProfile");

                    callback.run(
                            profile.get("name").getAsString(),
                            profile.get("id").getAsString().replace("-", ""),
                            xbox.get("access_token").getAsString()
                    );
                }

                String success = hasMinecraft ?
                        "Login successfully, you can close this page now!" :
                        "It seems like you haven't bought Minecraft yet...";
                httpExchange.sendResponseHeaders(200, success.length());
                OutputStream responseBody = httpExchange.getResponseBody();
                responseBody.write(success.getBytes(StandardCharsets.UTF_8));
                responseBody.close();

                httpServer.stop(2);
            });
            httpServer.setExecutor(null);
            httpServer.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
