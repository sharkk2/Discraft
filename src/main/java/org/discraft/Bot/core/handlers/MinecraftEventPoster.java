package org.discraft.Bot.core.handlers;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.discraft.Discraft;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.JDA;
import org.bukkit.entity.Player;

import java.awt.*;

public class MinecraftEventPoster {
    private final Discraft plugin;
    private final JDA jda;

    public MinecraftEventPoster(Discraft plugin, JDA jda) {
        this.plugin = plugin;
        this.jda = jda;
    }

    public void Post(int etype, String message, Player player) {
        String rawchannelid = plugin.getConfig().getString("chat_channel");
        boolean PlayerJoin = plugin.getConfig().getBoolean("player_join", true);
        boolean PlayerLeave = plugin.getConfig().getBoolean("player_leave", true);
        boolean PlayerDeath = plugin.getConfig().getBoolean("player_death", true);
        boolean PlayerAdvancements = plugin.getConfig().getBoolean("player_advancements", true);
        boolean ServerOn = plugin.getConfig().getBoolean("server_on", true);
        boolean ServerOff = plugin.getConfig().getBoolean("server_off", true);
        boolean chat = plugin.getConfig().getBoolean("chat_to_discord", true);
        if (rawchannelid == null || rawchannelid.isEmpty()) {
            return;
        }

        long channelID = Long.parseLong(rawchannelid);

        String guid = plugin.getConfig().getString("guild_id");

        if (guid == null || guid.isEmpty()) {
            return;
        }

        Guild guild = jda.getGuildById(guid);
        if (guild != null) {
            TextChannel channel = guild.getTextChannelById(channelID);
            if (channel != null) {
                /*
                  Type 0: Normal chat message
                  Type 1: Join chat message
                  Type 2: Leave chat message
                  Type 3: Advancement chat message
                  Type 4: Server ON
                  Type 5: Server OFF
                  Type 6: Death
                 */
                switch (etype) {

                    case 0:
                        if (chat != true) {return;}
                        String msg = "**(" + player.getName() + ")**: " + message;
                        if (msg.length() > 2000) {
                            return;
                        }
                        channel.sendMessage(msg).queue();
                        break;
                    case 1:
                        if (PlayerJoin != true) {return;}
                        Color color = new Color(230, 216, 71);
                        EmbedBuilder embed = new EmbedBuilder()
                                .setAuthor(player.getName(), null, "https://mineskin.eu/helm/" + player.getName())
                                .setTitle(message)
                                .setColor(color);

                        channel.sendMessageEmbeds(embed.build()).queue();
                        break;
                    case 2:
                        if (PlayerLeave != true) {return;}
                        Color color2 = new Color(230, 75, 64);
                        EmbedBuilder embed2 = new EmbedBuilder()
                                .setAuthor(player.getName(), null, "https://mineskin.eu/helm/" + player.getName())
                                .setTitle(message)
                                .setColor(color2);

                        channel.sendMessageEmbeds(embed2.build()).queue();
                        break;
                    case 3:
                        if (PlayerAdvancements != true) {return;}
                        Color color3 = new Color(243, 130, 47);
                        EmbedBuilder embed3 = new EmbedBuilder()
                                .setAuthor(player.getName(), null, "https://mineskin.eu/helm/" + player.getName())
                                .setTitle(message)
                                .setColor(color3);

                        channel.sendMessageEmbeds(embed3.build()).queue();
                        break;
                    case 4:
                        if (ServerOn != true) {return;}
                        Color color4 = new Color(71, 230, 111);
                        EmbedBuilder embed4 = new EmbedBuilder()
                                .setAuthor("Server is online ✅")
                                .setColor(color4);

                        channel.sendMessageEmbeds(embed4.build()).queue();

                        String consoleid = plugin.getConfig().getString("console_channel");
                        if (!consoleid.isEmpty() && consoleid != null) {
                            TextChannel console = jda.getTextChannelById(consoleid);
                            console.getManager().setTopic("Console is online! | Minecraft version: " + Bukkit.getServer().getVersion() + " | Bukkit " + Bukkit.getBukkitVersion()).queue();
                        }
                        break;
                    case 5:
                        if (ServerOff != true) {return;}
                        Color color5 = new Color(230, 75, 64);
                        EmbedBuilder embed5 = new EmbedBuilder()
                                .setAuthor("Server stopped ):")
                                .setColor(color5);

                        channel.sendMessageEmbeds(embed5.build()).queue();
                        break;
                    case 6:
                        if (PlayerDeath != true) {return;}
                        Color color6 = new Color(89, 84, 84);
                        EmbedBuilder embed6 = new EmbedBuilder()
                                .setAuthor(player.getName(), null, "https://mineskin.eu/helm/" + player.getName())
                                .setTitle(message)
                                .setColor(color6);

                        channel.sendMessageEmbeds(embed6.build()).queue();
                        break;
                    case 7:  // special challenge advancement type "i should have just made the color a parameter"
                        if (PlayerAdvancements != true) {return;}
                        Color color7 = new Color(118, 67, 220);
                        EmbedBuilder embed7 = new EmbedBuilder()
                                .setAuthor(player.getName(), null, "https://mineskin.eu/helm/" + player.getName())
                                .setTitle(message)
                                .setColor(color7);

                        channel.sendMessageEmbeds(embed7.build()).queue();
                        break;

                }
            }
        }

        

    }
}
