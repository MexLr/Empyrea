package org.example.ataraxiawarmup.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.item.customitem.Rarity;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.*;

public class Order {

    private static final Map<Integer, Order> ORDER_MAP = new HashMap<>();
    private static final Map<Integer, Order> ACTIVE_ORDERS = new HashMap<>();

    private final Map<UUID, Double> contributors = new HashMap<>();
    private final CustomItemStack[] itemsRequested;
    private final CustomItemStack[] itemsFilled;
    private final Reward reward;
    private final int id;
    private final Rarity rarity;
    private final String name;

    private double neededValue = 0;
    private double currentValue = 0;

    private boolean filled = false;

    public Order(CustomItemStack[] itemsRequested, Reward reward, Rarity rarity, int id, String name) {
        this.itemsRequested = itemsRequested;
        this.reward = reward;
        this.id = id;
        this.rarity = rarity;
        this.name = name;
        this.itemsFilled = new CustomItemStack[itemsRequested.length];
        for (int i = 0; i < itemsRequested.length; i++) {
            this.itemsFilled[i] = new CustomItemStack(itemsRequested[i].getItem(), 0);
            neededValue += itemsRequested[i].getTotalValue();
        }
        ORDER_MAP.put(id, this);
        reward.setOrder(this);
    }

    public CustomItemStack[] getItemsRequested() {
        return itemsRequested;
    }

    public CustomItemStack[] getItemsFilled() {
        return itemsFilled;
    }

