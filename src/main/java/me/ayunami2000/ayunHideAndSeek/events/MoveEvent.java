package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import me.ayunami2000.ayunHideAndSeek.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        GameHandler game = GameHandler.getGame(player);
        if (game == null) return;
        if (game.state != GameState.STARTED) return;
        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
        if (gamePlayer == null) return;
        if (gamePlayer.isSeeker) {
            if (gamePlayer.isFrozen){
                event.setCancelled(true);
            }
            return;
        }

        if (gamePlayer.block == null) return;

        if (gamePlayer.block.getLocation().equals(player.getLocation().getBlock().getLocation())) return;

        if (gamePlayer.isHidden){
            gamePlayer.isHidden = false;
            for (Player pl : game.players) {
                if (GamePlayer.getPlayer(pl).isSeeker) {
                    pl.showPlayer(player);
                    GameHandler.forceUpdatePlayer(pl, player);
                }
            }
            gamePlayer.block.setType(Material.AIR);
            MessageHandler.sendMessage(player, "noLongerHidden");
        }
    }
}
