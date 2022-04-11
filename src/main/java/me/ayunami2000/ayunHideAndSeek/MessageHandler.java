package me.ayunami2000.ayunHideAndSeek;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;

public class MessageHandler {
    public static HashMap<String, String> messageData = new HashMap<>();

    public static void initMessages(String pathname) {
        File f = new File(pathname);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(f);
        for (String message : config.getConfigurationSection("").getKeys(false)) {
            if (config.isString(message)) {
                messageData.put(message, config.getString(message));
            }else if(config.isList(message)){
                messageData.put(message, StringUtils.join(config.getStringList(message), "\n"));
            }
        }
    }

    public static String getMessage(String key, Object... args){
        return MessageFormat.format(messageData.getOrDefault(key, key), args);
    }

    public static void sendMessage(CommandSender commandSender, String key, Object... args){
        commandSender.sendMessage(getMessage(key, args));
    }
}
