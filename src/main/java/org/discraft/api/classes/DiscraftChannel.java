package org.discraft.api.classes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.discraft.api.classes.actions.ChannelActions;
import org.discraft.api.classes.actions.MessageActions;
import org.discraft.api.classes.actions.UserActions;
import org.discraft.bot.core.handlers.CommandAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DiscraftChannel {
    private final long id;
    private final long guildID;
    private final String name;
    private final ChannelActions actions;
    private final JDA jda;
    public enum CHANNELTYPE {
            TEXT, VOICE, FORUM
    };

    public DiscraftChannel(long id, long guildID, String name, ChannelActions actions, JDA jda) {
        this.id = id;
        this.guildID = guildID;
        this.name = name;
        this.jda = jda;
        this.actions = actions;
    }

    public long getID() {return id;}
    public long getGuildID() {return guildID;}
    public String getName() {return name;}
    public void sendMessage(String message) {actions.sender().accept(message);}

    public CompletableFuture<DiscraftMessage> getMessage(long messageID) {
        TextChannel channel = jda.getTextChannelById(id);
        if (channel == null) return CompletableFuture.failedFuture(new IllegalStateException("Channel " + id + " not found"));
        CompletableFuture<DiscraftMessage> future = new CompletableFuture<>();
        channel.retrieveMessageById(messageID).queue(
                message -> {
                    DiscraftUser author = new DiscraftUser(message.getAuthor().getIdLong(), message.getAuthor().getName(), message.getAuthor().getAvatarUrl(), DiscraftUser._bakeActions(message.getAuthor()));
                    future.complete(new DiscraftMessage(
                            messageID, this, author,
                            message.getContentRaw(),
                            message.getTimeCreated().toEpochSecond(),
                            DiscraftMessage._bakeActions(message)
                    ));
                },
                future::completeExceptionally
        );
        return future;
    }

    public static ChannelActions _bakeActions(MessageChannelUnion channel) {
        return new ChannelActions(message -> channel.sendMessage(message).queue());
    }

    public static ChannelActions _bakeActions(TextChannel channel) {
        return new ChannelActions(message -> channel.sendMessage(message).queue());
    }
}
