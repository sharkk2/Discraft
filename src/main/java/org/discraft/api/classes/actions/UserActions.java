package org.discraft.api.classes.actions;

import org.discraft.api.classes.DiscraftRole;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record UserActions(
        BiConsumer<String, Long> nicknamer,
        Consumer<String> sender
) {}
