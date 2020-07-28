package br.kaiosouza.minigladiador.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data @AllArgsConstructor
public class PlayerMG {
    private Player player;
    private int kills;

    public void addKill(){
        kills++;
    }
}
