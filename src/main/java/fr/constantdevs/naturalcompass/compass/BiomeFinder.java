package fr.constantdevs.naturalcompass.compass;

import com.google.common.collect.Maps;
import fr.constantdevs.NaturalCompass;
import fr.constantdevs.naturalcompass.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class BiomeFinder {
    private static final ConfigManager configManager = NaturalCompass.getInstance().getConfigManager();
    private static final Map<UUID, CompletableFuture<Location>> searchTasks = Maps.newHashMap();
    private static final Map<UUID, Biome> currentBiomes = Maps.newHashMap();

    public static CompletableFuture<Location> findNearestBiome(Player player, Biome targetBiome) {
        if (searchTasks.containsKey(player.getUniqueId())) {
            searchTasks.get(player.getUniqueId()).cancel(true);
        }

        currentBiomes.put(player.getUniqueId(), targetBiome);

        Executor asyncExecutor = runnable -> NaturalCompass.getInstance().getServer().getScheduler().runTaskAsynchronously(NaturalCompass.getInstance(), runnable);

        CompletableFuture<Location> future = CompletableFuture.supplyAsync(() -> {
            try {
                Location result = player.getWorld().locateNearestBiome(player.getLocation(), targetBiome, configManager.getSearchRadius(), 16);
                if (result == null) {
                    throw new RuntimeException("Biome not found within search radius");
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException("Error locating biome: " + e.getMessage());
            }
        }, asyncExecutor).orTimeout(configManager.getSearchTimeout(), TimeUnit.SECONDS);

        searchTasks.put(player.getUniqueId(), future);
        future.whenComplete((location, throwable) -> {
            searchTasks.remove(player.getUniqueId());
            currentBiomes.remove(player.getUniqueId());
        });

        return future;
    }

    private static String formatBiomeName(String biomeName) {
        String[] words = biomeName.toLowerCase().split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return formattedName.toString().trim();
    }

    public static void stopAllTasks() {
        for (CompletableFuture<Location> future : searchTasks.values()) {
            future.cancel(true);
        }
        searchTasks.clear();
        currentBiomes.clear();
    }

    public static Biome getCurrentBiome(Player player) {
        return currentBiomes.get(player.getUniqueId());
    }

    private static boolean isExcludedBiome(Biome biome, List<String> excludedBiomes) {
        String biomeName = biome.getKey().toString();
        return excludedBiomes.contains(biomeName);
    }

    public static List<Biome> getAvailableBiomes() {
        List<Biome> biomes = new ArrayList<>();
        List<String> excludedBiomes = configManager.getExcludeBiomes();

        for (Biome biome : Registry.BIOME) {
            if (!isExcludedBiome(biome, excludedBiomes)) {
                biomes.add(biome);
            }
        }

        return biomes;
    }
}