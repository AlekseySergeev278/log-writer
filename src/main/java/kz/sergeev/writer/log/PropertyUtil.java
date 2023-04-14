package kz.sergeev.writer.log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyUtil {
    private static final Properties PROPERTIES = new Properties();

    private PropertyUtil() {
    }

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static void set(String key, String value) {
        PROPERTIES.setProperty(key, value);
    }

    private static void loadProperties() {
        try (InputStream resourceAsStream = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
