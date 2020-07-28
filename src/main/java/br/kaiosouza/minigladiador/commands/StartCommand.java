package br.kaiosouza.minigladiador.commands;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.models.CommandBase;
import br.kaiosouza.minigladiador.type.Status;

public class StartCommand extends CommandBase {

    public StartCommand() {
        addAlias("iniciar");
        addAlias("start");

        setDescription("Inicia o gladiador");
    }

    @Override
    public void perform() {
        if(SolaryMiniGladiador.getMiniGladiadorManager().getStatus() != Status.CLOSED) {
            sendMessage("§cO mini gladiador já está acontecendo!");
        }else{
            SolaryMiniGladiador.getMiniGladiadorManager().startTimer();
        }
    }
}
