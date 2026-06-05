package org.discraft.bot.core.handlers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.discraft.Discraft;
import org.discraft.api.CommandContext;
import org.discraft.api.CommandOption;
import org.discraft.api.CommandOption.OPTION_TYPE;
import org.discraft.api.DiscordCommand;
import org.discraft.api.classes.DiscraftChannel;
import org.discraft.api.classes.DiscraftMessage;
import org.discraft.api.classes.DiscraftRole;
import org.discraft.api.classes.DiscraftUser;
import org.discraft.api.classes.actions.ChannelActions;
import org.discraft.api.classes.actions.UserActions;
import org.discraft.bot.core.classes.SlashCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CommandAdapter implements SlashCommand {
    private final DiscordCommand command;


    public static OptionType toOptionType(CommandOption.OPTION_TYPE type) {
        switch (type) {
            case OPTION_TYPE.INTEGER: return OptionType.INTEGER;
            case OPTION_TYPE.BOOLEAN: return OptionType.BOOLEAN;
            case OPTION_TYPE.STRING: return OptionType.STRING;
            case OPTION_TYPE.CHANNEL:return OptionType.CHANNEL;
            case OPTION_TYPE.USER: return OptionType.USER;
            case OPTION_TYPE.ROLE: return OptionType.ROLE;
            default: return OptionType.STRING;
        }
    }

    public static DiscraftRole toDiscraftRole(Role role) {
        return new DiscraftRole(role.getIdLong(), role.getGuild().getIdLong(), role.getName(), role.getColorRaw(), role.getPermissionsRaw(), role.getPositionRaw(), DiscraftRole._bakeActions(role));
    }


    public CommandAdapter(DiscordCommand command) {
        this.command = command;
    }

    @Override
    public SlashCommandData getCommandData() {
        SlashCommandData data = Commands.slash(command.getName(), command.getDescription());
        for (CommandOption p : command.getOptions()) {
            data.addOption(toOptionType(p.getType()), p.getName(), p.getDescription(), p.isRequired());
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
                case OPTION_TYPE.INTEGER -> optionMapping.getAsInt();
                case OPTION_TYPE.BOOLEAN -> optionMapping.getAsBoolean();
                case OPTION_TYPE.STRING -> optionMapping.getAsString();
                case OPTION_TYPE.CHANNEL -> {
                    GuildChannelUnion gcu = optionMapping.getAsChannel();
                    yield new DiscraftChannel(gcu.getIdLong(), gcu.getGuild().getIdLong(), gcu.getName(), DiscraftChannel._bakeActions(gcu.asTextChannel()), plugin.getJDA());
                }
                case OPTION_TYPE.ROLE -> toDiscraftRole(optionMapping.getAsRole());
                case OPTION_TYPE.USER -> {
                    User user = optionMapping.getAsUser();
                    UserActions userActions = DiscraftUser._bakeActions(user);
                    yield new DiscraftUser(user.getIdLong(), user.getName(), user.getAvatarUrl(), userActions);
                }
                default -> option.getAsString();
            };
            answered.add(option.answer(answer));
        }
        User jdauser = event.getUser();
        UserActions userActions = DiscraftUser._bakeActions(jdauser);
        DiscraftUser user = new DiscraftUser(jdauser.getIdLong(), jdauser.getName(), jdauser.getAvatarUrl(), userActions);
        DiscraftChannel channel = new DiscraftChannel(event.getChannel().getIdLong(), event.isFromGuild() ? event.getGuild().getIdLong() : -1L, event.getChannel().getName(), DiscraftChannel._bakeActions(event.getChannel()), plugin.getJDA());
        CommandContext context = new CommandContext(user, channel, event.getTimeCreated().toEpochSecond(),
                (msg, ephemeral) -> event.reply(msg).setEphemeral(ephemeral).queue()
        );
        callback.accept(answered, context);
    }
}