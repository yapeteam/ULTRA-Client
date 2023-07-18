package cn.timer.ultra.protection;

import cn.timer.ultra.protection.lk.AntiLeak;
import cn.timer.ultra.protection.lk.AntiLeakData;
import cn.timer.ultra.protection.lk.utils.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ProtectionLoader {
    private static final Map<String, Class<?>> loadedClass = new HashMap<>();

    static {
        try {
            load();
        } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (true) { //DebugMode
            loadedClass.put("Client", Client.class);
            loadedClass.put("AntiLeak", AntiLeak.class);
            loadedClass.put("CheckMD5", CheckMD5.class);
            loadedClass.put("CheckQQgroup", CheckQQgroup.class);
            loadedClass.put("EmailSender", EmailSender.class);
        } else {
            loadedClass.put("Client", new ProtectClassLoader().loadClassFromStream(new ByteArrayInputStream(new ClientData().load().blazz)));
            loadedClass.put("AntiLeak", new ProtectClassLoader().loadClassFromStream(new ByteArrayInputStream(new AntiLeakData().load().blazz)));
            loadedClass.put("CheckMD5", new ProtectClassLoader().loadClassFromStream(new ByteArrayInputStream(new CheckMD5Data().load().blazz)));
            loadedClass.put("CheckQQgroup", new ProtectClassLoader().loadClassFromStream(new ByteArrayInputStream(new CheckQQgroupData().load().blazz)));
            loadedClass.put("EmailSender", new ProtectClassLoader().loadClassFromStream(new ByteArrayInputStream(new EmailSenderData().load().blazz)));
        }
    }

    public static Map<String, Class<?>> getLoadedClass() {
        return loadedClass;
    }

    public static void main(String[] args) throws IOException {
        convertToHex(System.out, new File("./CheckMD5.class"));
    }

    public static void convertToHex(PrintStream out, File myFileReader) throws IOException {
        InputStream is = Files.newInputStream(myFileReader.toPath());
        int value;
        StringBuilder sbHex = new StringBuilder();
        StringBuilder string = new StringBuilder();
        while ((value = is.read()) != -1) {
            // convert to hex value with "x" formatter
            string.append(String.format("%02x", value));
            if (string.toString().length() == 128) {
                sbHex.append(String.format("sb.append(\"%s\");\n", string));
                string = new StringBuilder();
            }
        }
        sbHex.append(String.format("sb.append(\"%s\");\n", string));
        out.print(sbHex);
        is.close();
    }
}