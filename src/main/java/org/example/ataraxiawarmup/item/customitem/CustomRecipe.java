package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.example.ataraxiawarmup.Main;

import java.util.*;

public class CustomRecipe {

    public static final Map<String, CustomRecipe> NAME_MAP = new HashMap<>();
    public static final Map<CustomRecipe, CustomItemStack> RECIPE_MAP = new HashMap<>();

    private CustomItemStack[] recipeMatrix = new CustomItemStack[9];
    private CustomItemStack result;
    private boolean isShapeless;

    public CustomRecipe() {}

    public CustomRecipe(CustomItemStack[] matrix, CustomItemStack result, boolean shapeless) {
        this.recipeMatrix = matrix;
        this.result = result;
        this.isShapeless = shapeless;
        NAME_MAP.put(ChatColor.stripColor(result.getItemMeta().getDisplayName()).toLowerCase(), this);
        RECIPE_MAP.put(this, result);
    }

    public CustomItemStack[] getIngredients() {
        Map<String, CustomItemStack> ingredients = new HashMap<>();
        for (CustomItemStack item : recipeMatrix) {
            if (item != null) {
                String key = item.getItemMeta().getDisplayName();
                if (ingredients.containsKey(key)) {
                    ingredients.replace(key, new CustomItemStack(item.getItem(), item.getAmount() + ingredients.get(key).getAmount()));
                    continue;
                }
                ingredients.put(key, item);
            }
        }
        return ingredients.values().toArray(new CustomItemStack[0]);
    }

    public CustomItemStack[] getTotalIngredients() {
        Map<String, CustomItemStack> ingredients = new HashMap<>();
        for (CustomItemStack item : recipeMatrix) {
            if (item != null) {
                if (item.getItem().getRecipe() != null) {
                    CustomItemStack[] itemIngredients = item.getItem().getRecipe().getTotalIngredients();
                    for (CustomItemStack ingredient : itemIngredients) {
                        String key = ingredient.getItemMeta().getDisplayName();
                        if (ingredients.containsKey(key)) {
                            ingredients.replace(key, new CustomItemStack(ingredient.getItem(), ingredient.getAmount() * item.getAmount() + ingredients.get(key).getAmount()));
                            continue;
                        }
                        ingredients.put(key, new CustomItemStack(ingredient.getItem(), ingredient.getAmount() * item.getAmount()));
                    }
                } else {
                    String key = item.getItemMeta().getDisplayName();
                    if (ingredients.containsKey(key)) {
                        ingredients.replace(key, new CustomItemStack(item.getItem(), item.getAmount() + ingredients.get(key).getAmount()));
                        continue;
                    }
                    ingredients.put(key, new CustomItemStack(item.getItem(), item.getAmount()));
                }
            }
        }
        return ingredients.values().toArray(new CustomItemStack[0]);
    }

    public String getIngredientsAsString() {
        CustomItemStack[]  ingredients = getIngredients();
        StringBuilder str = new StringBuilder();
        str.append(this.result.getItemMeta().getDisplayName()).append(ChatColor.GREEN).append(" needs: ");
        for (CustomItemStack item : ingredients) {
            str.append("\n").append("§6").append(item.getAmount()).append(" ").append(item.getItemMeta().getDisplayName());
        }
        str.append(ChatColor.GREEN).append("\nto craft.");
        return str.toString();
    }

    public String getTotalIngredientsAsString() {
        CustomItemStack[]  ingredients = getTotalIngredients();
        StringBuilder str = new StringBuilder();
        str.append(this.result.getItemMeta().getDisplayName()).append(ChatColor.GREEN).append(" needs: ");
        for (CustomItemStack item : ingredients) {
            str.append("\n").append("§6").append(item.getAmount()).append(" ").append(item.getItemMeta().getDisplayName());
        }
        str.append(ChatColor.GREEN).append("\nto craft.");
        return str.toString();
    }

    public static CustomRecipe fromName(String name) {
        if (name == null || !NAME_MAP.containsKey(ChatColor.stripColor(name.toLowerCase()))) {
            return null;
        }
        return NAME_MAP.get(ChatColor.stripColor(name.toLowerCase()));
    }

