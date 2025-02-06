package org.discraft.Bot.core.classes;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface SlashCommand {
    SlashCommandData getCommandData();
    void execute(net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent event);
}
