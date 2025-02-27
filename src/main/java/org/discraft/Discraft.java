package org.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.java.JavaPlugin;
import org.discraft.Bot.Bot;
import org.bukkit.Bukkit;
import org.discraft.Bot.core.handlers.MinecraftEventPoster;
import org.discraft.Listeners.MinecraftListener;
import org.discraft.commands.discordmute;

import java.awt.*;

public final class Discraft extends JavaPlugin {
    private Bot discordBot;
    private TextChannel channel;
    private TextChannel console_channel;

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

        getCommand("discordmute").setExecutor(new discordmute(this));

        eventPoster.Post(4, null, null);

        String rawchannelid = this.getConfig().getString("chat_channel");
        String rawconsoleid = this.getConfig().getString("console_channel");
        if (rawchannelid.isEmpty() || rawchannelid == null) {return;}
        channel = discordBot.getJda().getTextChannelById(rawchannelid);
        if (rawconsoleid.isEmpty() || rawconsoleid == null) {return;}
        console_channel = discordBot.getJda().getTextChannelById(rawconsoleid);

    }

    @Override
    public void onDisable() {
        if (channel != null) {
            Color color = new Color(230, 75, 64);
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor("Server stopped ):")
                    .setColor(color);

            channel.sendMessageEmbeds(embed.build()).queue();
            console_channel.getManager().setTopic("Console is offline | Minecraft version: " + Bukkit.getServer().getVersion() + " | Bukkit: " + Bukkit.getBukkitVersion()).queue();
        }
    }
}
