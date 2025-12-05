package ru.traiwy.customEnchantingTable.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.customEnchantingTable.data.ConfigData;

import java.io.*;
import java.nio.charset.StandardCharsets;

@FieldDefaults(makeFinal = true)
public class ConfigManager {
    JavaPlugin plugin;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Getter @NonFinal
    ConfigData configData;
    File config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = new File(plugin.getDataFolder(), "config.json");
        parse();
    }

    private void parse(){
        try{
            if(!plugin.getDataFolder().exists()){
                plugin.getDataFolder().mkdirs();
            }

            if(!config.exists()){
                createJsonFile();
            }

            try (Reader reader = new InputStreamReader(new FileInputStream(config), StandardCharsets.UTF_8)){
                configData = gson.fromJson(reader, ConfigData.class);
                Bukkit.getLogger().info("Конфиг загружен");

            }
        } catch (Exception e) {
            configData = new ConfigData();
        }
    }

    private void createJsonFile(){
        try {
            config.getParentFile().mkdirs();
            config.createNewFile();

            ConfigData defaultConfig = new ConfigData();

            try (FileWriter writer =  new FileWriter(config)){
                 gson.toJson(defaultConfig, writer);
                Bukkit.getLogger().info("Конфиг создан");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