    public Reward getReward() {
        return reward;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColoredName() {
        return rarity.getColor() + name;
    }

    public void removeFromRotation() {
        if (isInRotation()) {
            ACTIVE_ORDERS.remove(id);
        }
    }

    public void addIntoRotation() {
        if (!isInRotation()) {
            ACTIVE_ORDERS.put(id, this);
        }
    }

    public boolean isInRotation() {
        return ACTIVE_ORDERS.containsKey(id);
    }

    public ItemStack getItem() {
        ItemStack item = itemsRequested[0].toItemStack();
        item.setAmount(1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(rarity.getColor() + name);
        List<String> lore = new ArrayList<>();
        lore.add(rarity.getColor() + "" + ChatColor.BOLD + rarity.getName().toUpperCase() + " ORDER");
        lore.add(ChatColor.DARK_GRAY + "ID " + id);
        lore.add(ChatColor.AQUA + "Requested Items:");
        for (int i = 0; i < itemsRequested.length; i++) {
            ChatColor chatColor = ChatColor.RED;
            if (itemsFilled[i].getAmount() >= itemsRequested[i].getAmount()) {
                chatColor = ChatColor.GREEN;
            }
            lore.add(itemsRequested[i].getItem().getItemMeta().getDisplayName() + ": " + chatColor + itemsFilled[i].getAmount() + "/" + itemsRequested[i].getAmount());
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public boolean fillOrder(Player player, CustomItemStack... items) {
        for (CustomItemStack item : items) {
            for (int i = 0; i < itemsFilled.length; i++) {
                if (itemsFilled[i].getItem() == item.getItem()) {
                    if (itemsFilled[i].getAmount() >= itemsRequested[i].getAmount()) {
                        player.sendMessage("§cThis order has already met the requirements for this item! " + ChatColor.GRAY + "(" + item.getItemMeta().getDisplayName() + ChatColor.GRAY + ")");
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        return false;
                    }
                    itemsFilled[i] = new CustomItemStack(item.getItem(), item.getAmount() + itemsFilled[i].getAmount());
                    player.getInventory().removeItem(new ItemStack[]{item.toItemStack()});
                    player.updateInventory();
                    reward.addReceiver(player, item.getTotalValue());
                    addContributor(player.getUniqueId(), item.getTotalValue());
                    this.currentValue += item.getTotalValue();
                }
            }
        }
        return true;
    }

    public void addContributor(UUID uuid, double contribution) {
        if (!contributors.containsKey(uuid)) {
            contributors.put(uuid, contribution);
        } else {
            contributors.replace(uuid, contributors.get(uuid) + contribution);
        }
    }

    public boolean isFilled() {
        for (int i = 0; i < itemsFilled.length; i++) {
            if (itemsFilled[i].getAmount() < itemsRequested[i].getAmount()) {
                return false;
            }
        }
        this.filled = true;
        return true;
    }

    public void rewardPlayers() {
        reward.getReceivers().forEach(uuid -> {
            reward.setReceiver((Player) Bukkit.getOfflinePlayer(uuid), (int) Math.round(contributors.get(uuid) / currentValue * 100));
            CustomPlayer.fromPlayer((Player) Bukkit.getOfflinePlayer(uuid)).addReward(reward);
        });
        removeFromRotation();
        for (CustomItemStack item : itemsFilled) {
            item.setAmount(0);
        }
        contributors.clear();
        this.currentValue = 0;
    }

    public ItemStack getContributionItem(Player player) {
        ItemStack item = new ItemStack(Material.ACACIA_SIGN);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Contribution:");

        List<String> lore = new ArrayList<>();
        for (UUID uuid : contributors.keySet()) {
            if (player.getUniqueId().equals(uuid)) {
                lore.add(ChatColor.YELLOW + player.getName() + ": " + (int) (contributors.get(uuid) / currentValue * 100) + "%");
            } else {
                lore.add(ChatColor.GRAY + Bukkit.getOfflinePlayer(uuid).getName() + ": " + (int) (contributors.get(uuid) / currentValue * 100) + "%");
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public Inventory getOrderInventory(Player player) {
        CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
        Map<CustomItem, CustomItemStack> itemsInInventory = customPlayer.getInventoryItemMap();
        Inventory inv = Bukkit.createInventory(null, 54, "Order: " + name);
        for (int i = 0; i < 54; i++) {
            inv.setItem(i, Main.FILLER_ITEM);
        }
        inv.setItem(13, getItem());
        inv.setItem(49, Main.CLOSE_BARRIER);
        inv.setItem(45, Main.BACK_ARROW);

        inv.setItem(25, getContributionItem(player));

        int startingSlot = (int) (31 - itemsRequested.length / 2);
        for (int i = 0; i < itemsRequested.length; i++) {
            ItemStack displayItem = itemsRequested[i].getItem().toItemStack();
            ItemMeta displayMeta = displayItem.getItemMeta();
            List<String> displayLore = new ArrayList<>();
            ChatColor chatColor = ChatColor.RED;
            if (itemsFilled[i].getAmount() >= itemsRequested[i].getAmount()) {
                chatColor = ChatColor.GREEN;
            }
            displayLore.add(ChatColor.DARK_GRAY + "Index " + i);
            displayLore.add("" + chatColor + itemsFilled[i].getAmount() + "/" + itemsRequested[i].getAmount());
            displayLore.add("");
            displayLore.add(ChatColor.GREEN + "Left click to give some items!");

            if (itemsInInventory.get(itemsRequested[i].getItem()) != null) {
                if (itemsInInventory.get(itemsRequested[i].getItem()).getAmount() >= (itemsRequested[i].getAmount() - itemsFilled[i].getAmount())) {
                    displayLore.add(ChatColor.YELLOW + "Right click to completely fill this item's order!");
                }
            }

            displayMeta.setLore(displayLore);
            displayItem.setItemMeta(displayMeta);

            inv.setItem(startingSlot, displayItem);
            startingSlot++;
        }
        return inv;
    }

    public static Order getOrder(int id) {
        return ORDER_MAP.get(id);
    }

    public static Order getActiveOrder(int id) {
        return ACTIVE_ORDERS.get(id);
    }

    public static Collection<Order> getAllOrders() {
        return ORDER_MAP.values();
    }

    public static Collection<Order> getActiveOrders() {
        return ACTIVE_ORDERS.values();
    }

}
