package com.azzubanana.custombossbar.event;

import com.azzubanana.custombossbar.CustomBossBar;
import com.azzubanana.custombossbar.command.BossBarMission;
import com.azzubanana.custombossbar.functions.MissionBar;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.sql.Timestamp;


public class ModEvents {
    public static MissionBar missionBar = new MissionBar();
    public static Timestamp lastMessage;
    @Mod.EventBusSubscriber(modid = CustomBossBar.MOD_ID)
    public static class ForgeEvents {
         // Commands
        @SubscribeEvent
        public static void Commands(RegisterCommandsEvent event)
        {
            new BossBarMission(event.getDispatcher(), missionBar);
        }
    }
}
