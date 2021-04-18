package qysher.config.core;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Config {

    public HashMap<Object, Object> values = new HashMap<>();
    public File file;

    public Config(File file) {
        this.file = file;
    }

    public boolean createFileAndDirectories() {
        try {
            File parentFolder = file.getParentFile();
            if(parentFolder != null)
                if(!parentFolder.exists())
                    parentFolder.mkdirs();

            if(!file.exists())
                if(!file.createNewFile())
                    return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Config load() {
        if(!createFileAndDirectories()) return this;
        byte[] bytes = ByteFileWriter.readBytesFromFile(file);
        if(bytes.length != 0)
            values = ByteConversion.byteArrayToMap(bytes);
        return this;
    }

    public Config save() {
        if(!createFileAndDirectories()) return this;
        ByteFileWriter.writeBytesToFile(file, ByteConversion.objectToByteArray(values));
        return this;
    }

    public Config registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
        return this;
    }

    public void set(Object key, Object value) {
        values.put(key, value);
    }

    public void remove(Object key) {
        values.remove(key);
    }

    public <T> T valueOf(Object key) {
        return valueOf(key, null);
    }

    public <T> T valueOf(Object key, T default_value) {
        return values.containsKey(key) ? (T) values.get(key) : default_value;
    }

    public <T> T valueAs(Object key, Class<T> clazz) {
        return valueAs(key, null, clazz);
    }

    public <T> T valueAs(Object key, T default_value, Class<T> clazz) {
        if(!values.containsKey(key)) {
            return default_value;
        }

        try {
            return clazz.cast(values.get(key));
        } catch (ClassCastException ignored) { }

        return default_value;
    }

    public <T> T valueAs_Safe(Object key, Class<?> clazz) {
        return valueAs_Safe(key, null, clazz);
    }

    public <T> T valueAs_Safe(Object key, T default_value, Class<?> clazz) {
        return isCastValid(key, clazz) ? (T) values.get(key) : default_value;
    }

    public boolean isCastValid(Object key, Class<?> clazz) {
        if(values.containsKey(key)) {
            try {
                clazz.cast(values.get(key));
                return true;
            } catch (ClassCastException ignored) { }
        }
        return false;
    }

    public boolean contains(Object key) {
        return values.containsKey(key);
    }

    public static class StaticKey <T> {
        public Config config;
        public String key;
        public T default_value;

        public StaticKey(String key, T default_value, Config config) {
            this.key = key;
            this.default_value = default_value;
            this.config = config;
        }

        public String toString() {
            return key;
        }

        public void setValue(T value) {
            if(config == null) return;
            config.set(key, value);
        }

        public T getValue() {
            if(config == null) return null;
            return config.valueOf(key, default_value);
        }

        public T getValue(T override_default_value) {
            if(config == null) return null;
            return config.valueOf(key, override_default_value);
        }

        public boolean hasValue() {
            return config.contains(key);
        }
    }

    public static class ByteConversion {

        public static byte[] objectToByteArray(Object object) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.close();
                byte[] objectBytes = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                return objectBytes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new byte[0];
        }

        public static HashMap<Object, Object> byteArrayToMap(byte[] bytes) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInput.readObject();

                objectInput.close();
                byteArrayInputStream.close();

                if(isCastValid(object, HashMap.class)) {
                    System.out.println("Valid");
                    return (HashMap<Object, Object>) object;
                }
                System.out.println("Invalid");

                //return (HashMap<Object, Object>) object;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static boolean isCastValid(Object object, Class<?> clazz) {
            try {
                clazz.cast(object);
                return true;
            } catch (ClassCastException ignored) { }
            return false;
        }

    }

    public static class ByteFileWriter {

        public static void writeBytesToFile(File file, byte[] bytes) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static byte[] readBytesFromFile(File file) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                ArrayList<Byte> bytes = new ArrayList<>();
                int read;
                while((read = fileInputStream.read()) != -1) bytes.add((byte)read);

                byte[] byteArray = new byte[bytes.size()];
                for(int i = 0 ; i < bytes.size() ; i++) byteArray[i] = bytes.get(i);

                bytes.clear();

                fileInputStream.close();
                return byteArray;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new byte[0];
        }

    }

}
