package org.discraft.bot.core.loops;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.dv8tion.jda.api.JDA;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ConsoleReader {
    private JavaPlugin plugin;
    private JDA jda;
    private String lastLine = "";
    private Message lastMessage;
    private String rawContent = "";
    private boolean firstBlock = true;

    public ConsoleReader(JavaPlugin plugin, JDA jda) {
        this.plugin = plugin;
        this.jda = jda;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Path logFile = Bukkit.getServer().getWorldContainer().toPath().resolve("logs/latest.log");
                String channelId = plugin.getConfig().getString("console_channel");
                if (channelId.isEmpty() || channelId == null) {
                    return;
                }

                try (Stream<String> lines = Files.lines(logFile)) {
                    String latestLine = lines.reduce((first, second) -> second).orElse(null);

                    if (latestLine != null && !latestLine.equals(lastLine)) {
                        lastLine = latestLine;
                        TextChannel channel = jda.getTextChannelById(channelId);

                        if (channel == null) return;

                        if (lastMessage != null && !rawContent.isEmpty()) {
                            if ((rawContent.length() + latestLine.length()) < 1994) {
                                rawContent += "\n" + latestLine;
                                String newMessage;
                                if (firstBlock) {
                                    newMessage = "**Console start**```" + rawContent + "```";
                                } else {
                                    newMessage = "```" + rawContent + "```";
                                }

                                lastMessage.editMessage(newMessage).queue(sentMessage -> {
                                    lastMessage = sentMessage;
                                });

                            } else {
                                channel.sendMessage("```" + latestLine + "```").queue(sentMessage -> {
                                    lastMessage = sentMessage;
                                    rawContent = latestLine;
                                    firstBlock = false;
                                });
                            }
                        } else {
                            channel.sendMessage("**Console start**\n```" + latestLine + "```").queue(sentMessage -> {
                                lastMessage = sentMessage;
                                rawContent = latestLine;
                            });
                        }
                    }
                } catch (IOException e) {
                    plugin.getLogger().warning("Failed to read latest.log: " + e.getMessage());
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

}
