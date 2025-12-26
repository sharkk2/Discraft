package org.discraft.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.discraft.bot.core.classes.SlashCommand;
import org.discraft.Discraft;
import org.discraft.PlayTimer;

import java.awt.*;
import java.util.List;

public class players implements SlashCommand {
    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("players", "Shows all the current online players");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, Discraft discraft) {
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
            String playtime = timer.getPlayTimeFormatted(player.getUniqueId());
            String playerString = "**" + player.getName() + "**: `latency: " + player.getPing() + "ms - play time: " + playtime + "`\n";
            if (player.getName().equals("sharkkk2")) {
                playerString = "**" + player.getName() + "**: `latency: " + player.getPing() + "ms - play time: " + playtime + " - sigma gigachad? yes`\n";
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
