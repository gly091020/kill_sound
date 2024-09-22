package com.kill_sound.mixin;

import com.kill_sound.KillSoundFunction;
import com.kill_sound.KillSound;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.impl.networking.v0.ServerSidePacketRegistryImpl;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ServerPlayerEntity.class)
public class PlayerDiedMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.getAttacker() != null && damageSource.getAttacker().isPlayer()) {
            PlayerEntity attacker = (PlayerEntity) damageSource.getAttacker();
            ServerPlayerEntity victim = (ServerPlayerEntity) (Object) this;
            KillSound.LOGGER.info("{} killed {}", attacker.getEntityName(), victim.getEntityName());
            KillSoundFunction.SetKillCount(attacker, victim);
            KillSound.s = victim;
            List<? extends PlayerEntity> playerEntityList = victim.getWorld().getPlayers();
            if (KillSound.killSoundSettings.getData("playKillSound").toString().equals("true")) {
                for (PlayerEntity playerEntity : playerEntityList) {
                    playerEntity.playSound(KillSoundFunction.getSound(KillSoundFunction.GetKillCount(attacker) - 1), 10, KillSound.killSoundSettings.getData("raiseThePitch").toString().equals("true") ? 1.5f : 1);
                }
            }
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeUuid(attacker.getUuid());
            passedData.writeUuid(victim.getUuid());
            passedData.writeInt(KillSoundFunction.GetKillCount(attacker) - 1);
            passedData.writeText(attacker.getDisplayName());
            passedData.writeText(victim.getDisplayName());
            ServerSidePacketRegistryImpl.INSTANCE.sendToPlayer(attacker, new Identifier("kill_sound", "show_kill"), passedData);
        }
    }
}
