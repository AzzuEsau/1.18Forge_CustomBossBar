package com.azzubanana.custombossbar.functions;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;

public class MissionBar {
    private boolean isActive = false;
    private String bossName;
    private float current = 0;
    private float maxCurrent = 10;
    private ServerBossEvent missionBar = (ServerBossEvent)(new ServerBossEvent(Component.nullToEmpty("base"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6)).setPlayBossMusic(true);

    public void start(String name)
    {
        current = 0;
        bossName = name;
        isActive = true;
        missionBar.setVisible(true);
        update();
    }

    public void finish()
    {
        current = 0;
        isActive = false;
        missionBar.setVisible(false);
    }


    public void restart()
    {
        current = 0;
        missionBar.setVisible(true);
        update();
    }

    public void leave(ServerPlayer player)
    {
        removePlayer(player);
    }

// Game
    public boolean isGaming()
    {
        return isActive;
    }

    public void increase(int add)
    {
        current = add;
        update();
    }

    public void complete()
    {
        if (current < maxCurrent) {
            current = maxCurrent;
            update();
        }
    }

    public void update() {
        missionBar.setName(Component.nullToEmpty(bossName));
        missionBar.setProgress((float) current / (float) maxCurrent);

        if (current >= maxCurrent)
        {
            ClientboundSetTitleTextPacket title = new ClientboundSetTitleTextPacket(Component.nullToEmpty("§l§6 Mision §r"));
            ClientboundSetSubtitleTextPacket subtitle = new ClientboundSetSubtitleTextPacket(Component.nullToEmpty("Mision §3" + missionBar.getName().getString() + "§r §6§ncompletada§r"));

//            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));

            for (ServerPlayer player :missionBar.getPlayers()) {
                player.connection.send(title);
                player.connection.send(subtitle);

                player.level.playSound(null, (Entity) player, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.AMBIENT, 1, 1);
            }

            finish();
        }
    }

// Admin Players
    public void removePlayers()
    {
        missionBar.removeAllPlayers();
    }

    public void addPlayer(ServerPlayer player)
    {
        missionBar.addPlayer(player);
    }

    public void removePlayer(ServerPlayer player)
    {
        missionBar.removePlayer(player);
    }
}
