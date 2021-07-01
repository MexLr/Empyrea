package org.example.ataraxiawarmup.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.mob.CustomMob;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CustomWeapon extends CustomCooldownableItem {
    public CustomWeapon(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds) {
        super(material, name, rarity, recipeMatrix);
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.getLore();
        for (int i = 0; i < elements.size(); i++) {
            lore.add(0, elements.get(i).getColoredChar() + " " + lowerBounds.get(i) + "-" + upperBounds.get(i));
        }
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
        CUSTOM_ITEMS.put(ChatColor.stripColor(getItemMeta().getDisplayName()).toLowerCase(), this);
    }

    /**
     * Executed when the player damages a mob
     *
     * @param player - The player using the weapon
     * @param damaged - The entity getting attacked by the weapon
     */
    public void onDamageMob(Player player, Entity damaged, double multi) {
        damaged.setInvulnerable(false);
        if (CustomMob.fromEntity(damaged) != null) {
            CustomMob damagedMob = CustomMob.fromEntity(damaged);
            String damageString = getDamageString(damagedMob, multi);
            int damageDealt = calculateDamage(damageString);

            damagedMob.damage(damageDealt);

            // armor stand for damage marker
            Location loc = damagedMob.getEntity().getLocation();
            loc.add(Math.random() - 0.5D, 1D, Math.random() - 0.5D);

            // spawn the armor stand with a random offset
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);

            // set the armor stand to have the name of damage dealt, make it invisible, and remove the hitbox
            armorStand.setCustomName(damageString);
            armorStand.setCustomNameVisible(true);
            armorStand.setMarker(true);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                armorStand.remove();
            }, 20);
        }
    }

    /**
     * Gets the string that is showed when a mob is damaged
     */
    protected String getDamageString(CustomMob damaged, double multi) {
        StringBuilder str = new StringBuilder();
        for (String string : getItemMeta().getLore()) {
            if (string == "") {
                break;
            }
            int damage;
            int lowerNumber;
            int higherNumber;
            Element element = Element.fromChar(string.split(" ")[0]);

            // get the lower number in the damage range
            lowerNumber = Integer.parseInt(string.split("-")[0]
                    .split(" ")[1]
                    .trim());
            // get the higher number in the damage range
            higherNumber = Integer.parseInt(string.split("-")[1]);

            // get a random number between the lower and higher numbers
            Random random = new Random();
            damage = random.nextInt(higherNumber + 1 - lowerNumber) + lowerNumber;
            for (Element element1 : damaged.getElements()) {
                damage *= element.getDamageMultiAgainst(element1);
            }
            damage *= multi;
            double damageMulti = 1 - (damaged.getDefense() / (double) (damaged.getDefense() + 1000));
            damage *= damageMulti;
            str.append(" ").append(element.getColoredChar()).append(" ").append(damage);
        }
        return str.toString().trim();
    }

    protected int calculateDamage(String damageString) {
        int totalDamage = 0;
        String[] damageStringArray = damageString.split(" ");
        for (int i = 0; i < damageStringArray.length; i++) {
            if ((i + 1) % 2 == 0) {
                totalDamage += Integer.parseInt(ChatColor.stripColor(damageStringArray[i]));
            }
        }
        return totalDamage;
    }

}
