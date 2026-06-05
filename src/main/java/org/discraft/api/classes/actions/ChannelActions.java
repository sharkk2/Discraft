package org.discraft.api.classes.actions;

import java.util.function.Consumer;

public record ChannelActions(
       Consumer<String> sender
) {}
