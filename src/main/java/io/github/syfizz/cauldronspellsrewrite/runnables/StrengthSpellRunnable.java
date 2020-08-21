package io.github.syfizz.cauldronspellsrewrite.runnables;

import io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite;
import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.getInUseCauldrons;

public class StrengthSpellRunnable extends BukkitRunnable {
    private CauldronSpellsRewrite main;
    private Player player;
    private Block block;
    private int time;
    private int chance;
    public StrengthSpellRunnable(Player player, CauldronSpellsRewrite main, int chance, Block block, int time)
    {
        this.main=main;
        this.player = player;
        this.block = block;
        this.chance = chance;
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
                player.sendMessage(main.getColoredMessage("spells.strength.spell_success_message").replaceAll("%duration%", Integer.toString(main.getConfig().getInt("spells.strength.duration_in_minutes"))));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 60 * main.getConfig().getInt("spells.strength.duration_in_minutes"), main.getConfig().getInt("spells.strength.potion_amplifier")));
            } else {
                double health = player.getHealth();
                player.sendMessage(main.getColoredMessage("spells.strength.spell_failed_message"));
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
