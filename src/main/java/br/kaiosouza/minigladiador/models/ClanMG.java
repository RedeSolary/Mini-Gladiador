package br.kaiosouza.minigladiador.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Player;

import java.util.Set;

@Data @AllArgsConstructor
public class ClanMG {
    private Clan clan;
    private Set<PlayerMG> players;

    public PlayerMG getPlayerMG(Player player){
        for(PlayerMG playerMG : players){
            if(playerMG.getPlayer().equals(player)){
                return playerMG;
            }
        }

        return null;
    }
}
