package me.ayunami2000.ayunHideAndSeek;

import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSAdmin;
import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSJoin;
import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSLeave;
import me.ayunami2000.ayunHideAndSeek.events.BlockClickEvent;
import me.ayunami2000.ayunHideAndSeek.events.SneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;

    public void onLoad() {
        plugin = this;
    }

    public void onEnable() {
        MessageHandler.initMessages();

        getServer().getPluginManager().registerEvents(new BlockClickEvent(), this);
        getServer().getPluginManager().registerEvents(new SneakEvent(), this);

        getCommand("hnsadmin").setExecutor(new CommandHNSAdmin());
        getCommand("hnsjoin").setExecutor(new CommandHNSJoin());
        getCommand("hnsleave").setExecutor(new CommandHNSLeave());
    }

    public void onDisable() {

    }
}
