package org.discraft.Bot.core.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.awt.*;

public class ModalListener extends ListenerAdapter {
    public boolean isValidNumber(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().startsWith("teleport-")) {
            String xs = event.getValue("x").getAsString();
            String ys = event.getValue("y").getAsString();
            String zs = event.getValue("z").getAsString();
            String playerName = event.getModalId().replace("teleport-", "");

            Player player = Bukkit.getPlayer(playerName);

            if (!isValidNumber(xs) || !isValidNumber(ys) || !isValidNumber(zs)) {
                Color ccolor = new Color(230, 75, 64);
                EmbedBuilder eembed = new EmbedBuilder()
                        .setDescription("❌ | Enter valid x,y,z values")
                        .setColor(ccolor);

                event.replyEmbeds(eembed.build()).setEphemeral(true).queue();
            }

            int x = Integer.parseInt(xs);
            int y = Integer.parseInt(ys);
            int z = Integer.parseInt(zs);

            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("Discraft"), () -> { // same comment in SelectMenuListener
                Location location = new Location(player.getWorld(), x, y, z);
                player.teleport(location);

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Action success")
                        .setColor(new Color(71, 230, 111))
                        .setDescription("**" + playerName + "**" + " has been teleported to X:`" + x + "` Y:`" + y + "` Z:`" + z + "`");

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
            });
        }
    }
}