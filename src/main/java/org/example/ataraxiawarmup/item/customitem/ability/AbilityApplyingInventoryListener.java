package org.example.ataraxiawarmup.item.customitem.ability;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.CustomItem;
import org.example.ataraxiawarmup.item.customitem.CustomWeapon;

import java.util.function.DoubleToIntFunction;

public class AbilityApplyingInventoryListener implements Listener {
    private Main plugin;

    public AbilityApplyingInventoryListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerClicksInInventory(InventoryClickEvent event) {
        if (event.getView().getTitle() == "Ability Anvil") {
            if (event.getSlot() == event.getRawSlot()) {
                event.setCancelled(true);
                if (event.getSlot() == 49) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        event.getWhoClicked().closeInventory();
                    });
                }

                ItemStack artifactTo = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta artifactToMeta = artifactTo.getItemMeta();
                artifactToMeta.setDisplayName(ChatColor.GOLD + "Place an artifact in the slot below!");
                artifactTo.setItemMeta(artifactToMeta);

                ItemStack weaponTo = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta weaponToMeta = weaponTo.getItemMeta();
                weaponToMeta.setDisplayName(ChatColor.GOLD + "Place a weapon in the slot below!");
                weaponTo.setItemMeta(weaponToMeta);

                ItemStack inputThere = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta inputMeta = inputThere.getItemMeta();
                inputMeta.setLore(null);
                inputMeta.addEnchant(Enchantment.DURABILITY, 0, true);
                inputMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                inputMeta.setDisplayName("");
                inputThere.setItemMeta(inputMeta);

                if (!event.getCursor().getType().equals(Material.AIR)) {
                    if (event.getCursor() == null) {
                        return;
                    }
                    if (event.getSlot() == 29) {
                        if (event.getInventory().getItem(29) != null) {
                            return;
                        }
                        CustomItem item = CustomItem.fromName(event.getCursor().getItemMeta().getDisplayName());
                        if (item instanceof CustomWeapon) {
                            CustomWeapon weapon = (CustomWeapon) CustomItem.fromName(event.getCursor().getItemMeta().getDisplayName());
                            if (weapon != null) {
                                event.getInventory().setItem(11, inputThere);
                                event.getInventory().setItem(12, inputThere);
                                event.getInventory().setItem(20, inputThere);

                                ItemStack savedItem = event.getCursor();
                                event.getView().setCursor(null);
                                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                    event.getView().setItem(event.getSlot(), savedItem);
                                });
                            } else {
                                ItemStack savedItem = event.getCursor();
                                event.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                    event.getView().setCursor(savedItem);
                                });
                            }
                        }
                    }
                    if (event.getSlot() == 33) {
                        if (event.getInventory().getItem(33) != null) {
                            return;
                        }
                        CustomItem item = CustomItem.fromName(event.getCursor().getItemMeta().getDisplayName());
                        if (item instanceof CustomArtifact) {
                            CustomArtifact artifact = (CustomArtifact) item;
                            if (artifact != null) {
                                event.getInventory().setItem(14, inputThere);
                                event.getInventory().setItem(15, inputThere);
                                event.getInventory().setItem(24, inputThere);

                                ItemStack savedItem = event.getCursor();
                                event.getView().setCursor(null);
                                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                    savedItem.setAmount(1);
                                    event.getView().setItem(event.getSlot(), savedItem);
                                    savedItem.setAmount(savedItem.getAmount() - 1);
                                    event.getView().setCursor(savedItem);
                                });
                            } else {
                                ItemStack savedItem = event.getCursor();
                                event.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                    event.getView().setCursor(savedItem);
                                });
                            }
                        }
                    }
                } else {
                    if (event.getSlot() == 29) {
                        ItemStack item = event.getInventory().getItem(event.getSlot());
                        event.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));

                        event.getInventory().setItem(11, weaponTo);
                        event.getInventory().setItem(12, weaponTo);
                        event.getInventory().setItem(20, weaponTo);

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            event.getView().setCursor(item);
                        });
                    }
                    if (event.getSlot() == 33) {
                        ItemStack item = event.getInventory().getItem(event.getSlot());
                        event.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));

                        event.getInventory().setItem(14, artifactTo);
                        event.getInventory().setItem(15, artifactTo);
                        event.getInventory().setItem(24, artifactTo);

                        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                            event.getView().setCursor(item);
                        });
                    }
                    if (event.getSlot() == 22) {
                        if (!event.getInventory().getItem(13).getType().equals(Material.BARRIER)) {
                            event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);

                            event.getInventory().setItem(29, new ItemStack(Material.AIR));
                            event.getInventory().setItem(33, new ItemStack(Material.AIR));

                            event.getInventory().setItem(11, weaponTo);
                            event.getInventory().setItem(12, weaponTo);
                            event.getInventory().setItem(20, weaponTo);

                            event.getInventory().setItem(14, artifactTo);
                            event.getInventory().setItem(15, artifactTo);
                            event.getInventory().setItem(24, artifactTo);
                        }
                    }
                    if (event.getSlot() == 13) {
                        if (event.getInventory().getItem(29) == null && event.getInventory().getItem(33) == null) {
                            ItemStack item = event.getInventory().getItem(13);

                            ItemStack outputSlot = new ItemStack(Material.BARRIER);
                            ItemMeta outputMeta = outputSlot.getItemMeta();
                            outputMeta.setDisplayName(ChatColor.RED + "Output");
                            outputSlot.setItemMeta(outputMeta);

                            event.getInventory().setItem(13, outputSlot);

                            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                                event.getView().setCursor(item);
                            });
                        }
                    }
                }
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    if (event.getInventory().getItem(29) != null && event.getInventory().getItem(33) != null) {
                        CustomArtifact artifact = (CustomArtifact) CustomItem.fromName(event.getInventory().getItem(33).getItemMeta().getDisplayName());
                        CustomWeapon weapon = (CustomWeapon) CustomItem.fromName(event.getInventory().getItem(29).getItemMeta().getDisplayName());
                        String prefix = artifact.getAbility().getPrefix(artifact.getLevel() - 1) + " ";
                        CustomWeapon newWeapon;
                        if (weapon.hasAbility()) { // if the weapon has an ability, remove its prefix before checking before adding the new prefix
                            String weaponName = weapon.getItemMeta().getDisplayName(); // get the weapon's name
                            String currentPrefix = weapon.getItemMeta().getDisplayName().split(" ")[0]; // get weapon's prefix
                            weaponName = weaponName.replace(currentPrefix, "").trim(); // replace the prefix with nothing, and remove any leading whitespace

                            newWeapon = (CustomWeapon) CustomItem.fromName(prefix + weaponName);
                        } else {
                            newWeapon = (CustomWeapon) CustomItem.fromName(prefix + weapon.getItemMeta().getDisplayName());
                        }
                        event.getInventory().setItem(13, newWeapon.toItemStack());
                    }
                });
            } else {
                if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
