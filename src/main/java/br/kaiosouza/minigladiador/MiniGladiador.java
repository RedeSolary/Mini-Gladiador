package br.kaiosouza.minigladiador;

import br.kaiosouza.minigladiador.models.ClanMG;
import br.kaiosouza.minigladiador.models.PlayerMG;
import br.kaiosouza.minigladiador.type.RemoveCause;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.entity.Player;

import java.util.Set;

public interface MiniGladiador {

    void addPlayer(Player player);

    void removePlayer(Player player, RemoveCause removeCause);

    void winner(ClanMG clanMG);

    boolean containsClan(Clan clan);

    boolean checkWin();

    ClanMG getClanMG(Clan clan);

    PlayerMG getPlayerMG(Player player);

    Set<PlayerMG> getPlayers();

    Set<ClanMG> getClans();
}
