package br.kaiosouza.minigladiador.managers;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.models.ClanMG;
import br.kaiosouza.minigladiador.models.MiniGladiadorBase;
import br.kaiosouza.minigladiador.models.PlayerMG;
import br.kaiosouza.minigladiador.type.RemoveCause;
import br.kaiosouza.minigladiador.type.Status;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class MiniGladiadorManager extends MiniGladiadorBase {

    private final SolaryMiniGladiador plugin;

    public MiniGladiadorManager(SolaryMiniGladiador plugin){
        this.plugin = plugin;
    }

    public void startTimer(){
        setStatus(Status.WAITING);

        new BukkitRunnable() {
            int timer = getTimer();
            int announcements = getAnnouncements();
            @Override
            public void run() {
                if(this.timer == 0) {
                    if(canStart()) startWatcher();
                    cancel();
                    return;
                }

                Bukkit.broadcastMessage(getPlayers().size() + " jogadores");
                Bukkit.broadcastMessage(getClans().size() + " clans");
                Bukkit.broadcastMessage(announcements + " anuncios");

                this.timer -= (getTimer() / getAnnouncements());
                this.announcements--;
            }
        }.runTaskTimer(this.plugin, 0, (getTimer() / getAnnouncements()) * 20);
    }

    private void startWatcher(){
        setStatus(Status.HAPPENING);
        Bukkit.broadcastMessage("Começou");
        Bukkit.broadcastMessage(getPlayers().size() + " jogadores");
        Bukkit.broadcastMessage(getClans().size() + " clans");

        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(getPlayers().size() + " jogadores");
                Bukkit.broadcastMessage(getClans().size() + " clans");
            }
        }.runTaskTimer(this.plugin, 0, 200);
    }

    private boolean canStart(){
        if(getClans().size() < 2){
            Bukkit.broadcastMessage("Clans insuficientes, cancelando");
            for(PlayerMG playerMG : getPlayers()){
                removePlayer(playerMG.getPlayer(), RemoveCause.INSUFFICIENTCLANS);
            }
            setStatus(Status.CLOSED);
            return false;
        }

        return true;
    }

    @Override
    public void winner(ClanMG clanMG) {
        setStatus(Status.FINISHING);
        Bukkit.broadcastMessage("O clan " + clanMG.getClan().getName() + " ganhou! MANAGER");
        new BukkitRunnable() {
            @Override
            public void run() {
                for(PlayerMG playerMG : getPlayers()){
                    removePlayer(playerMG.getPlayer(), RemoveCause.WINNER);
                }
                setStatus(Status.CLOSED);
            }
        }.runTaskLater(this.plugin, 100);
    }
}
