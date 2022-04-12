package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import me.ayunami2000.ayunHideAndSeek.game.GameState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerClickEvent implements Listener {
    @EventHandler
    public void onPlayerClick(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if (!(damager instanceof Player))return;
        Entity damaging = event.getEntity();
        if (!(damaging instanceof Player))return;

        Player player = (Player) damager;
        Player clickedPl = (Player) damaging;

        GameHandler game = GameHandler.getGame(player);
        if (game == null) return;
        if (game.state != GameState.STARTED) return;
        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
        if (gamePlayer == null) return;
        GamePlayer clickedPlayer = GamePlayer.getPlayer(clickedPl);
        if (clickedPlayer == null) return;
        if (gamePlayer.isSeeker) {
            if (clickedPlayer.isHidden) return;

            clickedPlayer.isSeeker = true;
            clickedPlayer.player.teleport(game.spawn);
            MessageHandler.sendMessage(clickedPlayer.player, "nowSeeker");
            MessageHandler.sendMessage(player, "foundPlayer", clickedPlayer.player.getName());
        }else{
            //clicked seeker, check if hidden and if so count as normal, if not hidden though then do something idk
            if (gamePlayer.isHidden) return; //normal

            clickedPlayer.player.setMaxHealth(clickedPlayer.player.getMaxHealth() - 3);
        }
    }
}
