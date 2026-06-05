package org.discraft.api.classes.actions;

import org.discraft.api.classes.DiscraftRole;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record MemberActions(
        Consumer<String> nicknamer,
        Consumer<String> sender,
        Consumer<DiscraftRole> roleAdder,
        Consumer<DiscraftRole> roleRemover
) {}
