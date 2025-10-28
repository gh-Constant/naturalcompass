package fr.constantdevs.naturalcompass.config;

import fr.constantdevs.NaturalCompass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {
    private final NaturalCompass plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(NaturalCompass plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config.yml: " + e.getMessage());
        }
    }

    public boolean isUseExternalBiomes() {
        return config.getBoolean("use-external-biomes", true);
    }

    public void setUseExternalBiomes(boolean useExternalBiomes) {
        config.set("use-external-biomes", useExternalBiomes);
        saveConfig();
    }

    public List<String> getExcludeBiomes() {
        return config.getStringList("exclude-biomes");
    }

    public void setExcludeBiomes(List<String> excludeBiomes) {
        config.set("exclude-biomes", excludeBiomes);
        saveConfig();
    }

    public void addExcludeBiome(String biome) {
        List<String> excludeBiomes = getExcludeBiomes();
        excludeBiomes.add(biome);
        setExcludeBiomes(excludeBiomes);
    }

    public void removeExcludeBiome(String biome) {
        List<String> excludeBiomes = getExcludeBiomes();
        excludeBiomes.remove(biome);
        setExcludeBiomes(excludeBiomes);
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getMessage(String key) {
        return config.getString("messages." + key, "Message not found: " + key);
    }
    public boolean isDisplayCoords() {
        return config.getBoolean("search.display-coords", true);
    }

    public int getSearchTimeout() {
        return config.getInt("search.search-timeout", 60);
    }

    public int getSearchRadius() {
        return config.getInt("search.radius", 10000);
    }

    public int getMaxAttempts() {
        return config.getInt("search.max-attempts", 100);
    }
}