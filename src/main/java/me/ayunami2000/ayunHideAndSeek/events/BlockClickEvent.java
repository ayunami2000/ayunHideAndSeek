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
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

public class BlockClickEvent implements Listener {
    @EventHandler
    public void onInteract(PlayerAnimationEvent event) {
        PlayerAnimationType animation = event.getAnimationType();
        Player player = event.getPlayer();
        Block block = player.getTargetBlock(null, 3);

        if (!animation.equals(PlayerAnimationType.ARM_SWING)) return;

        GameHandler game = GameHandler.getGame(player);
        if (game == null) return;
        if (game.state != GameState.STARTED) return;
        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
        if (gamePlayer == null) return;
        if (!gamePlayer.isSeeker) return;

        GamePlayer clickedPlayer = GamePlayer.getPlayerFromBlock(block);
        if (clickedPlayer == null) return;
        if (!clickedPlayer.isHidden) return;

        clickedPlayer.isSeeker = true;
        clickedPlayer.player.teleport(game.spawn);
        clickedPlayer.isHidden = false;
        for (Player pl : game.players) {
            if (GamePlayer.getPlayer(pl).isSeeker) {
                pl.showPlayer(clickedPlayer.player);
                GameHandler.forceUpdatePlayer(pl, clickedPlayer.player);
            }
        }
        clickedPlayer.block.setType(Material.AIR);
        MessageHandler.sendMessage(clickedPlayer.player, "nowSeeker");
        MessageHandler.sendMessage(player, "foundPlayer", clickedPlayer.player.getName());
    }
}
