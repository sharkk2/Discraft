package org.discraft.bot.core.Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.Bukkit;
import org.discraft.bot.core.classes.SlashCommand;
import org.discraft.bot.core.handlers.Broadcaster;
import org.discraft.bot.core.handlers.Commands;
import org.discraft.Discraft;
import org.discraft.bot.core.loops.ConsoleReader;
import org.discraft.bot.core.loops.StatusUpdater;
import org.discraft.commands.discordmute;
import org.discraft.listeners.MinecraftListener;

public class ReadyListener extends ListenerAdapter {
    private final Discraft plugin;

    public ReadyListener(Discraft plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onReady(ReadyEvent event) {
    }
}