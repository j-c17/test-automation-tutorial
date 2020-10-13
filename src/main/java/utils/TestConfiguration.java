package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class TestConfiguration {

    private static String env;
    private static String host;

    private TestConfiguration() {
    }

    private static Properties readProperties(Path file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file.toFile())) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }
    }

    public static void readRunProperties(Path localFile) throws IOException {
        // check if scope and env properties putted via maven
        if (System.getProperty("env") == null) {
            //if not: read file
            Properties properties = readProperties(localFile);
            env = properties.getProperty("env");
        } else {
            //if yes: read from system
            env = System.getProperty("env");
        }
    }

    public static void readConfig(Path path) throws IOException {
        Properties properties = readProperties(path);
        host = properties.getProperty("host");
    }


    public static String getHost() {
        return host;
    }

    public static String getEnv() {
        return env;
    }
}