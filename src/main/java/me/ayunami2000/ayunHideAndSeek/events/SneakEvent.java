package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import me.ayunami2000.ayunHideAndSeek.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class SneakEvent implements Listener {
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!event.isSneaking()) return; // only toggle if shift down (press shift once to hide, press again to unhide)

        GameHandler game = GameHandler.getGame(player);
        if (game == null) return;
        if (game.state != GameState.STARTED) return;
        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
        if (gamePlayer == null) return;
        if (gamePlayer.isSeeker) return;

        //toggle hidden as block
        if (gamePlayer.toggleHidden()){
            MessageHandler.sendMessage(player, gamePlayer.isHidden ? "nowHidden" : "noLongerHidden");
        }else{
            MessageHandler.sendMessage(player, "hideCooldown");
        }
    }
}
