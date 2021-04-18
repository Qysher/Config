package qysher.configsystem.test;

import qysher.configsystem.core.Config;

import java.io.File;

public class TestStart {

    public static Config config = new Config(new File("config.cfg")).load().registerShutdownHook();
    public static Config.StaticKey<String> testKey = new Config.StaticKey<>("test", "test_value", config);

    public static void main(String[] args) {
        config.set("Hallo", "Test");
        System.out.println(testKey.getValue());
        String str = config.valueOf("Hallo");
        System.out.println(config.valueAs("Hallo", String.class));
    }

}
