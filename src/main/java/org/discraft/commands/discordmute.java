package org.discraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.discraft.Discraft;
import org.jetbrains.annotations.NotNull;
import org.discraft.PluginData;
import org.json.JSONObject;

import java.util.UUID;

public class discordmute implements CommandExecutor {
    private final Discraft discraft;
    private PluginData dataManager;

    public discordmute(Discraft discraftp) {
        this.discraft = discraftp;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            dataManager = new PluginData(discraft);
            JSONObject playerData = dataManager.initPlayer(playerUUID);

            boolean muteState = playerData.optBoolean("discordMute", false);
            playerData.put("discordMute", !muteState);
            dataManager.savePlayer(playerUUID, playerData);

            player.sendMessage("Discord messages mute has been turned: " + (!muteState ? "§aON":"§cOFF"));
            return true;
        }
        return false;
    }
}
