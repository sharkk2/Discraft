package org.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.java.JavaPlugin;
import org.discraft.bot.Bot;
import org.bukkit.Bukkit;
import org.discraft.bot.core.handlers.MinecraftEventPoster;
import org.discraft.Listeners.MinecraftListener;

import java.awt.*;

public final class Discraft extends JavaPlugin {
    private Bot discordBot;
    private TextChannel channel;

    public void executeCommand(String command) {
        Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        discordBot = new Bot(this);
        discordBot.start();

        MinecraftEventPoster eventPoster = new MinecraftEventPoster(this, discordBot.getJda());
        Bukkit.getPluginManager().registerEvents(new MinecraftListener(eventPoster, this), this);
        eventPoster.Post(4, null, null);
        String rawchannelid = this.getConfig().getString("chat_channel");
        if (rawchannelid.isEmpty() || rawchannelid == null) {return;}
        channel = discordBot.getJda().getTextChannelById(rawchannelid);
    }

    @Override
    public void onDisable() {
        if (channel != null) {
            Color color = new Color(230, 75, 64);
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Server stopped ):")
                    .setColor(color);

            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }
}
