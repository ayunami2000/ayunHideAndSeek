package me.ayunami2000.ayunHideAndSeek.commands;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHNSJoin implements CommandExecutor {
    private static String PERMISSION = "ayunhideandseek.player.join";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            MessageHandler.sendMessage(sender, "noConsole");
            return true;
        }
        if (!(sender.isOp() || sender.hasPermission(PERMISSION))) {
            MessageHandler.sendMessage(sender, "noPermission", PERMISSION);
            return true;
        }

        if (GameHandler.joinAnyGame((Player)sender)){
            MessageHandler.sendMessage(sender, "joinedGame");
        }else{
            MessageHandler.sendMessage(sender, "noGamesAvail");
        }
        return true;
    }
}