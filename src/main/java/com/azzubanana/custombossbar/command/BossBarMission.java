package com.azzubanana.custombossbar.command;

import com.azzubanana.custombossbar.functions.MissionBar;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class BossBarMission {
    private static final String commandName = "boss";
    private static final Component emptyBossBar = Component.nullToEmpty("Not bossbar active");

    public BossBarMission(CommandDispatcher<CommandSourceStack> dispatcher, MissionBar missionBar   )
    {
        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("start")
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes((command) ->
                { return startGame(command.getSource(), StringArgumentType.getString(command, "name"), missionBar); }
        ))));

        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("join")
                .then(Commands.argument("players", EntityArgument.players())
                        .executes((command) ->
                { return joinGame(command.getSource(), missionBar, EntityArgument.getPlayers(command, "players")); }
        ))));

        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("leave")
                .then(Commands.argument("players", EntityArgument.players())
                        .executes((command) ->
                { return leaveGame(command.getSource(), missionBar, EntityArgument.getPlayers(command, "players")); }
        ))));

        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("set")
                .then(Commands.argument("value", IntegerArgumentType.integer())
                        .executes((command) ->
                { return increase(command.getSource(), IntegerArgumentType.getInteger(command, "value") , missionBar); }
        ))));

        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("complete")
                        .executes((command) ->
                { return completeGame(command.getSource(), missionBar); }
        )));

        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("stop")
                        .executes((command) ->
                { return stopGame(command.getSource(), missionBar); }
        )));

        dispatcher.register(Commands.literal(commandName)
                .then(Commands.literal("restart")
                        .executes((command) ->
                { return restartGame(command.getSource(), missionBar); }
        )));
    }

    private int joinGame(CommandSourceStack source, MissionBar missionBar, Collection<ServerPlayer> players) throws CommandSyntaxException
    {
        if (missionBar.isGaming())
            for(ServerPlayer player: players)
                missionBar.addPlayer(player);
        else
            for(ServerPlayer player: players)
                player.sendMessage(emptyBossBar,  player.getUUID());

        return 1;
    }

    private int leaveGame(CommandSourceStack source, MissionBar missionBar, Collection<ServerPlayer> players) throws CommandSyntaxException
    {
        if (missionBar.isGaming())
            for(ServerPlayer player: players)
                missionBar.leave(player);
        else
            for(ServerPlayer player: players)
                player.sendMessage(emptyBossBar,  player.getUUID());

        return 1;
    }

    private int startGame(CommandSourceStack source, String name, MissionBar missionBar) throws CommandSyntaxException
    {
        if (!missionBar.isGaming())
        {
            missionBar.start(name);
            missionBar.addPlayer(source.getPlayerOrException());
        }
        else
            source.getPlayerOrException().sendMessage(Component.nullToEmpty("Bossbar already running"), source.getPlayerOrException().getUUID());

        return 1;
    }

    private int increase (CommandSourceStack source, int value, MissionBar missionBar) throws CommandSyntaxException {
        if (missionBar.isGaming())
            missionBar.increase(value);
        else
            source.getPlayerOrException().sendMessage(emptyBossBar, source.getPlayerOrException().getUUID());

        return 1;
    }

    private int completeGame (CommandSourceStack source, MissionBar missionBar) throws CommandSyntaxException {
        if (missionBar.isGaming())
            missionBar.complete();
        else
            source.getPlayerOrException().sendMessage(emptyBossBar, source.getPlayerOrException().getUUID());

        return 1;
    }
    private int stopGame (CommandSourceStack source, MissionBar missionBar) throws CommandSyntaxException {
        if (missionBar.isGaming())
            missionBar.finish();
        else
            source.getPlayerOrException().sendMessage(emptyBossBar, source.getPlayerOrException().getUUID());

        return 1;
    }

    private int restartGame (CommandSourceStack source, MissionBar missionBar)
    {
        if (missionBar.isGaming())
        {
            missionBar.restart();
        }
        return 1;
    }
}
