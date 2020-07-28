package br.kaiosouza.minigladiador.commands;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.models.CommandBase;
import br.kaiosouza.minigladiador.type.RemoveCause;
import br.kaiosouza.minigladiador.type.Status;

public class ExitCommand extends CommandBase {

    public ExitCommand() {
        addAlias("sair");
        addAlias("exit");

        setOnlyPlayer(true);

        setDescription("Sai do mini gladiador");
    }

    @Override
    public void perform() {
        if(SolaryMiniGladiador.getMiniGladiadorManager().getStatus() == Status.CLOSED) {
            sendMessage("§cO mini gladiador não está acontecendo");
            return;
        }

        if(SolaryMiniGladiador.getMiniGladiadorManager().getStatus() == Status.WAITING) {
            if(SolaryMiniGladiador.getMiniGladiadorManager().getPlayers().stream().anyMatch(playerMG -> playerMG.getPlayer().equals(getPlayer()))){
                SolaryMiniGladiador.getMiniGladiadorManager().removePlayer(getPlayer(), RemoveCause.LEAVE);
            }else{
                sendMessage("§cVocê não está participando do mini gladiador");
            }
        }else{
            sendMessage("§cVocê não pode sair durante o mini gladiador");
        }
    }

}
