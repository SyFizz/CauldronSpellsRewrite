package io.github.syfizz.cauldronspellsrewrite;

import io.github.syfizz.cauldronspellsrewrite.listeners.PlayerDamageListener;
import io.github.syfizz.cauldronspellsrewrite.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.syfizz.cauldronspellsrewrite.utils.GlowEnchant;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class CauldronSpellsRewrite extends JavaPlugin {

    private static Set<UUID> fallingPlayers = new HashSet<>();
    public static Set<UUID> getFallingPlayers() {
        return fallingPlayers;
    }
    private static Set<UUID> inResetPlayers = new HashSet<>();
    public static Set<UUID> getInResetPlayers(){ return inResetPlayers;}
    private static ArrayList<String> spells = new ArrayList<>();
    public static ArrayList<String> getSpells() {
        return spells;
    }
    private static Set<Location> inUseCauldrons = new HashSet<>();
    public static Set<Location> getInUseCauldrons() {
        return inUseCauldrons;
    }
    private static Set<UUID> vanishedPlayers = new HashSet<>();
    public static Set<UUID> getVanishedPlayers() {
        return vanishedPlayers;
    }


    public Random random = new Random();

    public void onEnable() {
        if(Bukkit.getPluginManager().getPlugin("PlayEffect") == null){
            Bukkit.getPluginManager().disablePlugin(this);
            System.out.println(ChatColor.translateAlternateColorCodes('&', "&c&lPlayEffect is required to run CauldronSpells ! Please download it from releases page."));
        }
        try {
            File playEffectFile;
            playEffectFile = new File(Bukkit.getPluginManager().getPlugin("PlayEffect").getDataFolder(), "config.yml");
            FileConfiguration playEffectConfiguration = YamlConfiguration.loadConfiguration(playEffectFile);
            playEffectConfiguration.set("general.check-updates", false);
            playEffectConfiguration.save(playEffectFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.core.console_plugin_enabled")));
        saveDefaultConfig();
        registerGlow();
        spells.add("fly");
        spells.add("strength");
        spells.add("vanish");
        getCommand("cauldronspells").setExecutor(new CauldronSpellsCommand(this));
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        for(UUID uuid : getVanishedPlayers()){
            Player vanishedPlayer = Bukkit.getPlayer(uuid);
            for(Player p : Bukkit.getOnlinePlayers()){
                p.showPlayer(vanishedPlayer);
            }
        }

    }

    public void onDisable() {
        System.out.println(ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.core.console_plugin_disabled")));
    }


    public void registerGlow(){
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GlowEnchant glow = new GlowEnchant(70);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public Random getRandom(){
        return random;
    }

    public static String colorMessage(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public String getColoredMessage(String s){
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(s));
    }
}
