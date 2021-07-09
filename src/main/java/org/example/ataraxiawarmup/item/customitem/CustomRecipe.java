package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.ChatColor;

import java.util.*;

public class CustomRecipe {

    public static final Map<String, CustomRecipe> NAME_MAP = new HashMap<>();
    public static final Map<CustomRecipe, CustomItemStack> RECIPE_MAP = new HashMap<>();

    private CustomItemStack[] recipeMatrix = new CustomItemStack[9];
    private CustomItemStack result;

    public CustomRecipe() {}

    public CustomRecipe(CustomItemStack[] matrix, CustomItemStack result) {
        this.recipeMatrix = matrix;
        this.result = result;
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
        if (recipe == null) {
            return null;
        }
        for (CustomRecipe key : RECIPE_MAP.keySet()) {
            if (key == null) {
                continue;
            }
            int successes = 0;
            for (int i = 0; i < key.getMatrix().length; i++) {
                if (key.getMatrix()[i] == null && recipe[i] == null) {
                    successes++;
                    continue;
                }
                if (key.getMatrix()[i] == null || recipe[i] == null) {
                    break;
                }
                if (key.getMatrix()[i].isLess(recipe[i])) {
                    successes++;
                } else {
                    break;
                }
            }
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
}