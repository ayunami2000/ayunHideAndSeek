package me.ayunami2000.ayunHideAndSeek;

import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSAdmin;
import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSJoin;
import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSLeave;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;

    public void onLoad() {
        plugin = this;
    }

    public void onEnable() {
        MessageHandler.initMessages();

        this.getCommand("hnsadmin").setExecutor(new CommandHNSAdmin());
        this.getCommand("hnsjoin").setExecutor(new CommandHNSJoin());
        this.getCommand("hnsleave").setExecutor(new CommandHNSLeave());
    }

    public void onDisable() {

    }
}
