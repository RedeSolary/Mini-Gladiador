package br.kaiosouza.minigladiador.listeners;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.managers.MiniGladiadorManager;
import br.kaiosouza.minigladiador.models.PlayerMG;
import br.kaiosouza.minigladiador.type.RemoveCause;
import br.kaiosouza.minigladiador.type.Status;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class MiniGladidadorListener implements Listener {

    private final MiniGladiadorManager MGManager;
    public MiniGladidadorListener(SolaryMiniGladiador plugin){
        MGManager = SolaryMiniGladiador.getMiniGladiadorManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onDisconnect(PlayerQuitEvent event){
        if(MGManager.getStatus() != Status.CLOSED) {
            PlayerMG playerMG = MGManager.getPlayerMG(event.getPlayer());
            if(playerMG != null){
                MGManager.removePlayer(playerMG.getPlayer(), RemoveCause.DISCONNECT);
            }
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event){
        if(MGManager.getStatus() == Status.HAPPENING){
            PlayerMG playerMG = MGManager.getPlayerMG(event.getEntity());
            if(playerMG == null) return;

            Optional<Player> killer = Optional.ofNullable(event.getEntity().getKiller());
            killer.ifPresent(playerKiller -> {
                PlayerMG playerMGKiller = MGManager.getPlayerMG(playerKiller);
                if(playerMGKiller != null) {
                    playerMGKiller.addKill();
                    Bukkit.broadcastMessage(event.getEntity().getName() + " morreu para " + playerMGKiller.getPlayer().getName());
                }else{
                    Bukkit.broadcastMessage(event.getEntity().getName() + " morreu");
                }
            });

            MGManager.removePlayer(event.getEntity(), RemoveCause.DEATH);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        if(MGManager.getStatus() == Status.HAPPENING || MGManager.getStatus() == Status.CLOSED) return;

        event.setCancelled(true);
    }
}
