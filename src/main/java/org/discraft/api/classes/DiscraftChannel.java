package org.discraft.api.classes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.util.function.Consumer;

public class DiscraftChannel {
    private final long id;
    private final String name;
    private final Consumer<String> sender;
    private final JDA jda;

    public DiscraftChannel(long id, String name, Consumer<String> sender, JDA jda) {
        this.id = id;
        this.name = name;
        this.sender = sender;
        this.jda = jda;
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void sendMessage(String message) {
        sender.accept(message);
    }

    public void getMessage(long messageID, Consumer<DiscraftMessage> callback) {
        TextChannel channel = jda.getTextChannelById(id);
        if (channel != null) {
            channel.retrieveMessageById(messageID).queue(message -> {
                DiscraftUser author = new DiscraftUser(message.getAuthor().getIdLong(), message.getAuthor().getName(), message.getAuthor().getAvatarUrl());
                DiscraftMessage dmsg = new DiscraftMessage(
                        messageID,
                        this,
                        author,
                        message.getContentRaw(),
                        message.getTimeCreated().toEpochSecond(),
                        () -> message.delete().queue(),
                        emoji -> {message.addReaction(Emoji.fromUnicode(emoji)).queue();},
                        replyText -> {message.reply(replyText).queue();});
                callback.accept(dmsg);
            });
        }
    }
}
