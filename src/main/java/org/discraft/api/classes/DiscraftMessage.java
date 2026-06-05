package org.discraft.api.classes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import org.discraft.api.classes.actions.MessageActions;

import java.util.function.Consumer;

public class DiscraftMessage {
    private final long id;
    private final DiscraftChannel channel;
    private final DiscraftUser author;
    private final String content;
    private final long timeCreated;
    private final MessageActions actions;


    public DiscraftMessage(
            long id,
            DiscraftChannel channel, DiscraftUser author,
            String content,
            long timeCreated,
            MessageActions actions
    ) {
        this.id = id;
        this.channel = channel;
        this.author = author;
        this.content = content;
        this.timeCreated = timeCreated;
        this.actions = actions;
    }

    public long getID() {return id;}
    public DiscraftChannel getChannel() {return channel;}
    public DiscraftUser getAuthor() {return author;}
    public String getContent() {return content;}
    public long getTimeCreated() {return timeCreated;}
    public void delete() {actions.deleter().run();}
    public void addReaction(String emoji) {actions.reactor().accept(emoji);}
    public void removeReaction(String emoji) {actions.dereactor().accept(emoji);}
    public void reply(String message) {actions.replier().accept(message);}

    public static MessageActions _bakeActions(Message message) {
        return new MessageActions(
                () -> message.delete().queue(),
                emoji -> message.addReaction(Emoji.fromUnicode(emoji)).queue(),
                emoji -> message.removeReaction(Emoji.fromUnicode(emoji)).queue(),
                reply -> message.reply(reply).queue()
        );
    }
}
