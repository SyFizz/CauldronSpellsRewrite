package io.github.syfizz.cauldronspellsrewrite.listeners;

import io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite;
import io.github.syfizz.cauldronspellsrewrite.runnables.FlySpellRunnable;
import io.github.syfizz.cauldronspellsrewrite.runnables.StrengthSpellRunnable;
import io.github.syfizz.cauldronspellsrewrite.utils.GlowEnchant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Cauldron;

import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.getInUseCauldrons;

public class PlayerInteractListener implements Listener {
    CauldronSpellsRewrite main;

    public PlayerInteractListener(CauldronSpellsRewrite main) {
        this.main = main;
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (event.getClickedBlock().getType() == Material.CAULDRON){
                if(event.getPlayer().getItemInHand().getType() == Material.getMaterial(main.getConfig().getString("spells.cauldron_spell_item"))) {
                    if(event.getPlayer().getItemInHand().hasItemMeta()) {
                        GlowEnchant glowEnchant = new GlowEnchant(70);
                        if(event.getPlayer().getItemInHand().getItemMeta().hasEnchant(glowEnchant)) {
                            final String itemName = event.getPlayer().getItemInHand().getItemMeta().getDisplayName();
                            final Cauldron cauldron = (Cauldron) event.getClickedBlock().getState().getData();
                            final int chance = event.getPlayer().getItemInHand().getItemMeta().getEnchantLevel(glowEnchant);
                            if (cauldron.isFull()) {
                                if(!getInUseCauldrons().contains(event.getClickedBlock().getLocation())) {
                                    if (main.getConfig().getBoolean("permissions.per_spell_permission")) {
                                        if (itemName.equals(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("spells.fly.item_name")))) {
                                            if (event.getPlayer().hasPermission(main.getConfig().getString("permissions.spells.fly.use"))) {
                                                if(!event.getPlayer().getAllowFlight()) {
                                                    new FlySpellRunnable(event.getPlayer(), main, chance, event.getClickedBlock(), 5).runTaskTimer(main, 0L, 20L);
                                                    event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                                                    event.getPlayer().updateInventory();
                                                } else {
                                                    event.getPlayer().sendMessage(main.getColoredMessage("spells.fly.already_flying_message"));
                                                }
                                            } else {
                                                event.getPlayer().sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.spells.fly.use")));
                                            }
                                        } else if (itemName.equals(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("spells.strength.item_name")))) {
                                            if (event.getPlayer().hasPermission(main.getConfig().getString("permissions.spells.strength.use"))) {
                                                new StrengthSpellRunnable(event.getPlayer(), main, chance, event.getClickedBlock(), 5).runTaskTimer(main, 0L, 20L);
                                                event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                                                event.getPlayer().updateInventory();
                                            } else {
                                                event.getPlayer().sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.spells.strength.use")));
                                            }
                                        }
                                    } else {
                                        if (itemName.equals(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("spells.fly.item_name")))) {
                                            if(!event.getPlayer().getAllowFlight()) {
                                                new FlySpellRunnable(event.getPlayer(), main, chance, event.getClickedBlock(), 5).runTaskTimer(main, 0L, 20L);
                                                event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                                                event.getPlayer().updateInventory();
                                            } else {
                                                event.getPlayer().sendMessage(main.getColoredMessage("spells.fly.already_flying_message"));
                                            }
                                        } else if (itemName.equals(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("spells.strength.item_name")))) {
                                            new StrengthSpellRunnable(event.getPlayer(), main, chance, event.getClickedBlock(), 5).runTaskTimer(main, 0L, 20L);
                                            event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                                            event.getPlayer().updateInventory();
                                        }
                                    }
                                } else {
                                    event.getPlayer().sendMessage(main.getColoredMessage("messages.errors.cauldron_already_in_use"));
                                }
                            } else {
                                event.getPlayer().sendMessage(main.getColoredMessage("messages.errors.cauldron_must_be_full"));
                            }
                        }
                    }
                }
            }

        }
    }
}
