package com.kill_sound.mixin;

import com.kill_sound.KillSoundFunction;
import com.kill_sound.KillSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DamageSource.class)
public abstract class SetDeathMessage {

    @Shadow @Final @Nullable private Entity attacker;

    @Redirect(method = "getDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;"))
    public MutableText getDeathMessage(String key, Object[] args) {
        if(!KillSound.killSoundSettings.getData("setDeadText").toString().equals("true")){
            return Text.translatable(key, args);
        }
        if (KillSound.s != null && attacker != null && KillSound.s.isPlayer() && attacker.isPlayer()){
            return KillSoundFunction.getKillText(KillSoundFunction.GetKillCount((PlayerEntity) attacker) - 1, args[1]);
        }else{
            return Text.translatable(key, args);
        }
    }
}
