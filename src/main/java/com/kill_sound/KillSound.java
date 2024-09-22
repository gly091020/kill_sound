package com.kill_sound;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KillSound implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("kill_sound");
    public static final Map<PlayerEntity, Integer> killCount = new HashMap<>();
    public static PlayerEntity s;
    public static List<SoundEvent> sounds = new ArrayList<>();
    public static KillSoundSettings killSoundSettings = new KillSoundSettings();
    @Override
    public void onInitialize() {
        for (int i = 1; i < 7; i++) {
            sounds.add(SoundEvent.of(new Identifier("kill_sound", "kill" + i)));
            Registry.register(Registries.SOUND_EVENT, new Identifier("kill_sound", "kill" + i), sounds.get(i - 1));
        }
    }
}
