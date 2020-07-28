package br.kaiosouza.minigladiador.commands;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.models.CommandBase;
import br.kaiosouza.minigladiador.models.Parameter;

public class ItemsCommand extends CommandBase {

    public ItemsCommand() {
        addAlias("itens");

        setOnlyPlayer(true);

        setDescription("Definir ou visualizar os itens");

        getRequiredParameters().add(new Parameter("definir", "pegar"));
    }

    @Override
    public void perform() {
        String option = getParameters().get(0);

        if(option.equalsIgnoreCase("definir")){
            SolaryMiniGladiador.getInventoryManager().setItems(getPlayer());
            sendMessage("§aVocê definiu os items com sucesso");
        }else if(option.equalsIgnoreCase("pegar")){
            SolaryMiniGladiador.getInventoryManager().giveItems(getPlayer());
        }
    }

}
