package br.kaiosouza.minigladiador.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static boolean isInventoryEmpty(Player player){
        for(ItemStack itemStack : player.getInventory().getArmorContents()){
            if(itemStack != null && itemStack.getType() != Material.AIR) return false;
        }

        for(ItemStack itemStack : player.getInventory().getContents()){
            if(itemStack != null && itemStack.getType() != Material.AIR) return false;
        }

        return true;
    }

}
