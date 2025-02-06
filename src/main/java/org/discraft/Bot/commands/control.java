package org.discraft.Bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.discraft.Bot.core.classes.SlashCommand;
import java.awt.*;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.discraft.PlayTimer;
import org.json.JSONObject;
import org.discraft.Discraft;


public class control implements SlashCommand {
    public static String getCountryByIP(String ip) {
        try {
            URL url = new URL("https://freeipapi.com/api/json/" + ip);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());
            if (json.getString("status").equals("success")) {
                return json.getString("countryCode");
            } else {
                return "unknown";
            }
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getPlayTime(long joinTime) {
        long currentTime = System.currentTimeMillis();
        long playTimeMillis = currentTime - joinTime;

        long seconds = (playTimeMillis / 1000) % 60;
        long minutes = (playTimeMillis / (1000 * 60)) % 60;
        long hours = (playTimeMillis / (1000 * 60 * 60)) % 24;


        return (hours > 0 ? "`" + hours + "`h " : "") +
                (minutes > 0 ? "`" + minutes + "`m " : "") +
                "`" + seconds + "`s";
    }


    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash("control", "Control a specific player and view their info")
                .addOption(OptionType.STRING, "player", "The player to control", true);
    }


    public MessageEmbed getData(Player player, OfflinePlayer offlineplayer, boolean isOffline) {
        if (!isOffline) {
            PlayTimer timer = new PlayTimer();
            String ip = player.getAddress().getAddress().getHostAddress();
            String cc = getCountryByIP(ip);
            String playtime = getPlayTime(timer.getPlaytime(player.getUniqueId()));

            double health = player.getHealth();
            int totalHearts = (int) Math.ceil(health / 2);
            String hearts = ":heart:".repeat(totalHearts);

            if (hearts.isEmpty()) {
                hearts = "Dead x:";
            }

            double hunger = player.getFoodLevel();
            int totalHunger = (int) Math.ceil(hunger / 2);
            String food = "🍗".repeat(totalHunger);

            if (food.isEmpty()) {
                food = "starving";
            }

            World world = player.getWorld();
            World.Environment environment = world.getEnvironment();
            String dimension = switch (environment) {
                case NORMAL -> "Overworld";
                case NETHER -> "Nether";
                case THE_END -> "The end";
                default -> "Backrooms (unknown)";
            };

            String coords = "X: `" + Math.round(player.getX()) + "` Y: `" + Math.round(player.getY()) + "` Z:`" + Math.round(player.getZ()) + "`";
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(player.getName(), null, "https://mineskin.eu/helm/" + player.getName())
                    .setTitle("Player data")
                    .setColor(new Color(71, 230, 111))
                    .setDescription(
                            "**Player UUID**: `" + player.getUniqueId() + "`\n**IP**: `" + ip + " (" + cc + ")`\n**Ping**: `" + player.getPing() + "`ms\n" + "**Dimension**: " + dimension +
                            "\n**Is OP**: " + ((player.isOp()) ? "Yes":"No") + "\n**Health**: " + hearts + "\n**Hunger**: " + food + "\n**Coords**: " + coords +"\n**Play time**: " + playtime
                    );

            return embed.build();
        }

        String coords = "X: `" + Math.round(offlineplayer.getLocation().getX()) + "` Y: `" + Math.round(offlineplayer.getLocation().getY()) + "` Z:`" + Math.round(offlineplayer.getLocation().getZ()) + "`";
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(offlineplayer.getName(), null, "https://mineskin.eu/helm/" + offlineplayer.getName())
                .setTitle("Player data")
                .setColor(new Color(71, 230, 111))
                .setDescription(
                       "**Player UUID**: `" + offlineplayer.getUniqueId() + "`\n**Is banned**: " + ((offlineplayer.isBanned()) ? "Yes":"No") + "\n**Last seen**: <t:" + (offlineplayer.getLastSeen() / 1000) + ":F>\n**Is OP**: " + ((offlineplayer.isOp()) ? "Yes":"No" + "\n**Coords**: " + coords)
                )
                .setFooter("Offline player");

        return embed.build();
    }



    public void handleButtonClick(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        List<MessageEmbed> embeds = event.getMessage().getEmbeds();
        MessageEmbed embedd = embeds.getFirst();
        String playerName = embedd.getAuthor().getName();
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            MessageEmbed embed = getData(player, null, false);
            event.getMessage().editMessageEmbeds(embed).queue();
        } else {
            event.reply("Player not found!").setEphemeral(true).queue();
        }
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
       Discraft discraft = new Discraft();
       List<String> admins = discraft.getConfig().getStringList("admins");
       if (!admins.contains(event.getUser().getId())) {
           EmbedBuilder embed = new EmbedBuilder()
                   .setDescription("❌ | You're not an admin! you can set admins in the plugin's config.yml")
                   .setColor(new Color(230, 75, 64));
           event.replyEmbeds(embed.build()).setEphemeral(true).queue();
           return;
       }

       String playerName = event.getOption("player").getAsString();
       boolean isOffline = false;
       OfflinePlayer offlineplayer = null;
       Player player = Bukkit.getPlayer(playerName);
       if (player == null) {
           offlineplayer = Bukkit.getOfflinePlayer(playerName);
           if (offlineplayer == null) {
               EmbedBuilder embed = new EmbedBuilder()
                       .setDescription("❌ | Player **" + playerName + "** is not online!")
                       .setColor(new Color(230, 75, 64));
               event.replyEmbeds(embed.build()).setEphemeral(true).queue();
               return;
           }
           player = offlineplayer.getPlayer();
           isOffline = true;
       }

       MessageEmbed embed = getData(player, offlineplayer, isOffline);
       if (isOffline) {
           event.replyEmbeds(embed).queue();
           return;
       }

       event.replyEmbeds(embed)
               .addActionRow(Button.secondary("refresh", "Refresh"))
               .addActionRow(StringSelectMenu.create("actions")
                       .setMaxValues(1)
                       .setMinValues(1)
                       .setPlaceholder("Choose an action")
                       .addOptions(
                               SelectOption.of("Kill", "action_kill")
                                       .withDescription("Kill the player")
                                       .withEmoji(Emoji.fromUnicode("💀")),
                               SelectOption.of("Heal", "action_heal")
                                       .withDescription("Heal the player")
                                       .withEmoji(Emoji.fromUnicode("💊")),
                               SelectOption.of("Starve", "action_starve")
                                       .withDescription("Starve the player (apply hunger)")
                                       .withEmoji(Emoji.fromUnicode("🍗")),
                               SelectOption.of("Feed", "action_feed")
                                       .withDescription("Feed the player (apply saturation)")
                                       .withEmoji(Emoji.fromUnicode("😋")),
                               SelectOption.of("Inventory", "action_inventory")
                                       .withDescription("View the player inventory")
                                       .withEmoji(Emoji.fromUnicode("🎒")),
                               SelectOption.of("Teleport", "action_teleport")
                                       .withDescription("Teleport the player to a set x,y,z")
                                       .withEmoji(Emoji.fromUnicode("🌍")),
                               SelectOption.of("Toggle OP", "action_op")
                                       .withDescription("Toggle player's OP")
                                       .withEmoji(Emoji.fromUnicode("🔑"))
                       )
                       .build())
               .queue();
    }


}
