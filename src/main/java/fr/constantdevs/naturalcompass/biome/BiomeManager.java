package fr.constantdevs.naturalcompass.biome;

import fr.constantdevs.naturalcompass.NaturalCompass;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BiomeManager {

    private static final List<String> allBiomeNames = new ArrayList<>();

    public static void init() {
        allBiomeNames.clear();
        allBiomeNames.addAll(Arrays.stream(Biome.values())
                .map(biome -> biome.name())
                .collect(Collectors.toList()));
    }

    public static List<String> getBiomeNames() {
        return allBiomeNames;
    }

    public static List<String> getFilteredBiomeNames(World.Environment dimension) {
        List<String> excludedBiomes = NaturalCompass.getInstance().getConfigManager().getExcludedBiomes();
        return allBiomeNames.stream()
                .filter(biomeName -> !excludedBiomes.contains(biomeName))
                .filter(biomeName -> isBiomeInDimension(biomeName, dimension))
                .collect(Collectors.toList());
    }

    private static boolean isBiomeInDimension(String biomeName, World.Environment dimension) {
        // This is a simplified check. A more robust implementation might be needed.
        return switch (dimension) {
            case NETHER -> biomeName.toLowerCase().contains("nether");
            case THE_END -> biomeName.toLowerCase().contains("end");
            default -> !biomeName.toLowerCase().contains("nether") && !biomeName.toLowerCase().contains("end");
        };
    }
}