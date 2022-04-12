package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import me.ayunami2000.ayunHideAndSeek.game.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnEvent implements Listener {
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();

        GameHandler game = GameHandler.getGame(player);
        if (game == null) return;
        if (game.state != GameState.STARTED) return;
        new Thread(() -> {
            player.setMaxHealth(20);
            Location loc = game.spawn.clone();
            loc.setY(300);
            GamePlayer gamePlayer = GamePlayer.getPlayer(player);
            gamePlayer.isFrozen = true;
            player.setFlying(true);
            player.teleport(loc);
            for(int i=0;i<10;i++){
                MessageHandler.sendMessage(player, "respawnTimer", 10 - i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
            gamePlayer.isFrozen = false;
            player.setFallDistance(0);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.teleport(game.spawn);
        }).start();
    }
}
