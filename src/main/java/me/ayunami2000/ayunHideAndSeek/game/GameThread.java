package me.ayunami2000.ayunHideAndSeek.game;

import org.bukkit.entity.Player;

public class GameThread implements Runnable{
    private GameHandler game;

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
            //todo: event listeners (block mine, player kill, disguise change)
            /*
            try {
                Thread.sleep(1000); // why not every second, easier on the server anyways...
            } catch (InterruptedException ignored) {}
            */
        }
    }
}