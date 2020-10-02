package utils;

import java.io.IOException;
import java.nio.file.Paths;

public class TestInitialization {

    private static boolean isInit;

    static {
        isInit = false;
    }

    private TestInitialization() {

    }

    public static synchronized void init() {
        if (!isInit) {
            try {

                String userDir = System.getProperty("user.dir");

                TestConfiguration.readRunProperties(Paths.get(userDir, "run.properties"));
                TestConfiguration.readConfig(Paths.get(userDir, "env_config", TestConfiguration.getEnv() + ".properties"));

                isInit = true;

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}