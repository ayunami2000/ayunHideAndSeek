package me.ayunami2000.ayunHideAndSeek.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GamePlayer {
    public static Map<UUID, GamePlayer> players = new HashMap<>();
    public final Player player;
    public Block block = null;
    public long lastBlockChange = 0;

    public GamePlayer(Player pl){
        player = pl;
        players.put(player.getUniqueId(), this);
    }

    public static GamePlayer getPlayer(Player pl){
        UUID uuid = pl.getUniqueId();
        if (players.containsKey(uuid)){
            players.get(uuid);
        }
        return null;
    }

    public boolean setBlock(Block bl){
        if (lastBlockChange < 5000){
            return false;
        }
        lastBlockChange = System.currentTimeMillis();
        block = bl;
        return true;
    }
}
