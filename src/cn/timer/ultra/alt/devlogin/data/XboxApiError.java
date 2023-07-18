package cn.timer.ultra.alt.devlogin.data;

import com.google.gson.annotations.SerializedName;

public class XboxApiError {

    @SerializedName("Identity")
    public int identity;
    @SerializedName("XErr")
    public long xErr;
    @SerializedName("Message")
    public String message;
    @SerializedName("Redirect")
    public String redirect;

    @Override
    public String toString() {
        return "XboxApiError{" +
                "identity=" + identity +
                ", xErr=" + xErr +
                ", message='" + message + '\'' +
                ", redirect='" + redirect + '\'' +
                '}';
    }
}
