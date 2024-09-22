package com.kill_sound.client;

import com.kill_sound.KillSound;
import com.kill_sound.KillSoundFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.impl.client.networking.v0.ClientSidePacketRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KillSoundClient implements ClientModInitializer {
    public static float time = 0;
    public static UUID player1_uuid;
    public static UUID player2_uuid;
    public static Text player1_text;
    public static Text player2_text;
    public static int player1_count;
    public Map<UUID, PlayerListEntry> player_list;
    public MinecraftClient mc;
    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistryImpl.INSTANCE.register(new Identifier("kill_sound", "show_kill"), (packetContext, attachedData) -> {
            UUID player1 = attachedData.readUuid();
            UUID player2 = attachedData.readUuid();
            int player1_c = attachedData.readInt();
            Text player1_n = attachedData.readText();
            Text player2_n = attachedData.readText();
            packetContext.getTaskQueue().execute(() -> {
                time = 40;
                player1_uuid = player1;
                player2_uuid = player2;
                player1_count = player1_c;
                player1_text = player1_n;
                player2_text = player2_n;
            });
        });
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            if(player_list == null){
                player_list = getEntry();
            }else{
                Map<UUID, PlayerListEntry> p = getEntry();
                if(p == null){
                    return;
                }
                for (Map.Entry<UUID, PlayerListEntry> entry : p.entrySet()) {
                    UUID key = entry.getKey();
                    PlayerListEntry value = entry.getValue();
                    if (!player_list.containsKey(key)) {
                        player_list.put(key, value);
                    }
                }
            }
            time -= tickDelta;
            if (time <= 0){
                time = 0;
            }else if(Objects.equals(KillSound.killSoundSettings.getData("showKillInfo").toString(), "false")){
                time = 0;
            }else{
                float a = 0.8f;
                int x = drawContext.getScaledWindowWidth() / 2 - 64;
                int y = 10;
                RenderSystem.setShaderColor(1, 1, 1, a);
                drawContext.drawTexture(new Identifier("kill_sound", "textures/gui/kill_bg.png"), x, y, 0, 0, 128, 32, 128, 32);

                Text t = KillSoundFunction.getTitle(player1_count);
                int tx = mc.textRenderer.getWidth(t);
                int ty = mc.textRenderer.fontHeight;
                drawContext.drawText(mc.textRenderer, t, x + 64 - tx / 2, y + 16 - ty / 2 - 2, 0xC06D00, false);

                drawContext.drawText(mc.textRenderer, player1_text, x + 30, y + 20, 0xffffff, false);
                drawContext.drawText(mc.textRenderer, player2_text, x + 97 - mc.textRenderer.getWidth(player2_text), y + 20, 0xffffff, false);

                PlayerSkinDrawer.draw(drawContext, player_list.get(player2_uuid).getSkinTexture(), x + 102, y + 8, 16, true, false);
                PlayerSkinDrawer.draw(drawContext, player_list.get(player1_uuid).getSkinTexture(), x + 10, y + 8, 16, true, false);
            }
        }));
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> mc = client);
    }

    @Nullable
    public Map<UUID, PlayerListEntry> getEntry(){
        Collection<UUID> l1 = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerUuids();
        Collection<PlayerListEntry> l2 = mc.getNetworkHandler().getPlayerList();
        if(l1 == null){
            return null;
        }else{
            if (l1.size() != l2.size()) {
                return null;
            }
            Map<UUID, PlayerListEntry> map = new HashMap<>();

            Iterator<UUID> keyIterator = l1.iterator();
            Iterator<PlayerListEntry> valueIterator = l2.iterator();
            while (keyIterator.hasNext() && valueIterator.hasNext()) {
                UUID key = keyIterator.next();
                PlayerListEntry value = valueIterator.next();
                map.put(key, value);
            }
            return map;
        }
    }
}
