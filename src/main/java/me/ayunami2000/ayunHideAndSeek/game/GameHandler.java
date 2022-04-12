package me.ayunami2000.ayunHideAndSeek.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftEntity;
import org.bukkit.entity.Player;

import java.util.*;
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

    public static GameHandler joinAnyGame(Player player){
        if (games.size() == 0){
            return null;
        }
        return games.stream().sorted(Comparator.comparing(game -> game.players.size())).collect(Collectors.toList()).get(0).joinGame(player);
    }

    public GameHandler joinGame(Player player){
        for (GameHandler game : games) {
            if (game.players.contains(player)){
                return null;
            }
        }
        if (this.state == GameState.LOBBY){
            new GamePlayer(player);
            return players.add(player) ? this : null;
        }
        return null;
    }

    public GameHandler leaveGame(Player player){
        if (players.contains(player)){
            GamePlayer gamePlayer = GamePlayer.getPlayer(player);
            if (gamePlayer.isSeeker) {
                for (Player pl : players) {
                    pl.showPlayer(player);
                    forceUpdatePlayer(pl, player);
                }
            }
            players.remove(player);
            if (gamePlayer.block != null) gamePlayer.block.setType(Material.AIR);
            GamePlayer.players.remove(player.getUniqueId());
            player.getInventory().clear();
            player.setMaxHealth(20);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setHealth(0); // respawn to change position
            if (players.size() == 0 && state == GameState.LOBBY) end();
            return this;
        }
        return null;
    }

    public static GameHandler leaveCurrentGame(Player player){
        for (GameHandler game : games) {
            if (game.players.contains(player)){
                return game.leaveGame(player);
            }
        }
        return null;
    }

    public boolean start(){
        if (state != GameState.LOBBY) return false;
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
        if (state == GameState.LOBBY){
            for (Player player : players) {
                GamePlayer.players.remove(player.getUniqueId());
            }
        }
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

    public static void forceUpdatePlayer(Player playerFor, Player player){
        ((CraftWorld) playerFor.getWorld()).getHandle().entityJoinedWorld(((CraftEntity) player).getHandle(), false);
    }
}
