package me.ayunami2000.ayunHideAndSeek.commands;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHNSJoin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            MessageHandler.sendMessage(sender, "noConsole");
            return true;
        }

        GameHandler game = GameHandler.joinAnyGame((Player)sender);
        if (game == null){
            MessageHandler.sendMessage(sender, "noGamesAvail");
        }else{
            String playerListStr = "";
            for (Player player : game.players) {
                MessageHandler.sendMessage(player, "joinedGame", sender.getName());
                playerListStr += player.getName() + ", ";
            }
            playerListStr = playerListStr.substring(0, playerListStr.length() - 2);
            MessageHandler.sendMessage(sender, "gameInfo", game.state.name(), playerListStr);
        }
        return true;
    }
}