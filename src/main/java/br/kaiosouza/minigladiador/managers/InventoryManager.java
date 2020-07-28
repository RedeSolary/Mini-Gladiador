package br.kaiosouza.minigladiador.managers;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryManager {

    List<ItemStack> inventory;
    List<ItemStack> armor;

    private final SolaryMiniGladiador plugin;
    public InventoryManager(SolaryMiniGladiador plugin){
        this.plugin = plugin;

        loadItems();
    }

    public void setItems(Player player){
        inventory = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        armor = new ArrayList<>(Arrays.asList(player.getInventory().getArmorContents()));

        plugin.getConfig().set("Itens.inventario", inventory);
        plugin.getConfig().set("Itens.armadura", armor);

        plugin.saveConfig();
    }

    public void giveItems(Player player){
        player.getInventory().setArmorContents(armor.toArray(new ItemStack[0]));
        player.getInventory().setContents(inventory.toArray(new ItemStack[0]));
    }

    private void loadItems(){
        if(plugin.getConfig().contains("Itens.inventario")) inventory = (List<ItemStack>) plugin.getConfig().get("Itens.inventario");
        if(plugin.getConfig().contains("Itens.armadura"))armor = (List<ItemStack>) plugin.getConfig().get("Itens.armadura");
    }

}
