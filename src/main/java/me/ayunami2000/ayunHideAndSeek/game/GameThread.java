package me.ayunami2000.ayunHideAndSeek.game;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class GameThread implements Runnable{
    private GameHandler game;
    private long startTime;

    public GameThread(GameHandler g){
        game = g;
    }

    @Override
    public void run() {
        //sort out seekers
        int i = 0;
        int seekerCount = Math.min(1, game.players.size() / 4);
        for (Player player : game.players) {
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
                game.end();
                return;
            }
            //if no hiders then end game
            if (hidersLeft == 0){
                game.end();
                return;
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
                    //tp seekers
                    for (Player player : game.players) {
                        MessageHandler.sendMessage(player, "seekerCountdown", 0);
                        if (GamePlayer.getPlayer(player).isSeeker) player.teleport(game.spawn);
                    }
                }
            }
            //keep everyone healed
            for (Player player : game.players){
                player.setHealth(20);
                player.setFoodLevel(20);
                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potionEffect.getType());
                }
            }
            /*
            try {
                Thread.sleep(1000); // why not every second, easier on the server anyways...
            } catch (InterruptedException ignored) {}
            */
        }
        for (Player player : game.players) {
            if (GamePlayer.getPlayer(player).isSeeker) {
                for (Player pl : game.players) {
                    player.showPlayer(pl);
                }
            }
            MessageHandler.sendMessage(player, "gameOver");
        }
    }
}