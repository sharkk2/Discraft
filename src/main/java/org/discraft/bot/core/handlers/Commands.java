package org.discraft.bot.core.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.discraft.bot.core.classes.SlashCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Commands {
    public static List<SlashCommand> commandInstances = new ArrayList<>();

    public static void registerCommands(JDA jda) {
        try {
            Reflections reflections = new Reflections("org.discraft.bot.commands");
            Set<Class<? extends SlashCommand>> classes = reflections.getSubTypesOf(SlashCommand.class);

            List<SlashCommandData> commandDataList = new ArrayList<>();

            for (Class<? extends SlashCommand> nigga: classes) {
                SlashCommand command = nigga.getDeclaredConstructor().newInstance();
                commandInstances.add(command);
                SlashCommandData commandData = command.getCommandData();
                commandDataList.add(commandData);
                Bukkit.getLogger().info("[Discraft] Registered /" + commandData.getName());
            }

            jda.updateCommands().addCommands(commandDataList).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
