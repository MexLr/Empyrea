package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.ability.Ability;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.player.CustomPlayer;
import org.example.ataraxiawarmup.sql.SqlSetter;

import java.util.*;

public abstract class CustomWeapon extends CustomAttributableItem {

    private Ability ability;
    private int abilityLevel;
    private ItemStack item;
    private List<Element> elements = new ArrayList<>();
    private List<Integer> lowerBounds = new ArrayList<>();
    private List<Integer> upperBounds = new ArrayList<>();

    private List<String> extraLore = new ArrayList<>();

    private boolean isMisc; // whether or not the weapon is misc -> true = drops from mobs, false = doesn't drop from mobs and has a recipe

    public CustomWeapon(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, boolean shapeless, List<Element> elements, List<Integer> lowerBounds, List<Integer> upperBounds, Map<ItemAttribute, Integer> attributeMap, String extraLore, int combatLevelReq) {
        super(material, name, rarity, recipeMatrix, attributeMap, shapeless, combatLevelReq, null);
        ItemMeta itemMeta = getItemMeta();
        ItemMeta initialItemMeta = getItemMeta().clone();
        List<String> lore = itemMeta.getLore();
        double baseValue = getTotalValue();

            for (Ability ability : Ability.values()) {
                if (ability == Ability.NONE) {
                    continue;
                }
                for (int i = 0; i < 3; i++) {
                    this.setAttributes(attributeMap);
                    for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
                        if (getAttributes().keySet().contains(attribute) && attribute.getName() == "All") {
                            this.addAllAttributes(attribute, getAttributes().get(attribute));
                        }
                    }
                    this.elements.clear();
                    this.elements.addAll(elements);
                    this.lowerBounds.clear();
                    this.lowerBounds.addAll(lowerBounds);
                    this.upperBounds.clear();
                    this.upperBounds.addAll(upperBounds);
                    this.setRecipe(new CustomRecipe(recipeMatrix, new CustomItemStack(this), shapeless));
                    ItemMeta itemMetaCopy = initialItemMeta.clone();
                    double multi = 1 * Math.pow(1.5, i) * Math.pow(1.7, rarity.getId());
                    for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
                        if (ability.getAttributes().keySet().contains(attribute)) {
                            if (attribute.getName() == "All") {
                                this.addAllAttributes(attribute, (int) (ability.getAttributes().get(attribute) * multi));
                            } else {
                                this.addAttribute(attribute, (int) (ability.getAttributes().get(attribute) * multi), false);
                            }
                        }
                    }
                    // updating the item
                    List<String> loreCopy = itemMetaCopy.getLore();
                    loreCopy.add(ChatColor.GRAY + "Combat Lv. Min: " + getCombatLevelReq());
                    loreCopy.add("");
                    for (Element e : Element.getElementOrder()) {
                        if (this.elements.contains(e)) {
                            loreCopy.add(0, this.elements.get(this.elements.indexOf(e)).getColoredChar() + " " + this.lowerBounds.get(this.elements.indexOf(e)) + "-" + this.upperBounds.get(this.elements.indexOf(e)));
                        }
                    }

                    for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
                        if (getAttributes().keySet().contains(attribute) && attribute.getName() != "All") {
                            loreCopy.add(attribute.getColor() + "+" + getAttributeValue(attribute) + attribute.getName());
                        }
                    }

                    loreCopy.add("");
                    String loreString = "";
                    int totalCharacters = 0;
                    for (int c = 0; c < extraLore.length(); c++) {
                        loreString += extraLore.charAt(c);
                        if (c > 20 + totalCharacters) {
                            if (extraLore.charAt(c) == ' ') {
                                loreCopy.add(ChatColor.GRAY + loreString);
                                totalCharacters += c;
                                loreString = "";
                            }
                        }
                    }
                    loreCopy.add(ChatColor.GRAY + loreString);

