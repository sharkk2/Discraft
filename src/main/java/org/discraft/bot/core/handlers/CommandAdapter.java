package org.discraft.bot.core.handlers;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discraft.Discraft;
import org.discraft.api.CommandContext;
import org.discraft.api.CommandOption;
import org.discraft.api.DiscordCommand;
import org.discraft.api.classes.DiscraftChannel;
import org.discraft.api.classes.DiscraftUser;
import org.discraft.bot.core.classes.SlashCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CommandAdapter implements SlashCommand {
    private final DiscordCommand command;


    private OptionType toOptionType(int type) {
        switch (type) {
            case CommandOption.INTEGER: return OptionType.INTEGER;
            case CommandOption.BOOLEAN: return OptionType.BOOLEAN;
            case CommandOption.STRING:
            default: return OptionType.STRING;
        }
    }


    public CommandAdapter(DiscordCommand command) {
        this.command = command;
    }

    @Override
    public SlashCommandData getCommandData() {
        SlashCommandData data = Commands.slash(command.getName(), command.getDescription());
        for (CommandOption p : command.getOptions()) {
            data.addOption(toOptionType(p.getType()), p.getName(), p.getName(), p.isRequired());
        }

        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event, Discraft plugin) {
        BiConsumer<List<CommandOption>, CommandContext> callback = command.getCallback();
        if (callback == null) return;
        List<CommandOption> answered = new ArrayList<>();
        for (CommandOption option : command.getOptions()) {
            OptionMapping optionMapping = event.getOption(option.getName());
            if (optionMapping == null) continue;
            Object answer = switch (option.getType()) {
                case CommandOption.INTEGER -> optionMapping.getAsInt();
                case CommandOption.BOOLEAN -> optionMapping.getAsBoolean();
                case CommandOption.STRING -> optionMapping.getAsString();
                default -> option.getAsString();
            };
            answered.add(option.answer(answer));
        }
        User jdauser = event.getUser();
        DiscraftUser user = new DiscraftUser(jdauser.getIdLong(), jdauser.getName(), jdauser.getAvatarUrl());
        DiscraftChannel channel = new DiscraftChannel(event.getChannel().getIdLong(), event.getChannel().getName(),message -> event.getChannel().sendMessage(message).queue(), plugin.getJDA());
        CommandContext context = new CommandContext(user, channel, event.getTimeCreated().toEpochSecond(),
          msg -> event.reply(msg).queue()
        );
        callback.accept(answered, context);
    }
}