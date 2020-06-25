package qysher.configsystem.test;

import qysher.configsystem.core.ConfigSystem;

import java.io.File;

public class TestStart {

    public static void main(String[] args) {
        ConfigSystem configSystem = new ConfigSystem(new File("config.cfg")).load().registerShutdownHook();
        configSystem.set("Hallo", "Test");
        String str = configSystem.get("Hallo");
        System.out.println(configSystem.getAs("Hallo", String.class));
    }

}
