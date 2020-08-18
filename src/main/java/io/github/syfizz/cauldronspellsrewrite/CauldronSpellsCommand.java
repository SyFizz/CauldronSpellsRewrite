package io.github.syfizz.cauldronspellsrewrite;

import io.github.syfizz.cauldronspellsrewrite.utils.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.*;

public class CauldronSpellsCommand implements CommandExecutor {

    private final CauldronSpellsRewrite main = CauldronSpellsRewrite.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("cauldronspells") || label.equalsIgnoreCase("cspell") || label.equalsIgnoreCase("cauldronspell")) {
            boolean senderIsAPlayer;
            senderIsAPlayer = sender instanceof Player;

            if (args.length == 1) {

                //HELP COMMAND

                if (args[0].equalsIgnoreCase("help")) {
                    if (sender.hasPermission(main.getConfig().getString("permissions.help_command"))) {
                        ArrayList<String> helpMessage = new ArrayList<>();
                        helpMessage.add(colorMessage("&6&m&l---------------&r &6&lCauldronSpells &m&l---------------"));
                        helpMessage.add(colorMessage(" "));
                        helpMessage.add(colorMessage("&e/cauldronspells help&a : &eDisplays this message."));
                        helpMessage.add(colorMessage("&e/cauldronspells reload&a : &eReloads the configuration file."));
                        helpMessage.add(colorMessage("&e/cauldronspells reset&a : &eResets the configuration file."));
                        helpMessage.add(colorMessage("&e/cauldronspells give <spellname> &a: &eGive a spell to yourself."));
                        helpMessage.add(colorMessage("&e/cauldronspells give <spellname> <player> &a: &eGive a spell to a player."));
                        helpMessage.add(colorMessage(" "));
                        helpMessage.add(colorMessage("&6&m&l-------------&r &6Made with ❤ by SyFizz_ &6&m&l-------------"));

                        //TODO : ADD MORE HELP MESSAGES
                        for (String s : helpMessage) {
                            sender.sendMessage(s);
                        }

                    } else {
                        sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.help_command")));
                    }

                    //RELOAD COMMAND

                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission(main.getConfig().getString("permissions.reload_command"))) {
                        main.reloadConfig();
                        sender.sendMessage(main.getColoredMessage("messages.commands.plugin_reloaded"));
                        Bukkit.getLogger().log(Level.INFO, "messages.core.console_plugin_reloaded");
                    } else {
                        sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.reload_command")));
                    }
                } else if(args[0].equalsIgnoreCase("reset")) {
                    if(sender.hasPermission(main.getConfig().getString("permissions.reset_command"))){
                        if(senderIsAPlayer){
                            Player player = (Player) sender;
                            if(getRESETCONFIRM().contains(player.getUniqueId())) {
                                getRESETCONFIRM().remove(player.getUniqueId());
                                File file = new File(main.getDataFolder(), "config.yml");
                                file.delete();
                                main.saveDefaultConfig();
                                main.reloadConfig();
                                sender.sendMessage(main.getColoredMessage("messages.commands.config_reset"));
                            } else {
                                sender.sendMessage(main.getColoredMessage("messages.commands.please_re_type"));
                                getRESETCONFIRM().add(player.getUniqueId());
                                Bukkit.getServer().getScheduler().runTaskLater(main, () -> {
                                    if(getRESETCONFIRM().contains(player.getUniqueId())) {
                                        getRESETCONFIRM().remove(player.getUniqueId());
                                    }
                                }, 20L * 30);
                            }
                        } else {
                            sender.sendMessage(main.getColoredMessage("messages.errors.player_only_command"));
                        }
                    } else {
                        sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.reset_command")));
                    }
                } else if (args[0].equalsIgnoreCase("cancel")){
                    if(senderIsAPlayer){
                        Player player = (Player) sender;
                        if(getRESETCONFIRM().contains(player.getUniqueId())) {
                            getRESETCONFIRM().remove(player.getUniqueId());
                            sender.sendMessage(main.getColoredMessage("messages.commands.command_cancelled"));
                        } else {
                            sender.sendMessage(main.getColoredMessage("messages.errors.no_pending_commands"));
                            getRESETCONFIRM().add(player.getUniqueId());
                        }
                    } else {
                        sender.sendMessage(main.getColoredMessage("messages.errors.player_only_command"));
                    }
                } else {
                    sender.sendMessage(main.getColoredMessage("messages.errors.invalid_args"));
                }

            } else if (args.length == 2) {
                /*
                    GIVE SELF COMMAND
                 */
                if (args[0].equalsIgnoreCase("give")) {
                    if (getSpells().contains(args[1])) {
                        if (!main.getConfig().getBoolean("permissions.per_spell_permission")) {
                            if (sender.hasPermission(main.getConfig().getString("permissions.giveself"))) {
                                if (senderIsAPlayer) {
                                    Player player = (Player) sender;
                                    ItemStack item = new ItemGenerator(main).generateItem(args[1].toLowerCase(), 1, Material.getMaterial(main.getConfig().getString("spells.cauldron_spell_item")));
                                    player.getInventory().addItem(item);
                                    player.updateInventory();
                                    sender.sendMessage(main.getColoredMessage("messages.commands.successfully_auto_given_spell").replace("%spell%", args[1]));
                                } else {
                                    sender.sendMessage(main.getColoredMessage("messages.errors.player_only_command"));
                                }
                            } else {
                                sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.giveself")));
                            }
                        } else if (main.getConfig().getBoolean("permissions.per_spell_permission")) {
                            if (sender.hasPermission(main.getConfig().getString("permissions.spells." + args[1] + ".giveself"))) {
                                if (senderIsAPlayer) {
                                    Player player = (Player) sender;
                                    ItemStack item = new ItemGenerator(main).generateItem(args[1].toLowerCase(), 1, Material.getMaterial(main.getConfig().getString("spells.cauldron_spell_item")));
                                    player.getInventory().addItem(item);
                                    player.updateInventory();
                                    sender.sendMessage(main.getColoredMessage("messages.commands.successfully_auto_given_spell".replaceAll("%spell%", args[1])));
                                } else {
                                    sender.sendMessage(main.getColoredMessage("messages.errors.player_only_command"));
                                }
                            } else {
                                sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.spells." + args[1] + ".giveself")));
                            }
                        }
                    } else {
                        sender.sendMessage(main.getColoredMessage("messages.errors.not_a_spell").replaceAll("%spell%", args[1]));
                    }
                } else {
                    sender.sendMessage(main.getColoredMessage("messages.errors.invalid_args"));
                }
            } else if(args.length==3){
                /*
                    GIVE COMMAND
                 */
                if(args[0].equalsIgnoreCase("give")){
                    if(getSpells().contains(args[1])){
                        if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[2]))){

                            Player target = Bukkit.getPlayer(args[2]);
                            if(main.getConfig().getBoolean("permissions.per_spell_permission")){
                                if(sender.hasPermission(main.getConfig().getString("permissions.spells." + args[1] + ".give"))){
                                    ItemStack item = new ItemGenerator(main).generateItem(args[1].toLowerCase(), 1, Material.getMaterial(main.getConfig().getString("spells.cauldron_spell_item")));
                                    target.getInventory().addItem(item);
                                    target.updateInventory();
                                    final String message = main.getColoredMessage("messages.commands.successfully_given_spell").replaceAll("%spell%", args[1]);
                                    sender.sendMessage(message.replaceAll("%player%", target.getDisplayName()));
                                    target.sendMessage(main.getColoredMessage("messages.commands.spell_received").replaceAll("%spell%", args[1]));
                                } else {
                                    sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.spells."+args[1]+".give")));
                                }
                            } else {
                                if(sender.hasPermission(main.getConfig().getString("permissions.give"))){
                                    ItemStack item = new ItemGenerator(main).generateItem(args[1].toLowerCase(), 1, Material.getMaterial(main.getConfig().getString("spells.cauldron_spell_item")));
                                    target.getInventory().addItem(item);
                                    target.updateInventory();
                                    final String message = main.getColoredMessage("messages.commands.successfully_given_spell").replaceAll("%spell%", args[1]);
                                    sender.sendMessage(message.replaceAll("%player%", target.getDisplayName()));
                                    target.sendMessage(main.getColoredMessage("messages.commands.spell_received").replaceAll("%spell%", args[1]));
                                } else {
                                    sender.sendMessage(main.getColoredMessage("messages.errors.no_perm").replaceAll("%node%", main.getConfig().getString("permissions.give")));
                                }
                            }
                        } else {
                            sender.sendMessage(main.getColoredMessage("messages.errors.player_not_online").replaceAll("%player%", args[2]));
                        }
                    } else {
                        sender.sendMessage(main.getColoredMessage("messages.errors.not_a_spell").replaceAll("%spell%", args[1]));
                    }
                }
            } else {
                sender.sendMessage(main.getColoredMessage("messages.errors.invalid_args"));
            }
            return true;
        }
        return false;
    }
}
