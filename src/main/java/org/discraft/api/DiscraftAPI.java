package org.discraft.api;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.discraft.api.classes.DiscraftChannel;
import org.discraft.api.classes.DiscraftMember;
import org.discraft.api.classes.DiscraftUser;

import java.util.List;
import java.util.function.BiConsumer;

public interface DiscraftAPI {
    void registerCommand(String name, String description, List<CommandOption> options, BiConsumer<List<CommandOption>, CommandContext> callback);
    static DiscraftAPI get() {
        RegisteredServiceProvider<DiscraftAPI> provider = Bukkit.getServicesManager().getRegistration(DiscraftAPI.class);
        if (provider == null) {
            throw new IllegalStateException("Discraft is not loaded.");
        }
        return provider.getProvider();
    }

    DiscraftChannel getChannel(long id);
    DiscraftUser getUser(long id);
    DiscraftMember getMember(long guildID, long userID);

}