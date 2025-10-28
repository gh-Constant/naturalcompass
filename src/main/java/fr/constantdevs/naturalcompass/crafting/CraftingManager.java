package fr.constantdevs.naturalcompass.crafting;

import fr.constantdevs.naturalcompass.NaturalCompass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class CraftingManager {

    private final NaturalCompass plugin;

    public CraftingManager(NaturalCompass plugin) {
        this.plugin = plugin;
    }

    public void loadRecipes() {
        // Unregister old recipes to allow for reloading
        plugin.getServer().resetRecipes();

        if (!plugin.getConfigManager().isRecipesEnabled()) {
            return;
        }

        // Tier 1 Compass Recipe (Shaped)
        NamespacedKey tier1Key = new NamespacedKey(plugin, "natural_compass_tier1");
        ShapedRecipe tier1Recipe = new ShapedRecipe(tier1Key, plugin.getItemManager().createCompass(1));
        tier1Recipe.shape(" M ", "MCM", " M ");
        tier1Recipe.setIngredient('M', Material.MOSS_BLOCK);
        tier1Recipe.setIngredient('C', Material.COMPASS);
        plugin.getServer().addRecipe(tier1Recipe);

        // Tier 2 Upgrade Recipe (Shapeless)
        NamespacedKey tier2Key = new NamespacedKey(plugin, "natural_compass_tier2");
        ShapelessRecipe tier2Recipe = new ShapelessRecipe(tier2Key, plugin.getItemManager().createCompass(2));
        tier2Recipe.addIngredient(plugin.getItemManager().createCompass(1));
        tier2Recipe.addIngredient(Material.HEART_OF_THE_SEA);
        plugin.getServer().addRecipe(tier2Recipe);

        // Tier 3 Upgrade Recipe (Shapeless)
        NamespacedKey tier3Key = new NamespacedKey(plugin, "natural_compass_tier3");
        ShapelessRecipe tier3Recipe = new ShapelessRecipe(tier3Key, plugin.getItemManager().createCompass(3));
        tier3Recipe.addIngredient(plugin.getItemManager().createCompass(2));
        tier3Recipe.addIngredient(Material.AMETHYST_CLUSTER);
        plugin.getServer().addRecipe(tier3Recipe);

        // Tier 4 Upgrade Recipe (Shapeless)
        NamespacedKey tier4Key = new NamespacedKey(plugin, "natural_compass_tier4");
        ShapelessRecipe tier4Recipe = new ShapelessRecipe(tier4Key, plugin.getItemManager().createCompass(4));
        tier4Recipe.addIngredient(plugin.getItemManager().createCompass(3));
        tier4Recipe.addIngredient(Material.TOTEM_OF_UNDYING);
        plugin.getServer().addRecipe(tier4Recipe);

        // Tier 5 Upgrade Recipe (Shapeless)
        NamespacedKey tier5Key = new NamespacedKey(plugin, "natural_compass_tier5");
        ShapelessRecipe tier5Recipe = new ShapelessRecipe(tier5Key, plugin.getItemManager().createCompass(5));
        tier5Recipe.addIngredient(plugin.getItemManager().createCompass(4));
        tier5Recipe.addIngredient(Material.NETHER_STAR);
        plugin.getServer().addRecipe(tier5Recipe);
    }
}