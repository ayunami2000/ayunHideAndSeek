package me.ayunami2000.ayunHideAndSeek;

import me.ayunami2000.ayunHideAndSeek.commands.CommandHNSAdmin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    public void onEnable() {
        MessageHandler.initMessages(getDataFolder() + File.separator + "messages.yml");

        this.getCommand("hnsadmin").setExecutor(new CommandHNSAdmin());
    }

    public void onDisable() {

    }
}
