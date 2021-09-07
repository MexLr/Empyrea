package org.example.ataraxiawarmup.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomItemStack;
import org.example.ataraxiawarmup.player.Chat;
import org.example.ataraxiawarmup.player.CustomPlayer;

import java.util.List;
import java.util.Map;

public class ShopInventoryListener implements Listener {
    private Main plugin;
    public ShopInventoryListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerClicksInInventory(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith("Orders")) {
            if (event.getRawSlot() == event.getSlot()) {
                Player player = (Player) event.getWhoClicked();
                CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem.hasItemMeta()) {
                    if (clickedItem.getItemMeta().hasLore() && event.getSlot() != 48) {
                        int id = Integer.parseInt(ChatColor.stripColor(clickedItem.getItemMeta()
                                .getLore()
                                .get(1)
                                .split(" ")[1]));
                        Order order = Order.getActiveOrder(id);

                        for (Reward reward : customPlayer.getPendingRewards()) {
                            if (reward.getOrder().getId() == order.getId()) {
                                player.sendMessage("§cYou already have an outgoing reward for this order! Please claim it before attempting to view/fill this order!");
                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                return;
                            }
                        }

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            player.openInventory(order.getOrderInventory(player));
                        });
                        //order.fillOrder((Player) event.getWhoClicked(), new CustomItemStack(CustomItem.fromName("Rotten Flesh"), 32));
                    }
                }
                if (event.getSlot() == 48) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.openInventory(new RewardInventory(player).getInventory());
                    });
                }
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.closeInventory();
                    });
                }
                /**
                Bukkit.getScheduler().runTask(plugin, () -> {
                    event.getWhoClicked().openInventory(ShopInventory.getOrderInv());
                });
                 */
            }
        }
        if (event.getView().getTitle().startsWith("Order:")) {
            if (event.getRawSlot() == event.getSlot()) {
                Player player = (Player) event.getWhoClicked();
                CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem.hasItemMeta()) {
                    if (clickedItem.getItemMeta().hasLore()) {
                        List<String> itemLore = clickedItem.getItemMeta().getLore();
                        if (ChatColor.stripColor(itemLore.get(0).split(" ")[0]).equalsIgnoreCase("Index")) { // if the first word in the lore is "Index"
                            int index = Integer.parseInt(ChatColor.stripColor(itemLore.get(0).split(" ")[1])); // get the index
                            ItemStack orderItem = event.getInventory().getItem(13); // get the order item
                            int id = Integer.parseInt(ChatColor.stripColor(orderItem.getItemMeta()
                                    .getLore()
                                    .get(1)
                                    .split(" ")[1])); // id of the order
                            Order order = Order.getActiveOrder(id); // get the order from the id

                            if (order == null) {
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    player.openInventory(ShopInventory.getOrderInv());
                                });
                                player.sendMessage("§cThis order has already been filled, or doesn't exist for some reason!");
                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                return;
                            }

                            CustomItemStack itemRequested = order.getItemsRequested()[index];
                            CustomItemStack itemFilled = order.getItemsFilled()[index];

                            Map<CustomItem, CustomItemStack> itemsInInventory = customPlayer.getInventoryItemMap();
                            CustomItemStack donatedItem = itemsInInventory.get(itemRequested.getItem());

                            if (event.getClick().equals(ClickType.RIGHT)) {
                                player.sendMessage("" + itemRequested.getItem().getItemMeta().getDisplayName());
                                if (itemsInInventory.get(itemRequested.getItem()) != null) {
                                    player.sendMessage("" + donatedItem.getAmount());
                                    if (itemsInInventory.get(itemRequested.getItem()).getAmount() >= (itemRequested.getAmount() - itemFilled.getAmount())) {
                                        int donateAmount = itemRequested.getAmount() - itemFilled.getAmount();
                                        if (order.fillOrder(player, new CustomItemStack(donatedItem.getItem(), donateAmount))) {
                                            player.sendMessage(ChatColor.GREEN + "You donated " + donatedItem.getItemMeta().getDisplayName() + " x " + donateAmount + ChatColor.GREEN + " to the order!");
                                        }
                                        Bukkit.getScheduler().runTask(plugin, () -> {
                                            player.openInventory(order.getOrderInventory(player));
                                        });
                                    }
                                }
                            }

                            if (event.getClick().equals(ClickType.LEFT)) {
                                Bukkit.getScheduler().runTask(plugin, () -> {
                                    player.closeInventory();
                                });
                                player.sendMessage(ChatColor.BLUE + "Please specify an amount in chat!");
                                Location currentLocation = player.getLocation();
                                Chat currentChat = customPlayer.getChat();
                                customPlayer.setChat(Chat.INPUT);
                                new BukkitRunnable() {
                                    int ticks = 1;
                                    @Override
                                    public void run() {
                                        if (ticks % 7 == 0) {
                                            player.teleport(currentLocation);
                                        }
                                        if (customPlayer.getRecentInput() != null || ticks > 50) {
                                            cancel();
                                            Bukkit.getScheduler().runTask(plugin, () -> {
                                                player.openInventory(order.getOrderInventory(player));
                                            });
                                            try {
                                                int amount = Integer.parseInt(customPlayer.getRecentInput());
                                                customPlayer.setRecentInput(null);
                                                customPlayer.setChat(currentChat);
                                                if (amount <= 0) {
                                                    player.sendMessage("§cThat's not a valid number!");
                                                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                                    return;
                                                }
                                                if (donatedItem == null) {
                                                    player.sendMessage("§cYou don't have that many " + itemRequested.getItem().getItemMeta().getDisplayName());
                                                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                                    return;
                                                }
                                                if (amount > donatedItem.getAmount()) {
                                                    player.sendMessage("§cYou don't have that many " + donatedItem.getItemMeta().getDisplayName());
                                                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                                    return;
                                                }
                                                if (order.fillOrder(player, new CustomItemStack(donatedItem.getItem(), amount))) {
                                                    player.sendMessage(ChatColor.GREEN + "You donated " + donatedItem.getItemMeta().getDisplayName() + " x " + amount + ChatColor.GREEN + " to the order!");
                                                }
                                            } catch (NumberFormatException e) {
                                                player.sendMessage("§cThat's not a valid number!");
                                                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                                customPlayer.setRecentInput(null);
                                                customPlayer.setChat(currentChat);
                                                return;
                                            }
                                        }
                                        ticks++;
                                    }
                                }.runTaskTimer(Main.getInstance(), 0, 3);
                            }

                            Bukkit.getScheduler().runTask(plugin, () -> {
                                if (order.isFilled()) {
                                    order.rewardPlayers();
                                    player.openInventory(ShopInventory.getOrderInv());
                                }
                            });
                        }
                    }
                }
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.closeInventory();
                    });
                }
                if (event.getSlot() == 45) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.openInventory(ShopInventory.getOrderInv());
                    });
                }
            }
        }
        if (event.getView().getTitle().equalsIgnoreCase("Your Rewards")) {
            if (event.getRawSlot() == event.getSlot()) {
                Player player = (Player) event.getWhoClicked();
                CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                event.setCancelled(true);
                ItemStack clickedItem = event.getCurrentItem();
                if (clickedItem.hasItemMeta()) {
                    if (clickedItem.getItemMeta().hasLore()) {
                        List<String> itemLore = clickedItem.getItemMeta().getLore();
                        if (ChatColor.stripColor(itemLore.get(0).split(" ")[0]).equalsIgnoreCase("ID")) { // if the first word in the lore is "Index"
                            int id = Integer.parseInt(ChatColor.stripColor(clickedItem.getItemMeta()
                                    .getLore()
                                    .get(0)
                                    .split(" ")[1])); // id of the reward
                            Reward reward = Reward.getReward(id);
                            reward.generateReward(player);
                            reward.getReward().rewardPlayer(customPlayer);
                            reward.removeReceiver(customPlayer);
                            Bukkit.getScheduler().runTask(plugin, () -> {
                                player.openInventory(new RewardInventory(player).getInventory());
                            });
                        }
                    }
                }
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.closeInventory();
                    });
                }
                if (event.getSlot() == 45) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.openInventory(ShopInventory.getOrderInv());
                    });
                }
            }
        }
    }

}
