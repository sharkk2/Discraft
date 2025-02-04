package org.discraft.bot.core.Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.Message;
import org.discraft.Discraft;
import org.bukkit.Bukkit;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class MessageListener extends ListenerAdapter {
    private final Discraft plugin;
    private JDA jda;

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

        if (sendToMc != true) {
            return;
        }

        if (channelid == null || channelid.isEmpty()) {
            return;
        }

        TextChannel consolechannel = null;
        if (!consoleid.isEmpty() || consoleid != null) {
            consolechannel = jda.getTextChannelById(consoleid);
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
                   String mcMessage = "";
                   // if its a reply:
                   if (message.getMessageReference() != null) {
                       Message refMessage = message.getReferencedMessage();
                       Bukkit.broadcastMessage("§8[Discord] §9" + message.getAuthor().getName() + " §7(replying to " + refMessage.getAuthor().getName() + ")§r: " + message.getContentRaw());
                       message.addReaction(Emoji.fromUnicode("📩")).queue();
                       if (reactOnSuccess) {
                           message.addReaction(Emoji.fromUnicode("📩")).queue();
                       }
                   } else {
                       Bukkit.broadcastMessage("§8[Discord] §9" + message.getAuthor().getName() + "§r: " + message.getContentRaw());
                       if (reactOnSuccess) {
                           message.addReaction(Emoji.fromUnicode("📩")).queue();
                       }

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
}
