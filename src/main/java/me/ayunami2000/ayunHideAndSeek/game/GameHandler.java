package me.ayunami2000.ayunHideAndSeek.game;

import me.ayunami2000.ayunHideAndSeek.MessageHandler;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameHandler {
    public static Set<GameHandler> games = new HashSet<>();

    public Set<Player> players = new HashSet<>();

    public GameState state = GameState.LOBBY;

    public GameHandler(){
        games.add(this);
    }

    public static boolean joinAnyGame(Player player){
        if (games.size() == 0){
            return false;
        }
        return games.stream().sorted(Comparator.comparing(game -> game.players.size())).collect(Collectors.toList()).get(0).joinGame(player);
    }

    public boolean joinGame(Player player){
        if (this.state == GameState.LOBBY){
            return players.add(player);
        }
        return false;
    }

    public boolean leaveGame(Player player){
        //for now, allow players to leave mid-game. rehandle the player change dynamically
        /*
        if (state != GameState.LOBBY){
            return false;
        }
        */
        if (players.contains(player)){
            players.remove(player);
            return true;
        }
        return false;
    }

    public static boolean leaveCurrentGame(Player player){
        for (GameHandler game : games) {
            if (game.players.contains(player)){
                return game.leaveGame(player);
            }
        }
        return false;
    }

    public void start(){
        state = GameState.STARTED;
    }

    public static void endAll(){
        Set<GameHandler> snapshot = new HashSet<>(games);
        for (GameHandler game : snapshot) {
            game.end();
        }
    }

    public void end(){
        state = GameState.ENDED;
        players.clear();
        games.remove(this);
    }

    public static GameHandler getGame(Player player){
        for (GameHandler game : games) {
            if (game.players.contains(player)){
                return game;
            }
        }
        return null;
    }
}
