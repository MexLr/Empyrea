package org.example.ataraxiawarmup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.ataraxiawarmup.item.*;
import org.example.ataraxiawarmup.projectiletrail.ApplierListener;
import org.example.ataraxiawarmup.projectiletrail.ArrowShootListener;
import org.example.ataraxiawarmup.spawner.SpawnerListener;

import java.util.*;

public class Main extends JavaPlugin {

    public static final ItemStack FILLER_ITEM; // filler item for inventories
    public static final ItemStack BACK_ARROW; // back arrow for inventories
    public static final ItemStack CLOSE_BARRIER; // close barrier for inventories

    private static Main instance;

    static {
        FILLER_ITEM = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = FILLER_ITEM.getItemMeta();
        fillerMeta.setDisplayName(" ");
        fillerMeta.setLore(null);
        FILLER_ITEM.setItemMeta(fillerMeta);

        BACK_ARROW = new ItemStack(Material.ARROW);
        ItemMeta backMeta = BACK_ARROW.getItemMeta();
        backMeta.setDisplayName(ChatColor.WHITE + "Back");
        BACK_ARROW.setItemMeta(backMeta);

        CLOSE_BARRIER = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = CLOSE_BARRIER.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        closeMeta.setLore(null);
        CLOSE_BARRIER.setItemMeta(closeMeta);
    }

    public Map<UUID, List<String>> playerStats = new HashMap<UUID, List<String>>();

    // so MANIFEST.MF recognizes this as a main class
    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        new SpawnerListener(this);
        new CommandListener(this);
        new ArrowShootListener(this);
        new ApplierListener(this);
        new JoinListener(this);
        new RecipeListener(this);
        new CustomItemListener(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            List<String> stats = new ArrayList<String>();
            stats.add("Basic");
            playerStats.put(player.getUniqueId(), stats);
        }

        TestIngredient testIngredient = new TestIngredient(CustomItemType.BLAZE_POWDER, "Test Ingredient", Rarity.EPIC, null);
        TestItem testItem = new TestItem(CustomItemType.BLAZE_ROD, "Test Item", Rarity.LOVECRAFTIAN, new CustomItemStack[]
                {null, new CustomItemStack(testIngredient, 3), null,
                new CustomItemStack(testIngredient, 3), null, new CustomItemStack(testIngredient, 3),
                null, new CustomItemStack(testIngredient, 3), null});

        for (CustomItem item : CustomItem.CUSTOM_ITEMS.values()) {
            item.createRecipe();
        }

        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }
}
