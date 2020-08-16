package io.github.syfizz.cauldronspellsrewrite;

import io.github.syfizz.cauldronspellsrewrite.listeners.PlayerDamageListener;
import io.github.syfizz.cauldronspellsrewrite.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import io.github.syfizz.cauldronspellsrewrite.utils.GlowEnchant;

import java.lang.reflect.Field;
import java.util.*;

public class CauldronSpellsRewrite extends JavaPlugin {

    private static Set<UUID> FALLINGPLAYERS = new HashSet<>();
    public static Set<UUID> getFALLINGPLAYERS() {
        return FALLINGPLAYERS;
    }

    private static Set<UUID> RESETCONFIRM = new HashSet<>();
    public static Set<UUID> getRESETCONFIRM(){ return RESETCONFIRM;}

    private static ArrayList<String> spells = new ArrayList<>();
    public static ArrayList<String> getSpells() {
        return spells;
    }

    private static Set<Location> IN_USE_CAULDRONS = new HashSet<>();
    public static Set<Location> getInUseCauldrons() {
        return IN_USE_CAULDRONS;
    }

    public Random random = new Random();
    public static CauldronSpellsRewrite instance;

    public void onEnable() {
        System.out.println(ChatColor.translateAlternateColorCodes('&', getConfig().getString("messages.core.console_plugin_enabled")));
        saveDefaultConfig();
        registerGlow();
        instance = this;
        spells.add("fly");
        spells.add("strength");
        getCommand("cauldronspells").setExecutor(new CauldronSpellsCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(this), this);

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
    public static CauldronSpellsRewrite getInstance() {
        return instance;
    }
    public static String colorMessage(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public String getColoredMessage(String s){
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(s));
    }
}
