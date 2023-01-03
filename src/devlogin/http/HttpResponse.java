package devlogin.http;

import com.google.gson.Gson;
import devlogin.util.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by covers1624 on 11/9/22.
 */
public class HttpResponse {

    public final int code;
    public final String message;
    public final byte[] body;

    public HttpResponse(int code, String message, byte[] body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public <T> T fromJson(Gson gson, Type type) throws IOException {
        assert body != null : "Response has no body?";
        return JsonUtils.parse(gson, new ByteArrayInputStream(body), type);
    }
}
