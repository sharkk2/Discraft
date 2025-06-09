package org.discraft.Bot.core.loops;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class StatusUpdater {
    private JavaPlugin plugin;
    private JDA jda;
    private int last_count = -1;

    public StatusUpdater(JavaPlugin plugin, JDA jda) {
        this.plugin = plugin;
        this.jda = jda;
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int playerCount = Bukkit.getOnlinePlayers().size();

                if (playerCount == last_count) {
                    return;
                }

                String status = plugin.getConfig().getString("status", "").trim();

                if (status.isEmpty()) {
                    status = playerCount + " Player(s)";
                }
                last_count = playerCount;

                int activityType = plugin.getConfig().getInt("activity", 0);

                Activity activity = getActivity(status, activityType);
                updateStatus(activity);
            }
        }.runTaskTimer(plugin, 0L, 200L);
    }

    private void updateStatus(Activity activity) {
        if (jda != null && !jda.getStatus().equals(JDA.Status.SHUTDOWN)) {
            jda.getPresence().setActivity(activity);
            Bukkit.getLogger().info("[Discraft] Updated " + jda.getSelfUser().getName() + " status to: " + activity.getName());
        }
    }

    private Activity getActivity(String status, int activityType) {
        switch (activityType) {
            case 0:
                return Activity.playing(status);
            case 1:
                return Activity.listening(status);
            case 2:
                return Activity.watching(status);
            default:
                return Activity.playing(status);
        }
    }
}
