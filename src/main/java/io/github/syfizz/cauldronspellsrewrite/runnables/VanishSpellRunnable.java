package io.github.syfizz.cauldronspellsrewrite.runnables;

import io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite;
import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.getInUseCauldrons;
import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.getVanishedPlayers;

public class VanishSpellRunnable extends BukkitRunnable {
    private Player player;
    private CauldronSpellsRewrite main;
    private int chance;
    private Block block;
    private int time;
    public VanishSpellRunnable(Player player, CauldronSpellsRewrite main, int chance, Block block, int time) {
        this.player = player;
        this.main = main;
        this.chance = chance;
        this.block = block;
        this.time = time;
    }

    @Override
    public void run() {
        getInUseCauldrons().add(block.getLocation());
        if (time > 0){
            PlayEffect.play(VisualEffect.ENCHANTMENT_TABLE, block.getLocation().add(0, 1.5, 0), "speed:0.5 num:75");
        } else if (time == 0){
            if(main.random.nextInt(100) < chance) {
                block.setType(Material.AIR);
                block.setType(Material.CAULDRON);
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(!p.hasPermission(main.getConfig().getString("permissions.bypass_effects"))){
                        p.hidePlayer(player);
                    }
                    getVanishedPlayers().add(player.getUniqueId());
                }
                Bukkit.getServer().getScheduler().runTaskLater(main, () -> {
                    for(Player p : Bukkit.getOnlinePlayers()){
                        if(!p.hasPermission(main.getConfig().getString("permissions.bypass_effects"))){
                            p.showPlayer(player);
                        }
                        getVanishedPlayers().remove(player.getUniqueId());
                    }
                    player.sendMessage(main.getColoredMessage("spells.vanish.effect_expired_message"));
                }, 20L * 60 * main.getConfig().getInt("spells.vanish.duration_in_minutes"));
                player.sendMessage(main.getColoredMessage("spells.vanish.spell_success_message").replaceAll("%duration%", Integer.toString(main.getConfig().getInt("spells.vanish.duration_in_minutes"))));
            } else {
                double health = player.getHealth();
                player.sendMessage(main.getColoredMessage("spells.vanish.spell_failed_message"));
                PlayEffect.play(VisualEffect.LIGHTNING, block.getLocation().add(0, 1, 0), "lchance:100, mode:anytime");
                block.setType(Material.AIR);
                player.setHealth(health);
                player.damage(2D);
            }
            getInUseCauldrons().remove(block.getLocation());
            this.cancel();
            return;
        }
        time--;
    }
}
