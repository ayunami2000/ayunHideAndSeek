package me.ayunami2000.ayunHideAndSeek;

import me.ayunami2000.ayunHideAndSeek.commands.*;
import me.ayunami2000.ayunHideAndSeek.events.*;
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
        getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        getServer().getPluginManager().registerEvents(new PlayerClickEvent(), this);
        getServer().getPluginManager().registerEvents(new PlaceEvent(), this);

        getCommand("hnsadmin").setExecutor(new CommandHNSAdmin());
        getCommand("hnsjoin").setExecutor(new CommandHNSJoin());
        getCommand("hnsleave").setExecutor(new CommandHNSLeave());
        getCommand("hnsinfo").setExecutor(new CommandHNSInfo());
    }

    public void onDisable() {

    }
}
