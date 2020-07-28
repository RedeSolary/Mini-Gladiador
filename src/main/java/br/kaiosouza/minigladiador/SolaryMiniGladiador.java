package br.kaiosouza.minigladiador;

import br.kaiosouza.minigladiador.database.SQL;
import br.kaiosouza.minigladiador.database.SQL.SQLType;
import br.kaiosouza.minigladiador.listeners.MiniGladidadorListener;
import br.kaiosouza.minigladiador.managers.CommandManager;
import br.kaiosouza.minigladiador.managers.InventoryManager;
import br.kaiosouza.minigladiador.managers.LocationManager;
import br.kaiosouza.minigladiador.managers.MiniGladiadorManager;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SolaryMiniGladiador extends JavaPlugin {

    static @Getter private SQL database;
    static @Getter private MiniGladiadorManager miniGladiadorManager;
    static @Getter private InventoryManager inventoryManager;
    static @Getter private LocationManager locationManager;
    static @Getter private ClanManager clans;
    static @Getter private Economy economy;

    @Override
    public void onEnable() {
        setupDatabase();
        setupEconomy();
        setupClans();

        miniGladiadorManager = new MiniGladiadorManager(this);
        inventoryManager = new InventoryManager(this);
        locationManager = new LocationManager(this);
        new CommandManager(this);

        new MiniGladidadorListener(this);
    }

    @Override
    public void onDisable() {
        database.closeConnection();
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

        if (economyProvider == null) {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] §cVault não encontrado plugin desabilitado");
            Bukkit.getPluginManager().disablePlugin(this);
        }else {
            economy = economyProvider.getProvider();
        }
    }

    private void setupClans(){
        Plugin p = Bukkit.getPluginManager().getPlugin("SimpleClans");
        if(p != null && p.isEnabled()) {
            clans = SimpleClans.getInstance().getClanManager();
        }else {
            Bukkit.getConsoleSender().sendMessage("[" + this.getName() + "] §cSimpleClans não encontrado plugin desabilitado");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void setupDatabase(){
        if (getConfig().getBoolean("MySQL.Ativar")) {
            String user, password, host, databaseName;
            user = getConfig().getString("MySQL.User");
            password = getConfig().getString("MySQL.Password");
            host = getConfig().getString("MySQL.Host");
            databaseName = getConfig().getString("MySQL.Database");
            database = new SQL(user, password, host, databaseName, SQLType.MySQL, this);
        } else {
            if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();
            database = new SQL(this.getName(), getDataFolder(), SQLType.SQLite, this);
        }

        database.startConnection();
    }
}
