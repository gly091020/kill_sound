package com.kill_sound;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class KillSoundFunction {
    public static MutableText getKillText(int index, Object playerName){
        if (index <= 4){
            return Text.translatable("sound.kill_sound.kill" + (index + 1), playerName);
        }else{
            return Text.translatable("sound.kill_sound.kill5", playerName);
        }
    }

    public static void SetKillCount(PlayerEntity a, PlayerEntity s){
        if (KillSound.killCount.containsKey(a)){
            KillSound.killCount.put(a, KillSound.killCount.get(a) + 1);
        }else{
            KillSound.killCount.put(a, 1);
        }

        KillSound.killCount.put(s, 0);
    }

    public static int GetKillCount(PlayerEntity a){
        return KillSound.killCount.getOrDefault(a, 0);
    }

    public static SoundEvent getSound(int index){
        if(index < KillSound.sounds.size()) {
            return KillSound.sounds.get(index);
        }else{
            return KillSound.sounds.get(KillSound.sounds.size() - 1);
        }
    }

    public static Text getTitle(int index){
        if(index > 4){
            return Text.translatable("title.kill_sound.kill5");
        }
        return Text.translatable("title.kill_sound.kill" + (index + 1));
    }
}
