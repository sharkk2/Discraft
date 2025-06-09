package org.discraft.Bot.core.Listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.discraft.Discraft;

import java.awt.*;
import java.util.List;

public class SelectMenuListener extends ListenerAdapter {

    public String getInventoryString(Player player) {
        Inventory inventory = player.getInventory();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getAmount() > 0) {
                String itemName = item.getType().name().toLowerCase().replace("_", " "); // Convert to readable format
                int quantity = item.getAmount();
                int slot = i;

                sb.append("**").append(itemName).append("**: `").append(quantity).append("` (slot `").append(slot).append("`)\n");
            }
        }

        return sb.toString().trim();
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("actions")) {
            Discraft discraft = Discraft.getDiscraft();
            List<String> admins = discraft.getConfig().getStringList("admins");
            if (!admins.contains(event.getUser().getId())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setDescription("❌ | You're not an admin! you can set admins in the plugin's config.yml")
                        .setColor(new Color(230, 75, 64));
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                return;
            }

            String actionkey = event.getValues().get(0);

            List<MessageEmbed> embeds = event.getMessage().getEmbeds();
            MessageEmbed embedd = embeds.getFirst();
            String playerName = embedd.getAuthor().getName();
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                event.reply("Player is not found!").setEphemeral(true).queue();
                return;
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Action success")
                    .setColor(new Color(71, 230, 111));

            Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("Discraft"), () -> {  // do NOT change this name unless the plugin name has changed!
                switch (actionkey) {
                    case "action_kill":
                        player.setHealth(0);
                        embed.setDescription("**" + playerName + "** Has been executed.");
                        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        break;
                    case "action_starve":
                        player.setFoodLevel(0);
                        embed.setDescription("Starved **" + playerName + "** inhumanely.");
                        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        break;
                    case "action_feed":
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 300, 10));
                        embed.setDescription("**" + playerName + "** has been feed.");
                        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        break;
                    case "action_heal":
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 10));
                        embed.setDescription("**" + playerName + "** has been healed.");
                        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        break;
                    case "action_inventory":
                        String inventory = getInventoryString(player);
                        if (inventory.isEmpty()) {
                            inventory = "No items.";
                        }

                        embed.setDescription(inventory);
                        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        break;
                    case "action_teleport":
                        TextInput x = TextInput.create("x", "X", TextInputStyle.SHORT)
                                .setPlaceholder("69")
                                .setMaxLength(20)
                                .setRequired(true)
                                .build();

                        TextInput y = TextInput.create("y", "Y", TextInputStyle.SHORT)
                                .setPlaceholder("36")
                                .setMaxLength(20)
                                .setRequired(true)
                                .build();

                        TextInput z = TextInput.create("z", "Z", TextInputStyle.SHORT)
                                .setPlaceholder("420")
                                .setRequired(true)
                                .setMaxLength(20)
                                .build();

                        Modal modal = Modal.create("teleport-" + playerName, "Teleport " + playerName)
                                .addComponents(ActionRow.of(x), ActionRow.of(y), ActionRow.of(z))
                                .build();

                        event.replyModal(modal).queue();
                        break;


                    case "action_op":
                        boolean isOp = player.isOp();
                        player.setOp(!isOp);

                        if (isOp) {
                            embed.setDescription("**" + playerName + "**" + " is no longer OP");
                            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        } else {
                            embed.setDescription("**" + playerName + "**" + " is now OP");
                            event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                        }
                }
            });
        }
    }

}
