package br.kaiosouza.minigladiador.managers;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.utils.LocationsSerializer;
import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;

public class LocationManager {

    private final SolaryMiniGladiador plugin;

    @Getter final private HashMap<String, Location> locations;

    public LocationManager(SolaryMiniGladiador plugin){
        this.plugin = plugin;
        locations = new HashMap<>();

        loadLocations();
    }

    public void saveLocation(String locationName, Location location){
        getLocations().put(locationName, location);
        plugin.getConfig().set("Locations." + locationName, LocationsSerializer.serializeLocation(location, true));
        plugin.saveConfig();
    }

    private void loadLocations(){
        if(plugin.getConfig().contains("Locations.entrada")) getLocations().put("entrada", LocationsSerializer.deserializeLocation(plugin.getConfig().getString("Locations.entrada")));
        if(plugin.getConfig().contains("Locations.saida")) getLocations().put("saida", LocationsSerializer.deserializeLocation(plugin.getConfig().getString("Locations.entrada")));
        if(plugin.getConfig().contains("Locations.espectador")) getLocations().put("espectador", LocationsSerializer.deserializeLocation(plugin.getConfig().getString("Locations.entrada")));
   }
}
