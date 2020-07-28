package br.kaiosouza.minigladiador.models;

import lombok.Data;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class CommandBase {

    private List<String> aliases;
    private List<Parameter> requiredParameters;
    private List<String> permissions;

    private String description;

    private CommandSender sender;
    private boolean onlyPlayer;
    private Player player;

    private List<String> parameters;

    protected CommandBase() {
        aliases = new ArrayList<>();
        requiredParameters = new ArrayList<>();
        permissions = new ArrayList<>();

        description = "Sem descrição";
    }

    public void execute(CommandSender sender, List<String> parameters) {
        this.sender = sender;
        this.parameters = parameters;

        if (!canPerform()) return;
        if (this.onlyPlayer) this.player = (Player)sender;

        perform();
    }

    private boolean canPerform(){
        if(this.onlyPlayer && !(sender instanceof  Player)) {
            sendMessage("§cEste comando pode ser executado somente por jogadores");
            return false;
        }

        if(!hasPermission()){
            sendMessage("§cVocê não possui permissão para utilizar este comando");
            return false;
        }

        if (parameters.size() < requiredParameters.size()) {
            sendMessage("REPLACE");
            return false;
        }

        return true;
    }

    protected void perform() {}

    protected void sendMessage(String message) {
        sender.sendMessage(message);
    }

    protected void addAlias(String alias){
        aliases.add(alias);
        permissions.add("minigladiador.command." + alias);
    }

    private boolean hasPermission() {
        for(String permission : permissions) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }


    protected final void setDescription(final String description) { this.description = description; }
}
