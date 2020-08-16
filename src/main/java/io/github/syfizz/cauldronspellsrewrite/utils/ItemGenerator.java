package io.github.syfizz.cauldronspellsrewrite.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite;

import java.util.ArrayList;
import java.util.Random;

public class ItemGenerator {

    CauldronSpellsRewrite main;

    public ItemGenerator(CauldronSpellsRewrite main) {
        this.main = main;
    }

    public ItemStack generateItem(String spellName, int amount, Material material){

        //Creating a random success rate between the minimal and maximal values set in the config.
        final int minRate = main.getConfig().getInt("spells.success_rate_min");
        final int maxRate = main.getConfig().getInt("spells.success_rate_max");
        int chance = main.getRandom().nextInt(maxRate);

        while (chance < minRate){
            chance = main.getRandom().nextInt(maxRate);
        }


        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("spells."+spellName+".item_name")));

        //Colouring and replacing placeholders in the lore.
        ArrayList<String> rawLore = new ArrayList<>(main.getConfig().getStringList("spells."+spellName+".lore"));
        ArrayList<String> coloredRawLore = new ArrayList<>();
        ArrayList<String> finalLore = new ArrayList<>();
        for(String s : rawLore){
            coloredRawLore.add(ChatColor.translateAlternateColorCodes('&', s).replaceAll("%success_rate%", Integer.toString(chance)));
        }
        for(String s : coloredRawLore){
            finalLore.add(s.replaceAll("%destroy_rate%", Integer.toString(100-chance)));
        }
        meta.setLore(finalLore);

        //Storing chance rate in an invisible enchant.
        GlowEnchant glowEnchant = new GlowEnchant(70);
        meta.addEnchant(glowEnchant, chance, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(meta);
        return itemStack;

    }

}
