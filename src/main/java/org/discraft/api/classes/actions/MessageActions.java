package org.discraft.api.classes.actions;

import java.util.function.Consumer;

public record MessageActions(
        Runnable deleter,
        Consumer<String> reactor,
        Consumer<String> dereactor,
        Consumer<String> replier
) {}