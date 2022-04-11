package me.ayunami2000.ayunHideAndSeek.events;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import me.ayunami2000.ayunHideAndSeek.game.GameHandler;
import me.ayunami2000.ayunHideAndSeek.game.GamePlayer;
import me.ayunami2000.ayunHideAndSeek.game.GameState;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockClickEvent implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (!action.equals(Action.LEFT_CLICK_BLOCK)) return;

        GameHandler game = GameHandler.getGame(player);
        if (game == null) return;
        if (game.state != GameState.STARTED) return;
        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
        if (gamePlayer == null) return;
        if (!gamePlayer.isSeeker) return;

        GamePlayer clickedPlayer = GamePlayer.getPlayerFromBlock(block);
        if (clickedPlayer == null) return;

        //todo: implement damage + raytrace where player is looking to prevent cheating (or just...get an anticheat lmao)
        clickedPlayer.isSeeker = true;
        clickedPlayer.player.teleport(game.spawn);
        MessageHandler.sendMessage(clickedPlayer.player, "nowSeeker");
        MessageHandler.sendMessage(player, "foundPlayer", clickedPlayer.player.getName());
    }
}
