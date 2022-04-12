package me.ayunami2000.ayunHideAndSeek.commands;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHNSLeave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            MessageHandler.sendMessage(sender, "noConsole");
            return true;
        }

        GameHandler game = GameHandler.leaveCurrentGame((Player)sender);
        if (game != null){
            MessageHandler.sendMessage(sender, "leftGame");
            for (Player player : game.players) {
                MessageHandler.sendMessage(player, "playerLeft", sender.getName());
            }
        }else{
            MessageHandler.sendMessage(sender, "notInGame");
        }
        return true;
    }
}