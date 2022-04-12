package me.ayunami2000.ayunHideAndSeek.game;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameThread implements Runnable{
    private GameHandler game;
    private long startTime;
    private long endTime;

    public GameThread(GameHandler g){
        game = g;
    }

    @Override
    public void run() {
        //sort out seekers
        int i = 0;
        int seekerCount = Math.min(1, game.players.size() / 4);
        List<Player> playerList = new ArrayList<>(game.players);
        Collections.shuffle(playerList);
        for (Player player : playerList) {
            if (i > seekerCount) break;
            GamePlayer.getPlayer(player).isSeeker = true;
            i++;
        }
        //tp non-seekers into arena
        for (Player player : game.players) {
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            if (GamePlayer.getPlayer(player).isSeeker) {
                player.getInventory().addItem(new ItemStack(Material.STICK));
            }else{
                player.teleport(game.spawn);
                player.getInventory().addItem(new ItemStack(Material.WOOD), new ItemStack(Material.LEAVES), new ItemStack(Material.COBBLESTONE), new ItemStack(Material.TNT), new ItemStack(Material.SAND));
            }
        }
        //announce start of 30s
        for (Player player : game.players){
            MessageHandler.sendMessage(player, "seekerCountdown", 30);
        }
        //set start time
        startTime = System.currentTimeMillis();
        //create jail
        Location loc = game.spawn.clone();
        loc.setY(300);
        //jail seekers
        for (Player player : game.players){
            GamePlayer gamePlayer = GamePlayer.getPlayer(player);
            if (gamePlayer.isSeeker){
                gamePlayer.isFrozen = true;
                player.setAllowFlight(true);
                player.setFlying(true);
                player.teleport(loc);
            }
        }
        //game loop
        while(game.state == GameState.STARTED){
            int seekersLeft = 0;
            int hidersLeft = game.players.size();
            for (Player player : game.players) {
                if (GamePlayer.getPlayer(player).isSeeker) seekersLeft++;
            }
            hidersLeft -= seekersLeft;
            //if no seekers then end game
            if (seekersLeft == 0){
                for (Player player : game.players) {
                    game.leaveGame(player);
                    MessageHandler.sendMessage(player, "hidersWin");
                }
                game.end();
                break;
            }
            //if no hiders then end game
            if (hidersLeft == 0){
                for (Player player : game.players) {
                    game.leaveGame(player);
                    MessageHandler.sendMessage(player, "seekersWin");
                }
                game.end();
                break;
            }
            //keep frozen
            for (Player player : game.players){
                if (GamePlayer.getPlayer(player).isFrozen){
                    player.setFlying(true);
                }
            }
            //seeker timer
            if (startTime != -1) {
                long secsLeft = (30000 - (System.currentTimeMillis() - startTime)) / 1000L;
                /*
                for (Player player : game.players){
                    MessageHandler.sendMessage(player, "seekerCountdown", secsLeft);
                }
                */
                if (secsLeft <= 0) {
                    startTime = -1;
                    endTime = System.currentTimeMillis();
                    //tp seekers
                    for (Player player : game.players) {
                        GamePlayer gamePlayer = GamePlayer.getPlayer(player);
                        MessageHandler.sendMessage(player, "seekerCountdown", 0);
                        if (gamePlayer.isSeeker) {
                            gamePlayer.isFrozen = false;
                            player.setFallDistance(0);
                            player.setFlying(false);
                            player.setAllowFlight(false);
                            player.teleport(game.spawn);
                        }
                    }
                }
            }else if(System.currentTimeMillis() - endTime > 300000){ //5mins
                for (Player player : game.players) {
                    MessageHandler.sendMessage(player, "timeUp");
                }
                game.end();
                break;
            }
            //keep everyone healed + ghost air block
            for (Player player : game.players){
                player.setHealth(20);
                player.setFoodLevel(20);
                GamePlayer gamePlayer = GamePlayer.getPlayer(player);
                if (!gamePlayer.isSeeker && gamePlayer.isHidden){
                    player.sendBlockChange(gamePlayer.block.getLocation(), Material.AIR.getId(), (byte) 0); // todo: check if this works!!
                }
                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potionEffect.getType());
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
        for (Player player : game.players) {
            game.leaveGame(player);
            MessageHandler.sendMessage(player, "gameOver");
        }
    }
}