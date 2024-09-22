package com.kill_sound;

import java.io.*;
import java.util.Properties;

public class KillSoundSettings {
    private Properties properties;

    public KillSoundSettings(){
        properties = readFromFile();
    }

    public String getFilePath(){
        File file = new File("config/kill_sound.config.properties");
        if(!file.isFile()){
            try {
                file.createNewFile();
                properties = getProperties();
                writeToFile();
            }catch (IOException e){
                KillSound.LOGGER.error("Don't write file:{}", file.getPath());
                return null;
            }
        }
        return "config/kill_sound.config.properties";
    }

    public Properties readFromFile(){
        Properties p = new Properties();
        try {
            FileReader fileReader = new FileReader(getFilePath());
            p.load(fileReader);
        }catch (IOException e){
            KillSound.LOGGER.error(e.getMessage());
            p = getProperties();
        }
        return p;
    }

    public Properties getProperties(){
        Properties p = new Properties();
        p.put("raiseThePitch", "false");
        p.put("showKillInfo", "true");
        p.put("playKillSound", "true");
        p.put("setDeadText", "true");
        return p;
    }

    public Object getData(Object key){
        return properties.getOrDefault(key, "null");
    }

    public void setData(Object key, Object value){
        properties.put(key, value);
    }

    public void writeToFile(){
        try {
            properties.store(new FileOutputStream(getFilePath()), null);
        } catch (IOException e) {
            KillSound.LOGGER.error("Don't write file!");
            throw new RuntimeException(e);
        }
    }
}
