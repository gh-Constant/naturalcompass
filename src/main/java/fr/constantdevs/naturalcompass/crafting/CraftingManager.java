package fr.constantdevs.naturalcompass.crafting;

import fr.constantdevs.naturalcompass.NaturalCompass;
public class CraftingManager {

    private final NaturalCompass plugin;
    private final RecipeManager recipeManager;

    public CraftingManager(NaturalCompass plugin) {
        this.plugin = plugin;
        this.recipeManager = new RecipeManager(plugin);
    }

    public void loadRecipes() {
        recipeManager.loadRecipes();
    }

    public void unloadRecipes() {
        recipeManager.unregisterRecipes();
    }
}