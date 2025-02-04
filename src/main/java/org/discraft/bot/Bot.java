package org.discraft.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.discraft.bot.core.Listeners.MessageListener;
import org.discraft.bot.core.loops.ConsoleReader;
import org.discraft.bot.core.loops.StatusUpdater;
import org.bukkit.Bukkit;
import org.discraft.bot.core.handlers.Commands;
import org.discraft.bot.core.Listeners.CommandListener;
import org.discraft.Discraft;

public class Bot {
   private JDA jda;
   private final Discraft plugin;

   public Bot(Discraft plugin) {
      this.plugin = plugin;
   }

   public void start() {
      try {
         String token = plugin.getConfig().getString("token");
         if (token == null || token.isEmpty()) {
            Bukkit.getLogger().warning("[Discraft] nigga Just put a token in config.yml");
            return;
         }



         jda = JDABuilder.createDefault(token)
                 .enableIntents(
                         GatewayIntent.GUILD_MESSAGES,
                         GatewayIntent.GUILD_MESSAGES,
                         GatewayIntent.MESSAGE_CONTENT,
                         GatewayIntent.GUILD_MEMBERS
                 )
                 .build();

         jda.awaitReady();
         Bukkit.getLogger().info("[Discraft] Logged into Discord as " + jda.getSelfUser().getName());
         Commands.registerCommands(jda);
         jda.addEventListener(new CommandListener());
         jda.addEventListener(new MessageListener(plugin, jda));

         new StatusUpdater(plugin, jda).start();
         new ConsoleReader(plugin, jda).start();
      } catch (Exception e) {
         Bukkit.getLogger().warning("[Discraft] Failed to create a bot instance: " + e.getMessage());
      }
   }

   public JDA getJda() {
        return jda;
   }

   public void stopBot() {
      if (jda != null) {
         jda.shutdown();
         Bukkit.getLogger().info("[Discraft] Discord bot stopped.");
      }
   }
}
