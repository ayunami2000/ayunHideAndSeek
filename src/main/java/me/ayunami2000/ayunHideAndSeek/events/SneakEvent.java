package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import me.ayunami2000.ayunHideAndSeek.game.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

        Block standingInBlock = player.getLocation().getBlock();
        if (standingInBlock.getType() != Material.AIR) {
            MessageHandler.sendMessage(player, "invalidSpot");
            return;
        }
        //toggle hidden as block
        if (gamePlayer.toggleHidden()){
            if (gamePlayer.isHidden) {
                Material itemType = player.getItemInHand().getType();
                if (!itemType.isBlock()) itemType = Material.WOOD;
                standingInBlock.setType(itemType);
                gamePlayer.block = standingInBlock;
                player.sendBlockChange(standingInBlock.getLocation(), Material.AIR, (byte) 0); // todo: check if this works!!
                for (Player pl : game.players) {
                    if (GamePlayer.getPlayer(pl).isSeeker) pl.hidePlayer(player);
                }
            }else{
                for (Player pl : game.players) {
                    if (GamePlayer.getPlayer(pl).isSeeker) pl.showPlayer(player);
                }
                gamePlayer.block.setType(Material.AIR);
            }
            MessageHandler.sendMessage(player, gamePlayer.isHidden ? "nowHidden" : "noLongerHidden");
        }else{
            MessageHandler.sendMessage(player, "hideCooldown");
        }
    }
}
