package cn.timer.ultra.protection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProtectClassLoader extends ClassLoader {
    public Class<?> loadClassFromStream(InputStream stream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        byte[] classData = outStream.toByteArray();
        return defineClass(null, classData, 0, classData.length);
    }
}
