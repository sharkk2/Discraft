package org.discraft.api;

import org.discraft.api.classes.DiscraftChannel;
import org.discraft.api.classes.DiscraftUser;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommandContext {

    private final DiscraftUser user;
    private final DiscraftChannel channel;
    private final long interactionTime;
    private final BiConsumer<String, Boolean> responder;

    public CommandContext(DiscraftUser user, DiscraftChannel channel, long interactionTime, BiConsumer<String, Boolean> responder) {
        this.user = user;
        this.channel = channel;
        this.interactionTime = interactionTime;
        this.responder = responder;
    }

    public DiscraftUser getUser() { return user; }
    public DiscraftChannel getChannel() { return channel; }
    public long getInteractionTime() { return interactionTime; }
    public void respond(String message, boolean ephemeral) {responder.accept(message, ephemeral);}

}
