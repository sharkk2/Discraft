package org.discraft.bot.commands;

import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.discraft.bot.core.classes.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import java.awt.Color;

public class ping implements SlashCommand {
    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("ping", "Returns bot latency");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        double[] tpsRaw = Bukkit.getServer().getTPS();
        String tps = String.format("`%.2f`", tpsRaw[0]);
        int playerCount = Bukkit.getOnlinePlayers().size();
        Color color = new Color(71, 230, 111);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Service latency")
                .setDescription("**Pong! 🏓**\n> Discord bot latency: `" + event.getJDA().getGatewayPing() + "`ms\n> Minecraft TPS: `" + tps + "` (`" + playerCount + "` players)")
                .setColor(color);


        event.replyEmbeds(embed.build()).queue();
    }
}
