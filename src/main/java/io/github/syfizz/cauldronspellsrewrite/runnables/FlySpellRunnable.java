package io.github.syfizz.cauldronspellsrewrite.runnables;

import io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite;
import me.fromgate.playeffect.PlayEffect;
import me.fromgate.playeffect.VisualEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.getInUseCauldrons;

public class FlySpellRunnable extends BukkitRunnable {

    private final int chance;
    private final Player player;
    private final CauldronSpellsRewrite main;
    int time;
    private final Block block;

    public FlySpellRunnable(Player player, CauldronSpellsRewrite main, int chance, Block block, int time) {
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
        } else if(time==0){
            if(main.random.nextInt(100) < chance) {
                block.setType(Material.AIR);
                block.setType(Material.CAULDRON);
                player.sendMessage(main.getColoredMessage("spells.fly.spell_success_message").replaceAll("%duration%", Integer.toString(main.getConfig().getInt("spells.fly.duration_in_minutes"))));
                player.setAllowFlight(true);
                Bukkit.getServer().getScheduler().runTaskLater(main, () -> {
                    player.setAllowFlight(false);
                    CauldronSpellsRewrite.getFALLINGPLAYERS().add(player.getUniqueId());
                    }, 20L * 60 * main.getConfig().getInt("spells.fly.duration_in_minutes"));
            } else {
                double health = player.getHealth();
                player.sendMessage(main.getColoredMessage("spells.fly.spell_failed_message"));
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
