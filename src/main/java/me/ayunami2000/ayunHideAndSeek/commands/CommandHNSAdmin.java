package me.ayunami2000.ayunHideAndSeek.commands;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHNSAdmin implements CommandExecutor {
    private static String PERMISSION = "ayunhideandseek.admin";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            MessageHandler.sendMessage(sender, "noConsole"); // todo: allow console to use a few commands
            return true;
        }

        if (!(sender.isOp() || sender.hasPermission(PERMISSION))) {
            MessageHandler.sendMessage(sender, "noPermission", PERMISSION);
            return true;
        }

        Player player = (Player) sender;

        GameHandler game = GameHandler.getGame(player);

        String subCmd = args.length == 0 ? "help" : args[0].toLowerCase();
        switch (subCmd){
            case "help":
            case "?":
                MessageHandler.sendMessage(sender, "helpAdmin");
                break;
            case "create":
                if (GameHandler.createGame(player) == null){
                    MessageHandler.sendMessage(sender, "cannotCreateGame");
                }else{
                    MessageHandler.sendMessage(sender, "createGame");
                }
                break;
            case "start":
                if (game == null){
                    MessageHandler.sendMessage(sender, "notInGame");
                }else{
                    if(game.start()){
                        MessageHandler.sendMessage(sender, "startGame");
                    }else{
                        MessageHandler.sendMessage(sender, "cannotStartGame");
                    }
                }
                break;
            case "stop":
                if (game == null){
                    MessageHandler.sendMessage(sender, "notInGame");
                }else{
                    game.end();
                    MessageHandler.sendMessage(sender, "endGame");
                }
                break;
            case "stopall":
                GameHandler.endAll();
                MessageHandler.sendMessage(sender, "endAllGames");
                break;
            case "spawn":
                if (game == null){
                    MessageHandler.sendMessage(sender, "notInGame");
                }else{
                    game.spawn = player.getLocation();
                    MessageHandler.sendMessage(sender, "setSpawn");
                }
                break;
            default:
                MessageHandler.sendMessage(sender, "unknownArgs");
        }
        return true;
    }
}
