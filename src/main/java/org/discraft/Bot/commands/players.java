package org.discraft.Bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.discraft.Bot.core.classes.SlashCommand;
import org.discraft.PlayTimer;

import java.awt.*;
import java.util.List;

public class players implements SlashCommand {
    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("players", "Shows all the current online players");
    }

    private String getPlayTime(long joinTime) {
        long currentTime = System.currentTimeMillis();
        long playTimeMillis = currentTime - joinTime;

        long seconds = (playTimeMillis / 1000) % 60;
        long minutes = (playTimeMillis / (1000 * 60)) % 60;
        long hours = (playTimeMillis / (1000 * 60 * 60)) % 24;

        String playtimeFormatted = (hours > 0 ? "`" + hours + "`h " : "") +
                (minutes > 0 ? "`" + minutes + "`m " : "") +
                "`" + seconds + "`s";


        return playtimeFormatted;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<Player> onlinePlayers = List.copyOf(Bukkit.getOnlinePlayers());

        if (onlinePlayers.isEmpty()) {
            Color color = new Color(230, 75, 64);
            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription("❌ | No online players found >:")
                    .setColor(color);

            event.replyEmbeds(embed.build()).setEphemeral(true).queue();

            return;
        }
        PlayTimer timer = new PlayTimer();
        String embedDesc = "";
        for (Player player : onlinePlayers) {
            String playtime = getPlayTime(timer.getPlaytime(player.getUniqueId()));
            String playerString = "**" + player.getName() + "**\n> Latency: `" + player.getPing() + "`ms\n> Has been playing for: " + playtime + "\n";
            if (player.getName().equals("sharkkk2")) {
                playerString = "**" + player.getName() + "**\n> Latency: `" + player.getPing() + "`ms\n> Has been playing for: " + playtime + "\n> Player is a sigma: **Yes**\n";
            }
            embedDesc += playerString;
        }

        int playerCount = Bukkit.getOnlinePlayers().size();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Online players")
                .setDescription("Total online players: **" + playerCount + "**\n" + embedDesc)
                .setColor(new Color(71, 230, 111));

        event.replyEmbeds(embed.build()).queue();
    }
}
