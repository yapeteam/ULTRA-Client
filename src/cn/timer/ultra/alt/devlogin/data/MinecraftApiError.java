package cn.timer.ultra.alt.devlogin.data;

public class MinecraftApiError {

    public String path;
    public String errorType;
    public String error;
    public String errorMessage;
    public String developerMessage;

    @Override
    public String toString() {
        return "MinecraftApiError{" +
                "path='" + path + '\'' +
                ", errorType='" + errorType + '\'' +
                ", error='" + error + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", developerMessage='" + developerMessage + '\'' +
                '}';
    }
}
