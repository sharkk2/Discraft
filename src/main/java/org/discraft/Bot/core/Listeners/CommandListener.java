package org.discraft.Bot.core.Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.discraft.Bot.core.classes.SlashCommand;
import org.discraft.Bot.core.handlers.Commands;
import org.discraft.Discraft;

public class CommandListener extends ListenerAdapter {
    private final Discraft plugin;

    public CommandListener(Discraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();

        for (SlashCommand command : Commands.commandInstances) {
            if (command.getCommandData().getName().equals(commandName)) {
                command.execute(event, plugin); // pass the event and disccraft
                return;
            }
        }
        event.reply("Unknown command!").setEphemeral(true).queue();
    }
}
