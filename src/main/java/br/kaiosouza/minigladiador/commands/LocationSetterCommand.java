package br.kaiosouza.minigladiador.commands;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.models.CommandBase;
import br.kaiosouza.minigladiador.models.Parameter;

public class LocationSetterCommand extends CommandBase {

    public LocationSetterCommand() {
        addAlias("definir");
        addAlias("setar");
        addAlias("set");

        setOnlyPlayer(true);

        setDescription("Define as localizações");

        getRequiredParameters().add(new Parameter("entrada", "saida", "espectador"));
    }

    @Override
    public void perform() {
        String locationName = getParameters().get(0);

        SolaryMiniGladiador.getLocationManager().saveLocation(locationName, getPlayer().getLocation());

        sendMessage("§aVocê definiu a localização \"" + locationName + "\" com sucesso");
    }

}
