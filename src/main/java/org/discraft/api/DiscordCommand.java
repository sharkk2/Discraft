package org.discraft.api;

import java.util.List;
import java.util.function.BiConsumer;


public interface DiscordCommand {
    String getName();
    String getDescription();
    List<CommandOption> getOptions();
    BiConsumer<List<CommandOption>, CommandContext> getCallback();
}
