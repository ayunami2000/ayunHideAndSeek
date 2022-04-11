package me.ayunami2000.ayunHideAndSeek.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GamePlayer {
    public static final Map<UUID, GamePlayer> players = new HashMap<>();
    public final Player player;
    public Block block = null;
    public long lastHideChange = 0;
    public boolean isSeeker = false;
    public boolean isHidden = false;

    public GamePlayer(Player pl){
        player = pl;
        players.put(player.getUniqueId(), this);
    }

    public static GamePlayer getPlayer(Player pl){
        UUID uuid = pl.getUniqueId();
        if (players.containsKey(uuid)){
            return players.get(uuid);
        }
        return null;
    }

    public boolean toggleHidden(){
        if (System.currentTimeMillis() - lastHideChange < 2000){
            return false;
        }
        lastHideChange = System.currentTimeMillis();
        isHidden = !isHidden;
        return true;
    }

    public static GamePlayer getPlayerFromBlock(Block bl){
        for (GamePlayer gp : players.values()) {
            if (gp.block.getLocation().equals(bl.getLocation())) return gp;
        }
        return null;
    }
}
