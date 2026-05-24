package org.discraft;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.discraft.api.CommandContext;
import org.discraft.api.CommandOption;
import org.discraft.api.DiscordCommand;
import org.discraft.api.DiscraftAPI;
import org.discraft.api.classes.DiscraftChannel;
import org.discraft.api.classes.DiscraftUser;
import org.discraft.bot.Bot;
import org.bukkit.Bukkit;
import org.discraft.bot.core.handlers.Broadcaster;
import org.discraft.bot.core.handlers.CommandAdapter;
import org.discraft.bot.core.handlers.Commands;
import org.discraft.listeners.MinecraftListener;
import org.discraft.commands.discordmute;

import java.awt.Color;
import java.util.List;
import java.util.function.BiConsumer;

public final class Discraft extends JavaPlugin implements DiscraftAPI{
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
        getServer().getServicesManager().register(DiscraftAPI.class, this,this, ServicePriority.Normal);
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

    @Override
    public void registerCommand(String name, String description, List<CommandOption> options, BiConsumer<List<CommandOption>, CommandContext> callback) {
        DiscordCommand command = new DiscordCommand() {
            @Override public String getName() { return name; }
            @Override public String getDescription() { return description; }
            @Override public List<CommandOption> getOptions() { return options; }
            @Override public BiConsumer<List<CommandOption>, CommandContext> getCallback() { return callback; }
        };
        Commands.registerCommand(this, new CommandAdapter(command));
    }

    public JDA getJDA() {return discordBot.getJda();}

    @Override
    public DiscraftChannel getChannel(long id) {
        TextChannel channel = discordBot.getJda().getTextChannelById(id);
        if (channel == null) {throw new IllegalArgumentException("No channel found with id: " + id);}
        return new DiscraftChannel(channel.getIdLong(), channel.getName(), message -> channel.sendMessage(message).queue(), getJDA());
    }

    @Override
    public DiscraftUser getUser(long id) {
        User user = discordBot.getJda().getUserById(id);
        if (user == null) {throw new IllegalArgumentException("No user found with id: " + id);}
        return new DiscraftUser(user.getIdLong(), user.getName(), user.getAvatarUrl());
    }


}
