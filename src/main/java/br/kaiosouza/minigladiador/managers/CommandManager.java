package br.kaiosouza.minigladiador.managers;

import br.kaiosouza.minigladiador.SolaryMiniGladiador;
import br.kaiosouza.minigladiador.commands.ExitCommand;
import br.kaiosouza.minigladiador.commands.ItemsCommand;
import br.kaiosouza.minigladiador.commands.LocationSetterCommand;
import br.kaiosouza.minigladiador.commands.StartCommand;
import br.kaiosouza.minigladiador.models.CommandBase;
import br.kaiosouza.minigladiador.models.Parameter;
import br.kaiosouza.minigladiador.type.Status;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager implements CommandExecutor {

    private final Set<CommandBase> commands;

    public CommandManager(SolaryMiniGladiador plugin) {
        commands = new HashSet<>();

        plugin.getCommand("minigladiador").setExecutor(this);

        commands.add(new ExitCommand());
        commands.add(new ItemsCommand());
        commands.add(new LocationSetterCommand());
        commands.add(new StartCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<String> parameters = new ArrayList<>(Arrays.asList(args));
        return handleCommand(sender, command, parameters);
    }

    private boolean handleCommand(CommandSender sender, Command command, List<String> parameters) {
        if (!parameters.isEmpty()) {
            String subCommand = parameters.get(0);
            parameters.remove(0);

            for (CommandBase commandBase : commands) {
                if (commandBase.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(subCommand))) {
                    commandBase.execute(sender, parameters);
                    return true;
                }
            }
        }

        if(SolaryMiniGladiador.getMiniGladiadorManager().getStatus().equals(Status.WAITING)) {
            if(sender instanceof Player){
                SolaryMiniGladiador.getMiniGladiadorManager().addPlayer((Player) sender);
                return true;
            }
        }

        sendCommandList(sender, command);
        return true;
    }

    private void sendCommandList(CommandSender sender, Command command){
        for(CommandBase commandBase : commands){
            if(!hasPermission(sender, commandBase)) continue;

            StringBuilder commandHelp = new StringBuilder();
            commandHelp.append("§a/").append(command.getLabel()).append(" ").append(commandBase.getAliases().get(0));
            for(Parameter params : commandBase.getRequiredParameters()) {
                commandHelp.append(params.getDisplay());
            }
            commandHelp.append(" §8- §7").append(commandBase.getDescription());

            sender.sendMessage(commandHelp.toString());
        }
    }

    private boolean hasPermission(CommandSender sender, CommandBase commandBase){
        for(String permission : commandBase.getPermissions()) {
            if (sender.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
}
