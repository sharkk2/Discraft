package org.discraft.api.classes;

import java.util.function.Consumer;

public class DiscraftMessage {
    private final long id;
    private final DiscraftChannel channel;
    private final DiscraftUser author;
    private final String content;
    private final long timeCreated;
    private final Runnable deleter;
    private final Consumer<String> reactor;
    private final Consumer<String> replier;

    public DiscraftMessage(
            long id,
            DiscraftChannel channel, DiscraftUser author,
            String content,
            long timeCreated,
            Runnable deleter, Consumer<String> reactor, Consumer<String> replier
    ) {
        this.id = id;
        this.channel = channel;
        this.author = author;
        this.content = content;
        this.timeCreated = timeCreated;
        this.deleter = deleter; this.reactor = reactor; this.replier = replier;
    }

    public long getID() {return id;}
    public DiscraftChannel getChannel() {return channel;}
    public DiscraftUser getAuthor() {return author;}
    public String getContent() {return content;}
    public long getTimeCreated() {return timeCreated;}
    public void delete() {deleter.run();}
    public void react(String emoji) {reactor.accept(emoji);}
    public void reply(String message) {replier.accept(message);}
}
