package br.kaiosouza.minigladiador.models;

import br.kaiosouza.minigladiador.MiniGladiador;
import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.type.RemoveCause;
import br.kaiosouza.minigladiador.type.Status;
import br.kaiosouza.minigladiador.utils.InventoryUtils;
import com.google.common.collect.Sets;
import lombok.Data;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import org.bukkit.entity.Player;

import java.util.Set;

@Data
public abstract class MiniGladiadorBase implements MiniGladiador {

    private Set<ClanMG> clans;
    private Set<PlayerMG> players;
    private Status status;
    private int timer;
    private int announcements;

    protected MiniGladiadorBase(){
        clans = Sets.newConcurrentHashSet();
        players = Sets.newConcurrentHashSet();
        status = Status.CLOSED;
        timer = 10;
        announcements = 10;
    }

    @Override
    public void addPlayer(Player player) {
        ClanPlayer clanPlayer = SolaryMiniGladiador.getClans().getClanPlayer(player.getUniqueId());
        if(clanPlayer == null){
            player.sendMessage("§cVocê precisa participar de um clan para entrar no mini gladiador");
            return;
        }

        for(PlayerMG playerMG : players){
            if(playerMG.getPlayer().equals(player)) {
                player.sendMessage("§cVocê já está participando do mini gladiador");
                return;
            }
        }

        if(!InventoryUtils.isInventoryEmpty(player)) {
            player.updateInventory(); // Aparecer os itens para o jogador se ele não estiver totalmente sincronizado com servidor
            player.sendMessage("§cSeu inventario deve estar vazio para participar do mini gladiador");
            return;
        }

        PlayerMG playerMG = new PlayerMG(player, 0);
        players.add(playerMG);

        player.teleport(SolaryMiniGladiador.getLocationManager().getLocations().get("entrada"));
        SolaryMiniGladiador.getInventoryManager().giveItems(player);

        ClanMG clanMG = getClanMG(clanPlayer.getClan());
        if(clanMG == null){
            clans.add(new ClanMG(clanPlayer.getClan(), Sets.newHashSet(playerMG)));
        }else{
            clanMG.getPlayers().add(playerMG);
        }
    }

    @Override
    public boolean containsClan(Clan clan) {
        for(ClanMG clanMG : clans){
            if(clanMG.getClan().equals(clan)){
                return true;
            }
        }

        return false;
    }

    @Override
    public ClanMG getClanMG(Clan clan) {
        for(ClanMG clanMG : clans){
            if(clanMG.getClan().equals(clan)){
                return clanMG;
            }
        }

        return null;
    }

    @Override
    public PlayerMG getPlayerMG(Player player) {
        for(PlayerMG playerMG : players){
            if(playerMG.getPlayer().equals(player)){
                return playerMG;
            }
        }

        return null;
    }

    @Override
    public void removePlayer(Player player, RemoveCause removeCause) {
        player.getInventory().clear();
        player.teleport(SolaryMiniGladiador.getLocationManager().getLocations().get("saida"));

        ClanPlayer clanPlayer = SolaryMiniGladiador.getClans().getClanPlayer(player);
        ClanMG clanMG = getClanMG(clanPlayer.getClan());
        if(clanMG.getPlayers().size() == 1) {
            clans.remove(clanMG);
        }else{
            clans.forEach(clansMG -> clansMG.getPlayers().removeIf(playerMG -> playerMG.getPlayer().equals(player)));
        }

        players.removeIf(playerMG -> playerMG.getPlayer().equals(player));

        if(checkWin()){
            clans.stream().findAny().ifPresent(this::winner);
        }
    }

    @Override
    public boolean checkWin() {
        return clans.size() == 1;
    }

    @Override
    public Set<PlayerMG> getPlayers() {
        return players;
    }

    @Override
    public Set<ClanMG> getClans() {
        return clans;
    }

}
