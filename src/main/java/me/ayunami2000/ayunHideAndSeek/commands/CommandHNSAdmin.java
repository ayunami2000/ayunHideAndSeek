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

        String subCmd = args.length == 0 ? "help" : args[0].toLowerCase();
        switch (subCmd){
            case "help":
            case "?":
                MessageHandler.sendMessage(sender, "helpAdmin");
                break;
            case "create":

                break;
            case "start":

                break;
            case "stop":
                GameHandler game = GameHandler.getGame(player);
                game.end();
                break;
            case "stopall":
                GameHandler.endAll();
                break;
            case "spawn":

                break;
            case "corner1":

                break;
            case "corner2":

                break;
            default:
                MessageHandler.sendMessage(sender, "unknownArgs");
        }
        return true;
    }
}
