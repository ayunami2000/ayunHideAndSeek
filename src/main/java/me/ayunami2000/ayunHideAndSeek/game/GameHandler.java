package me.ayunami2000.ayunHideAndSeek.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GameHandler {
    public static Set<GameHandler> games = new HashSet<>();

    public Set<Player> players = new HashSet<>();

    //todo: allow multiple spawns
    public Location spawn = null;

    public GameState state = GameState.LOBBY;

    public static GameHandler createGame(Player player){
        for (GameHandler game : games) {
            if (game.players.contains(player)){
                return null;
            }
        }
        GameHandler gh = new GameHandler();
        games.add(gh);
        gh.spawn = player.getLocation();
        gh.joinGame(player);
        return gh;
    }

    public static boolean joinAnyGame(Player player){
        if (games.size() == 0){
            return false;
        }
        return games.stream().sorted(Comparator.comparing(game -> game.players.size())).collect(Collectors.toList()).get(0).joinGame(player);
    }

    public boolean joinGame(Player player){
        for (GameHandler game : games) {
            if (game.players.contains(player)){
                return false;
            }
        }
        if (this.state == GameState.LOBBY){
            new GamePlayer(player);
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
            GamePlayer.players.remove(player.getUniqueId());
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

    public boolean start(){
        if (players.size() < 2){
            return false;
        }
        if (spawn == null){
            return false;
        }
        state = GameState.STARTED;
        new Thread(new GameThread(this)).start();
        return true;
    }

    public static void endAll(){
        Set<GameHandler> snapshot = new HashSet<>(games);
        for (GameHandler game : snapshot) {
            game.end();
        }
    }

    public void end(){
        state = GameState.ENDED;
        for (Player player : players) {
            GamePlayer.players.remove(player.getUniqueId());
        }
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