    public static CustomItemStack getResult(CustomItemStack[] recipe) {
        if (recipe == null) { // if the recipe doesn't exist, there's no result
            return null;
        }
        List<CustomRecipe> possibleRecipes = new ArrayList<>(); // a list of all possible recipes, obtained through the following:
        for (CustomItemStack itemStack : recipe) { // iterate through each item in the recipe
            if (itemStack == null) { // if the item is null, continue to the next one
                continue;
            }
            possibleRecipes = usesItem(itemStack.getItem()); // get all of the recipes that use the first non-null item in the given array. These are all of the possible recipes
            // this helps avoid checking recipes with 0 successes
            if (possibleRecipes == null) { // if the possible recipes is null for whatever reason, return null
                return null;
            }
            if (possibleRecipes.size() == 0) { // if there are no possible recipes, return null
                return null;
            }
        }
        // iterate through all of the recipes
        for (CustomRecipe key : possibleRecipes) {
            // initialize a "successes" variable that increments every time there is a match in the given recipe and possible recipe. if successes == 9, return the result of the recipe
            int successes = 0;
            if (key.isShapeless) {
                CustomItemStack[] shapelessRecipe = key.getMatrix().clone(); // copy the shapeless recipe into an array
                CustomItemStack[] givenRecipe = recipe.clone(); // copy the given recipe into an array
                for (int i = 0; i < shapelessRecipe.length; i++) { // iterate through each element of the shapeless recipe
                    for (int j = 0; j < givenRecipe.length; j++) { // iterate through each element of the given recipe/array
                        if (shapelessRecipe[i] == null && givenRecipe[j] == null) { // if the item in the shapeless recipe and the item in the given recipe are both null, increment successes and go to the next item in the shapeless recipe
                            shapelessRecipe[i] = new CustomItemStack(Main.CUSTOM_AIR);
                            givenRecipe[j] = new CustomItemStack(Main.CUSTOM_AIR);
                            successes++;
                            break;
                        }
                        if (shapelessRecipe[i] == null || givenRecipe[j] == null) { // if just the shapeless recipe's item or the given recipe's item is null, continue to the next item in the given recipe array
                            continue;
                        }
                        if (givenRecipe[j].getMaterial().equals(Material.AIR) || shapelessRecipe[i].getMaterial().equals(Material.AIR)) {
                            continue;
                        }
                        if (shapelessRecipe[i].isLess(recipe[j])) {
                            shapelessRecipe[i] = new CustomItemStack(Main.CUSTOM_AIR);
                            givenRecipe[j] = new CustomItemStack(Main.CUSTOM_AIR);
                            successes++;
                            break;
                        }
                    }
                }
            } else {
                // iterate through all of the items in the possible recipe's matrix
                for (int i = 0; i < key.getMatrix().length; i++) {
                    if (key.getMatrix()[i] == null && recipe[i] == null) { // if both the given recipe's item and the possible recipe's item are null at the same index, increment successes and move to the next index
                        successes++;
                        continue;
                    }
                    if (key.getMatrix()[i] == null || recipe[i] == null) { // if just one of the items is null, they aren't equal, so successes isn't incremented
                        break; // if successes isn't incremented, there's not point in checking anymore, so break
                    }
                    if (key.getMatrix()[i].isLess(recipe[i])) { // if the given recipe's CustomItemStack is made of the same CustomItem and has an amount <= of the possible recipe's item, increment successes
                        successes++;
                    } else { // if successes isn't incremented, there's not point in checking anymore, so break
                        break;
                    }
                }
            }
            Bukkit.getPlayer("MexLr").sendMessage(key.getResult().getItemMeta().getDisplayName() +  successes);
            if (successes == 9) {
                return RECIPE_MAP.get(key);
            }
        }
        return null;
    }

    public static List<CustomRecipe> usesItem(CustomItem item) {
        List<CustomRecipe> recipes = new ArrayList<CustomRecipe>();
        for (CustomRecipe recipe : RECIPE_MAP.keySet()) {
            if (recipe == null) {
                continue;
            }
            for (int i = 0; i < recipe.getMatrix().length; i++) {
                if (recipe.getMatrix()[i] == null) {
                    continue;
                }
                if (recipe.getMatrix()[i].getItem().toCustomItemStack().isSimilar(item.toCustomItemStack())) {
                    recipes.add(recipe);
                    break;
                }
            }
        }
        return recipes;
    }

    public CustomItemStack[] getMatrix() {
        return this.recipeMatrix;
    }

    public CustomItemStack getResult() {
        return this.result;
    }

    public boolean isShapeless() {
        return isShapeless;
    }
}