                    itemMetaCopy.setLore(loreCopy);
                    itemMetaCopy.setDisplayName(ability.getColor() + ability.getPrefix(i) + " " + itemMeta.getDisplayName());
                    this.setItemMeta(itemMetaCopy);
                    this.setRecipe(null);
                    this.abilityLevel = i + 1;
                    this.ability = ability;
                    setValue(baseValue + Math.pow(576, i) * 128);
                    CUSTOM_ITEMS.put(ChatColor.stripColor(itemMetaCopy.getDisplayName()).toLowerCase(), this.clone());
                }
            }
        this.setAttributes(attributeMap);

        this.elements.clear();
        this.elements.addAll(elements);
        this.lowerBounds.clear();
        this.lowerBounds.addAll(lowerBounds);
        this.upperBounds.clear();
        this.upperBounds.addAll(upperBounds);

        this.ability = Ability.NONE;

        lore.add(ChatColor.GRAY + "Combat Lv. Min: " + getCombatLevelReq());
        lore.add("");

        for (Element e : Element.getElementOrder()) {
            if (this.elements.contains(e)) {
                lore.add(0, this.elements.get(this.elements.indexOf(e)).getColoredChar() + " " + this.lowerBounds.get(this.elements.indexOf(e)) + "-" + this.upperBounds.get(this.elements.indexOf(e)));
            }
        }

        for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
            if (getAttributes().keySet().contains(attribute) && attribute.getName() == "All") {
                this.addAllAttributes(attribute, getAttributes().get(attribute));
            }
        }

        for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
            if (getAttributes().keySet().contains(attribute) && attribute.getName() != "All") {
                lore.add(attribute.getColor() + "+" + getAttributeValue(attribute) + attribute.getName());
            }
        }

        lore.add("");
        String loreString = "";
        int totalCharacters = 0;
        for (int c = 0; c < extraLore.length(); c++) {
            loreString += extraLore.charAt(c);
            if (c > 20 + totalCharacters) {
                if (extraLore.charAt(c) == ' ') {
                    lore.add(ChatColor.GRAY + loreString);
                    this.extraLore.add(ChatColor.GRAY + loreString);
                    totalCharacters += c;
                    loreString = "";
                }
            }
        }
        lore.add(ChatColor.GRAY + loreString);
        this.extraLore.add(ChatColor.GRAY + loreString);

        itemMeta.setLore(lore);
        setItemMeta(itemMeta);

        this.setRecipe(new CustomRecipe(recipeMatrix, new CustomItemStack(this), shapeless));
        removeRecipe();

        if (recipeMatrix == null) {
            this.isMisc = true;
        }

        CUSTOM_ITEMS.put(ChatColor.stripColor(itemMeta.getDisplayName()).toLowerCase(), this);

        SqlSetter setter = new SqlSetter();
        setter.addWeapon(this);
    }

    @Override
    public void addAttribute(ItemAttribute attribute, int value, boolean all) {
        if (attribute.getName().contains("Weapon Damage")) {
            Element element = Element.fromName(attribute.getName().split(" ")[0]);
            if (this.elements.contains(element)) {
                this.lowerBounds.set(this.elements.indexOf(element), this.lowerBounds.get(this.elements.indexOf(element)) + value);
                this.upperBounds.set(this.elements.indexOf(element), this.upperBounds.get(this.elements.indexOf(element)) + value);
            } else {
                if (all) {
                    this.elements.add(element);
                    this.lowerBounds.add(value);
                    this.upperBounds.add(value);
                } else {
                    this.elements.add(0, element);
                    this.lowerBounds.add(0, value);
                    this.upperBounds.add(0, value);
                }
            }
        } else {
            addToAttribute(attribute, value);
        }
    }

    @Override
    public void onUseRight(Player player) {
        this.ability.performAbility(player, this.abilityLevel);
    }

    public List<Element> getElements() {
        return elements;
    }

    public List<Integer> getLowerBounds() {
        return lowerBounds;
    }

    public List<Integer> getUpperBounds() {
        return upperBounds;
    }

    public int getAbilityLevel() {
        return this.abilityLevel;
    }

    public Ability getAbility() {
        return this.ability;
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
            if (!damagedMob.isInvulnerable()) {
                CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
                if (this.getCombatLevelReq() <= customPlayer.getCombatLevel()) {
                    String damageString = getDamageString(damagedMob, multi, player);
                    int damageDealt = calculateDamage(damageString);

                    damagedMob.damage(damageDealt, player);

                    customPlayer.regenHealth(customPlayer.getValueOfAttribute(ItemAttribute.LIFESTEAL));
                    Bukkit.getPlayer("MexLr").sendMessage("" + (double) (damagedMob.getHealth()) / damagedMob.getMaxHealth() * ((LivingEntity) damaged).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    // set the mob's health to customHealth / customMaxHealth * actualMaxHealth
                    double newHealth = (double) (damagedMob.getHealth()) / damagedMob.getMaxHealth() * ((LivingEntity) damaged).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    if (newHealth <= 1) {
                        ((LivingEntity) damaged).setHealth(1);
                    } else {
                        ((LivingEntity) damaged).setHealth(newHealth);
                    }

                    // armor stand for damage marker
                    Location loc = damagedMob.getEntity().getLocation();
                    loc.add(Math.random() - 0.5D, 1D, Math.random() - 0.5D);

                    // spawn the armor stand with a random offset
                    ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    // set the armor stand to have the name of damage dealt, make it invisible, and remove the hitbox
                    armorStand.setMarker(true);
                    armorStand.setCustomName(damageString);
                    armorStand.setCustomNameVisible(true);

                    Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                        armorStand.remove();
                    }, 20);
                }
            }
        }
    }

    /**
     * Gets the string that is showed when a mob is damaged
     */
    protected String getDamageString(CustomMob damaged, double multi, Player player) {
        StringBuilder str = new StringBuilder();
        for (String string : getItemMeta().getLore()) {
            if (string == "") {
                break;
            }
            int damage;
            int lowerNumber;
            int higherNumber;
            // Element element = getElements().get(getItemMeta().getLore().indexOf(string));
            Element element = Element.fromChar(string.split(" ")[0]);

            // get the lower number in the damage range
            // lowerNumber = getLowerBounds().get(getItemMeta().getLore().indexOf(string));
            lowerNumber = Integer.parseInt(string
                    .split(" ")[1]
                    .split("-")[0]);
            // get the higher number in the damage range
            // higherNumber = getUpperBounds().get(getItemMeta().getLore().indexOf(string));
            higherNumber = Integer.parseInt(string
                    .split(" ")[1]
                    .split("-")[1]);;

            // get a random number between the lower and higher numbers
            Random random = new Random();
            damage = random.nextInt(higherNumber + 1 - lowerNumber) + lowerNumber;
            for (Element element1 : damaged.getElements()) {
                damage *= element.getDamageMultiAgainst(element1);
            }

            double damageMulti = 1;
            if (element != Element.AIR) {
                damageMulti = 1 - (damaged.getDefense() / (double) (damaged.getDefense() + 1000));
                damage *= damageMulti;
            } else {
                damageMulti = 1 - (damaged.getDefense() * 0.5D / (double) (damaged.getDefense() * 0.5D + 1000));
                damage *= damageMulti;
            }

            damage *= multi;

            double percentBonus = 0;
            CustomPlayer customPlayer = CustomPlayer.fromPlayer(player);
            if (customPlayer != null) {
                customPlayer.updateAttributes();
                Map<ItemAttribute, Integer> bonuses = customPlayer.getAttributes();
                if (bonuses != null) {
                    switch (element) {
                        case FIRE:
                            if (bonuses.get(ItemAttribute.FIREPERCENT) != null) {
                                percentBonus += bonuses.get(ItemAttribute.FIREPERCENT) / 100D;
                            }
                            break;
                        case AIR:
                            if (bonuses.get(ItemAttribute.AIRPERCENT) != null) {
                                percentBonus += bonuses.get(ItemAttribute.AIRPERCENT) / 100D;
                            }
                            break;
                        case THUNDER:
                            if (bonuses.get(ItemAttribute.THUNDERPERCENT) != null) {
                                percentBonus += bonuses.get(ItemAttribute.THUNDERPERCENT) / 100D;
                            }
                            break;
                        case CHAOS:
                            if (bonuses.get(ItemAttribute.CHAOSPERCENT) != null) {
                                percentBonus += bonuses.get(ItemAttribute.CHAOSPERCENT) / 100D;
                            }
                            break;
                        case EARTH:
                            if (bonuses.get(ItemAttribute.EARTHPERCENT) != null) {
                                percentBonus += bonuses.get(ItemAttribute.EARTHPERCENT) / 100D;
                            }
                            break;
                        case WATER:
                            if (bonuses.get(ItemAttribute.WATERPERCENT) != null) {
                                percentBonus += bonuses.get(ItemAttribute.WATERPERCENT) / 100D;
                            }
                            break;
                    }
                }
            }

            damage *= (double) (1 + percentBonus);

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
        if (totalDamage > Integer.MAX_VALUE) {
            totalDamage = Integer.MAX_VALUE;
        }
        return totalDamage;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public boolean hasAbility() {
        return this.ability != Ability.NONE ? true : false;
    }

    public List<String> getExtraLore() {
        return extraLore;
    }

    public boolean isMisc() {
        return isMisc;
    }

    @Override
    public CustomWeapon clone() {
        CustomWeapon weapon = (CustomWeapon) super.clone();

        weapon.abilityLevel = this.abilityLevel;
        weapon.ability = this.ability;

        return weapon;
    }
}
