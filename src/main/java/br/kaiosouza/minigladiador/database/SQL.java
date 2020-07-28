package br.kaiosouza.minigladiador.database;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.sql.*;

public class SQL {

    private String user, host, database, password;
    private Connection connection;
    private Statement statement;
    private final SQLType sqlType;
    private File db;

    public final SolaryMiniGladiador plugin;

    public SQL(String user, String password, String host, String database, SQLType sqlType, SolaryMiniGladiador plugin) {
        this.plugin = plugin;
        this.user = user;
        this.password = password;
        this.host = host;
        this.database = database;
        this.sqlType = sqlType;
    }

    public SQL(String database, File folder, SQLType sqlType, SolaryMiniGladiador plugin){
        this.plugin = plugin;
        this.db = folder;
        this.database = database;
        this.sqlType = sqlType;
    }

    public void startConnection(){
        if(getType().equals(SQLType.MySQL)){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database + "", user, password);
                statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS MiniGladiador (clan VARCHAR(32) NOT NULL, WINS INT(4) NOT NULL DEFAULT 0, LOSES INT(4) NOT NULL DEFAULT 0)");
            } catch (SQLException | ClassNotFoundException e) {
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
            }
        }else if(getType().equals(SQLType.SQLite)){
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath() + File.separator + database+".db");
                statement = connection.createStatement();
                statement.execute("CREATE TABLE IF NOT EXISTS MiniGladiador (clan VARCHAR(32) NOT NULL, wins INT(4) NOT NULL DEFAULT 0, loses INT(4) NOT NULL DEFAULT 0)");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateClan(Clan clan, boolean winner){
        checkConnection();

        new BukkitRunnable() {
            @Override
            public void run() {
                PreparedStatement ps = null;
                try {
                    ps = connection.prepareStatement("SELECT * FROM MiniGladiador WHERE clan = ?");
                    ps.setString(1, clan.getName());
                    final ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int wins = rs.getInt("wins");
                        int loses = rs.getInt("loses");

                        ps.close();

                        if(winner) {
                            ps = connection.prepareStatement("UPDATE MiniGladiador SET wins=? WHERE clan=?");
                            ps.setInt(1, ++wins);
                        }else{
                            ps = connection.prepareStatement("UPDATE MiniGladiador SET loses=? WHERE clan=?");
                            ps.setInt(1, ++loses);
                        }

                        ps.setString(2, clan.getName());
                        ps.executeUpdate();
                    }else {
                        ps = connection.prepareStatement("INSERT INTO MiniGladiador(clan, wins, loses) VALUES (?,?,?)");
                        ps.setString(1, clan.getName());
                        ps.setInt(2, winner ? 1 : 0);
                        ps.setInt(3, winner ? 0 : 1);
                        ps.execute();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if(ps != null) ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    public void closeConnection(){
        try {
            this.statement.close();
            this.connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public SQLType getType(){
        return sqlType;
    }

    public enum SQLType{
        MySQL, SQLite
    }

    public void checkConnection(){
        try {
            if(this.connection.isClosed() || this.connection == null){
                startConnection();
                Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] A conexão com o MySQL foi restabelecida");
            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] Erro ao checar a conexão: ");
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
