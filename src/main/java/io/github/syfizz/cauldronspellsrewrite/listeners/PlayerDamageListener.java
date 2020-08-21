package io.github.syfizz.cauldronspellsrewrite.listeners;

import io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static io.github.syfizz.cauldronspellsrewrite.CauldronSpellsRewrite.getFallingPlayers;

public class PlayerDamageListener implements Listener {
    CauldronSpellsRewrite main;

    public PlayerDamageListener(CauldronSpellsRewrite main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e){
        if(e.getEntity().getType() == EntityType.PLAYER && e.getCause() == EntityDamageEvent.DamageCause.FALL){
            Player player = (Player) e.getEntity();
            if(getFallingPlayers().contains(player.getUniqueId())){
                e.setCancelled(true);
                getFallingPlayers().remove(player.getUniqueId());
            }
        }
    }
}
