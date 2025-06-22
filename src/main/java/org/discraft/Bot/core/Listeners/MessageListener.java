package org.discraft.Bot.core.Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

import net.dv8tion.jda.api.entities.Message;
import org.bukkit.entity.Player;
import org.discraft.Discraft;
import org.bukkit.Bukkit;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.discraft.Filter;
import org.discraft.PluginData;
import org.json.JSONObject;

public class MessageListener extends ListenerAdapter {
    private final Discraft plugin;
    private final JDA jda;

    public MessageListener(Discraft plugin, JDA jda) {
        this.plugin = plugin;
        this.jda = jda;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message message = event.getMessage();
        if (message.getContentRaw().startsWith("!!")) {return;}
        String channelid = plugin.getConfig().getString("chat_channel");
        String consoleid = plugin.getConfig().getString("console_channel");
        boolean reactOnSuccess = plugin.getConfig().getBoolean("reactOnSuccess", true);
        boolean sendToMc = plugin.getConfig().getBoolean("discord_to_minecraft", true);
        boolean shouldFilter = !plugin.getConfig().getBoolean("allow_profanity", false);

        if (!sendToMc) {
            return;
        }

        if (channelid == null || channelid.isEmpty()) {
            return;
        }

        long channelID = Long.parseLong(channelid);

        String guid = plugin.getConfig().getString("guild_id");

        if (guid == null || guid.isEmpty()) {
            return;
        }

        Guild guild = jda.getGuildById(guid);
        if (guild != null) {
            TextChannel channel = guild.getTextChannelById(channelID);
            if (channel != null) {
               if (message.getChannelId().equals(channel.getId())) {
                   String discordMessage = message.getContentRaw();
                   String filtered = discordMessage;
                   Filter filter = new Filter();

                   if (shouldFilter) {
                       filtered = filter.censor(discordMessage);
                   }

                   if (message.getMessageReference() != null) {
                       Message refMessage = message.getReferencedMessage();
                       Broadcast("§8[Discord] §9" + message.getAuthor().getName() + " §7(replying to " + refMessage.getAuthor().getName() + ")§r: " + filtered);
                       message.addReaction(Emoji.fromUnicode("📩")).queue();
                   } else {
                       Broadcast("§8[Discord] §9" + message.getAuthor().getName() + "§r: " + filtered);

                   }

                   if (reactOnSuccess) {
                       message.addReaction(Emoji.fromUnicode("📩")).queue();
                       if (shouldFilter) {
                           List<String> flagged = filter.getFlagged(discordMessage);
                           if (!flagged.isEmpty()) {message.addReaction(Emoji.fromUnicode("🤡")).queue();}
                       } // FUCK ".queue()"
                   }

               } else if (message.getChannelId().equals(consoleid)) {
                   List<String> admins = plugin.getConfig().getStringList("admins");
                   if (!admins.contains(message.getAuthor().getId())) {
                       message.addReaction(Emoji.fromUnicode("❌")).queue();
                       return;
                   }
                   String command = message.getContentRaw();
                   plugin.executeCommand(command);
               }
            }
        }
    }

    private void Broadcast(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PluginData dataManager = new PluginData(plugin);
            JSONObject playerData = dataManager.initPlayer(player.getUniqueId());

            if (!playerData.getBoolean("discordMute")) {
                player.sendMessage(message);
            }
        }
        Bukkit.getLogger().info("[Discraft] Broadcast: " + message);
    }
}
