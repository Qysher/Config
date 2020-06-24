package qysher.configsystem.test;

import qysher.configsystem.core.ConfigSystem;

import java.io.File;
import java.util.Arrays;

public class TestStart {

    public static void main(String[] args) {
        ConfigSystem configSystem = new ConfigSystem(new File("config.cfg"));
        configSystem.load();
        //configSystem.set("Hallo", "Test");
        configSystem.save();
        System.out.println(Arrays.toString(configSystem.values.entrySet().toArray()));
    }

}
