package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
        if (gamePlayer != null && gamePlayer.block != null) gamePlayer.block.setType(Material.AIR);
        GameHandler game = GameHandler.leaveCurrentGame(player);
        if (game == null) return;
        for (Player pl : game.players) {
            MessageHandler.sendMessage(pl, "playerLeft", player.getName());
        }
    }
}