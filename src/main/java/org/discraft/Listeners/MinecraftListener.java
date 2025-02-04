package org.discraft.Listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.discraft.bot.core.handlers.MinecraftEventPoster;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.Bukkit;
import org.discraft.Discraft;
import org.bukkit.event.server.ServerLoadEvent;
import org.json.JSONObject;

/*
   Type 0: Normal chat message
   Type 1: Join chat message
   Type 2: Leave chat message
   Type 3: Advancment chat message
   Type 4: Server ON
   Type 5: Server OFF
   Type 6: Death
  */

public class MinecraftListener implements Listener {

    private final MinecraftEventPoster eventPoster;
    private final Discraft plugin;

    private static final JSONObject advancements = new JSONObject("""
    {
      "advancements": {
        "story/mine_stone": { "name": "Stone Age", "type": "normal" },
        "story/upgrade_tools": { "name": "Getting an Upgrade", "type": "normal" },
        "story/smelt_iron": { "name": "Acquire Hardware", "type": "normal" },
        "story/iron_tools": { "name": "Isn't It Iron Pick", "type": "normal" },
        "story/deflect_arrow": { "name": "Not Today, Thank You", "type": "normal" },
        "story/form_obsidian": { "name": "Ice Bucket Challenge", "type": "normal" },
        "story/lava_bucket": { "name": "Hot Stuff", "type": "normal" },
        "story/enter_nether": { "name": "We Need to Go Deeper", "type": "normal" },
        "story/shiny_gear": { "name": "Cover Me with Diamonds", "type": "challenge" },
        "story/enchant_item": { "name": "Enchanter", "type": "normal" },
        "story/cure_zombie_villager": { "name": "Zombie Doctor", "type": "challenge" },
        "story/follow_ender_eye": { "name": "Eye Spy", "type": "normal" },
        "story/enter_end": { "name": "The End?", "type": "normal" },
        "story/obtain_blaze_rod": { "name": "Into Fire", "type": "normal" },
        "story/brew_potion": { "name": "Local Brewery", "type": "normal" },
        "story/create_beacon": { "name": "Bring Home the Beacon", "type": "challenge" },
        "story/obtain_netherite": { "name": "Netherite Upgrade", "type": "normal" }, 
        "adventure/sleep_in_bed": { "name": "Sweet Dreams", "type": "normal" },
        "nether/return_to_sender": { "name": "Return to Sender", "type": "normal" },
        "nether/find_bastion": { "name": "Those Were the Days", "type": "normal" },
        "nether/obtain_ancient_debris": { "name": "Hidden in the Depths", "type": "normal" },
        "nether/netherite_armor": { "name": "Cover Me in Debris", "type": "challenge" },
        "nether/obtain_netherite_hoe": { "name": "Serious Dedication", "type": "challenge" },
        "nether/use_lodestone": { "name": "Country Lode, Take Me Home", "type": "normal" },
        "nether/all_effects": { "name": "How Did We Get Here?", "type": "challenge" },
        "nether/all_potions": { "name": "A Furious Cocktail", "type": "challenge" },
        "nether/create_full_beacon": { "name": "Beaconator", "type": "challenge" },
        "nether/ride_strider": { "name": "Feels Like Home", "type": "normal" },
        "adventure/voluntary_exile": { "name": "Voluntary Exile", "type": "normal" },
        "adventure/kill_a_mob": { "name": "Monster Hunter", "type": "normal" },
        "adventure/trade": { "name": "What a Deal!", "type": "normal" },
        "adventure/shoot_arrow": { "name": "Take Aim", "type": "normal" },
        "adventure/sniper_duel": { "name": "Sniper Duel", "type": "challenge" },
        "adventure/hero_of_the_village": { "name": "Hero of the Village", "type": "challenge" },
        "adventure/arbalistic": { "name": "Arbalistic", "type": "challenge" },
        "adventure/kill_all_mobs": { "name": "Monsters Hunted", "type": "challenge" },
        "adventure/summon_iron_golem": { "name": "Hired Help", "type": "normal" },
        "adventure/totem_of_undying": { "name": "Postmortal", "type": "normal" },
        "adventure/spyglass_at_dragon": { "name": "Is it a Bird?", "type": "normal" },
        "adventure/spyglass_at_parrot": { "name": "Is it a Balloon?", "type": "normal" },
        "adventure/spyglass_at_ghast": { "name": "Is it a Plane?", "type": "normal" },
        "adventure/fall_from_world_height": { "name": "Caves & Cliffs", "type": "normal" },
        "adventure/avoid_vibration": { "name": "Sneak 100", "type": "normal" },
        "adventure/ride_a_boat_with_a_goat": { "name": "Whatever Floats Your Goat!", "type": "normal" },
        "husbandry/bred_all_animals": { "name": "Two by Two", "type": "challenge" },
        "husbandry/plant_seed": { "name": "A Seedy Place", "type": "normal" },
        "husbandry/tame_an_animal": { "name": "Best Friends Forever", "type": "normal" },
        "husbandry/fishy_business": { "name": "Fishy Business", "type": "normal" },
        "husbandry/balanced_diet": { "name": "A Balanced Diet", "type": "challenge" },
        "husbandry/complete_catalogue": { "name": "A Complete Catalogue", "type": "challenge" },
        "husbandry/axolotl_in_a_bucket": { "name": "The Cutest Predator", "type": "normal" },
        "husbandry/froglights": { "name": "With Our Powers Combined!", "type": "normal" },
        "husbandry/leash_all_frog_variants": { "name": "When the Squad Hops into Town", "type": "normal" }
      }
    }
    """);


    private JSONObject getAdvancement(String key) {
        try {
            JSONObject advancement = advancements.getJSONObject("advancements").optJSONObject(key);
            if (advancement != null) {
                return advancement;
            } else {
                Bukkit.getLogger().warning("[Discraft] Advancement key not found: " + key);
                return null;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[Discraft] Failed to fetch advancement: " + e.getMessage());
            return null;
        }
    }

    public MinecraftListener(MinecraftEventPoster eventPoster, Discraft plugin) {
        this.eventPoster = eventPoster;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if (message.length() < 2000) {return;}

        if (message.startsWith("!!")) {return;}

        eventPoster.Post(0, message, event.getPlayer());
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String message = "Has joined the game";
        eventPoster.Post(1, message, event.getPlayer());
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String message = "Has left the game";
        eventPoster.Post(2, message, event.getPlayer());
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        if (event.getAdvancement().getDisplay() == null) {
            return;
        }
        String key = event.getAdvancement().getKey().getKey();
        JSONObject advancement = getAdvancement(key);

        if (advancement == null) {return;}

        String name = advancement.getString("name");
        String type = advancement.getString("type");


        if (type.equals("normal")) {
            eventPoster.Post(3, "Has made the advancement " + name + "!", event.getPlayer());
        } else {
            eventPoster.Post(7, "Has done the challenge " + name + "!", event.getPlayer());
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        eventPoster.Post(6, event.getDeathMessage(), event.getPlayer());
    }

}
