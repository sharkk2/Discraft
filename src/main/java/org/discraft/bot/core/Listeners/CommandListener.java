package org.discraft.bot.core.Listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.discraft.bot.core.classes.SlashCommand;
import org.discraft.bot.core.handlers.Commands;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();

        for (SlashCommand command : Commands.commandInstances) {
            if (command.getCommandData().getName().equals(commandName)) {
                command.execute(event);
                return;
            }
        }

        event.reply("Unknown command!").setEphemeral(true).queue();
    }
}

