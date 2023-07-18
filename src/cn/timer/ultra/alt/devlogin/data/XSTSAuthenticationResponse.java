package cn.timer.ultra.alt.devlogin.data;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class XSTSAuthenticationResponse {

    @SerializedName("IssueInstant")
    public String issueInstant;
    @SerializedName("NotAfter")
    public String notAfter;
    @SerializedName("Token")
    public String token;
    @SerializedName("DisplayClaims")
    public JsonObject displayClaims;

    public String getUserId() {
        // Ew, thanks Microsoft..
        return displayClaims.get("xui").getAsJsonArray().get(0).getAsJsonObject().get("ush").getAsString();
    }
}
