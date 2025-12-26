package org.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.java.JavaPlugin;
import org.discraft.bot.Bot;
import org.bukkit.Bukkit;
import org.discraft.bot.core.handlers.Broadcaster;
import org.discraft.listeners.MinecraftListener;
import org.discraft.commands.discordmute;

import java.awt.*;

public final class Discraft extends JavaPlugin {
    private Bot discordBot;
    private TextChannel channel;
    private TextChannel console_channel;
    private static Discraft instance;

    public void executeCommand(String command) {
        Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    public static Discraft getDiscraft() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        discordBot = new Bot(this);
        discordBot.start();

        Broadcaster broadcaster = new Broadcaster(this, discordBot.getJda());
        Bukkit.getPluginManager().registerEvents(new MinecraftListener(broadcaster, this), this);

        getCommand("discordmute").setExecutor(new discordmute(this));
        broadcaster.Post(4, null, null);

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

    public long[] getMemoryUsage() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        return new long[]{usedMemory, maxMemory};
    }

    public String getMemoryUsageFormatted() {
        long[] mem = getMemoryUsage();
        return String.format("%d MB / %d MB",
                mem[0] / (1024 * 1024),
                mem[1] / (1024 * 1024));
    }
}
