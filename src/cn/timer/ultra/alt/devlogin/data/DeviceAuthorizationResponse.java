package cn.timer.ultra.alt.devlogin.data;

import com.google.gson.annotations.SerializedName;

public class DeviceAuthorizationResponse {

    @SerializedName("user_code")
    public String userCode;
    @SerializedName("device_code")
    public String deviceCode;
    @SerializedName("verification_url")
    public String verificationUrl;
    @SerializedName("expires_in")
    public int expiresIn;
    public int interval;
    public String message;
}
