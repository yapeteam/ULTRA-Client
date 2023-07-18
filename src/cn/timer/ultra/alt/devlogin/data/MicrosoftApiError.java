package cn.timer.ultra.alt.devlogin.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.UUID;

public class MicrosoftApiError {

    public String error;
    @SerializedName("error_description")
    public String errorDescription;
    @SerializedName("error_codes")
    public List<Integer> errorCodes;
    public String timestamp;
    @SerializedName("trace_id")
    public UUID traceId;
    @SerializedName("correlation_id")
    public UUID correlation_id;
    @SerializedName("error_uri")
    public String errorUri;
}
