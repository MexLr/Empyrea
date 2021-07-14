package org.example.ataraxiawarmup.player;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.item.customitem.*;

import java.util.*;

public class CustomPlayer {

    private static final Map<UUID, CustomPlayer> PLAYER_MAP = new HashMap<>();

    private final UUID uuid;
    private final Player player;
    private final int baseHealth;
    private int maxHealth;
    private int health;
    private Map<ItemAttribute, Integer> attributes;
    private String projTrail;
    private final int baseTotalAbilityCharge;
    private int totalAbilityCharge;
    private int abilityCharge;

    private RadioSongPlayer currentRadio;

    public CustomPlayer(Player player) {
        this.baseHealth = 100;
        this.maxHealth = 100;
        this.health = 100;
        this.attributes = new HashMap<>();
        this.uuid = player.getUniqueId();
        this.player = player;
        this.projTrail = "Basic";
        this.baseTotalAbilityCharge = 50;
        this.totalAbilityCharge = 50;
        this.abilityCharge = 50;
        PLAYER_MAP.put(uuid, this);
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
            if (maxHealth < this.health) {
                this.health = maxHealth;
            }
            sendActionbarMessage();
        }
    }

    public void addMaxHealth(int extraHealth) {
        setMaxHealth(this.health + extraHealth);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
        if (this.health <= 0) {
            this.player.teleport(new Location(this.player.getWorld(), 1574, 93, -157));
            this.player.setVelocity(new Vector(0, 0, 0));
            this.health = 1;
        }
        // this.player.setHealth(Math.ceil((this.health / this.maxHealth) * this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()));
        double healthFrac = (double) (this.health) / (double) (this.maxHealth);
        double newHealth = Math.ceil(healthFrac * this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        this.player.setHealth(newHealth);

        sendActionbarMessage();
    }

    public int getHealth() {
        return health;
    }

    public void regenHealth(double percent) {
        int newHealth = (int) (this.health + this.maxHealth * percent / 100);
        setHealth(newHealth);
    }

    public void regenHealth(int amount) {
        int newHealth = this.health + amount;
        setHealth(newHealth);
    }

    public void damage(double amount, List<Element> elements) {
        double damageDealt = 0;
        for (Element element : elements) {
            double currentDamage = amount / elements.size();
            double playerDefense = this.getDefenseAgainst(element);
            currentDamage *= 1 - (playerDefense / (playerDefense + 200));
            player.sendMessage("" + element + ", " + currentDamage + ", " + playerDefense);
            damageDealt += currentDamage;
        }
        this.player.playEffect(EntityEffect.HURT);
        setHealth((int) (this.health - damageDealt));
    }

    public void setTrail(String trail) {
        this.projTrail = trail;
    }

    public String getTrail() {
        return projTrail;
    }

    public void setAbilityCharge(int charge) {
        this.abilityCharge = charge;
        if (abilityCharge > totalAbilityCharge) {
            this.abilityCharge = totalAbilityCharge;
        }
        if (abilityCharge < totalAbilityCharge / 2) {
            PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0, false, false);
            this.player.addPotionEffect(slowness);
            if (abilityCharge < totalAbilityCharge / 4) {
                slowness = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1, false, false);
                if (abilityCharge < totalAbilityCharge / 8) {
                    slowness = new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2, false, false);
                }
            }
            this.player.removePotionEffect(PotionEffectType.SLOW);
            this.player.addPotionEffect(slowness);
        } else {
            this.player.removePotionEffect(PotionEffectType.SLOW);
        }
        this.player.setExp((float) this.abilityCharge / this.totalAbilityCharge);
        this.player.setLevel(this.abilityCharge);
    }

    /**
     * Remove a number from the player's ability charge.
     *
     * @param remove - the amount of charge to remove
     * @return - false if abilityCharge ends up being < 0, true otherwise
     */
    public boolean removeAbilityCharge(int remove) {
        int newCharge = this.abilityCharge - remove;
        if (newCharge < 0) {
            return false;
        }
        setAbilityCharge(newCharge);
        return true;
    }

    public int getAbilityCharge() {
        return abilityCharge;
    }

    public void setTotalAbilityCharge(int amount) {
        this.totalAbilityCharge = amount;
    }

    public int getTotalAbilityCharge() {
        return totalAbilityCharge;
    }

    public void regenAbilityCharge(double percent) {
        int newCharge = (int) (this.abilityCharge + (double) this.totalAbilityCharge * (percent + (double) getValueOfAttribute(ItemAttribute.ABILITYREGEN)) / 100D);
        setAbilityCharge(newCharge);
    }

    public void setCurrentRadio(RadioSongPlayer radio) {
        this.currentRadio = radio;
    }

    public RadioSongPlayer getCurrentRadio() {
        return currentRadio;
    }

    public double getDefenseAgainst(Element element) {
        switch (element) {
            case FIRE:
                return getValueOfAttribute(ItemAttribute.FIREDEF);
            case WATER:
                return getValueOfAttribute(ItemAttribute.WATERDEF);
            case EARTH:
                return getValueOfAttribute(ItemAttribute.EARTHDEF);
            case THUNDER:
                return getValueOfAttribute(ItemAttribute.THUNDERDEF);
            case AIR:
                return getValueOfAttribute(ItemAttribute.AIRDEF);
            case CHAOS:
                return getValueOfAttribute(ItemAttribute.CHAOSDEF);
            default:
                return 0;
        }
    }

    public void setAttributes(Map<ItemAttribute, Integer> attributes) {
        this.attributes = attributes;
    }

    public Map<ItemAttribute, Integer> getAttributes() {
        return attributes;
    }

    public Integer getValueOfAttribute(ItemAttribute attribute) {
        if (attributes.containsKey(attribute)) {
            return attributes.get(attribute);
        } else {
            return 0;
        }
    }

    public void setAttribute(ItemAttribute attribute, int value) {
        attributes.put(attribute, value);
    }

    public void addAttribute(ItemAttribute attribute, int value) {
        if (attributes.containsKey(attribute)) {
            attributes.replace(attribute, attributes.get(attribute) + value);
        } else {
            setAttribute(attribute, value);
        }
    }

    public void updateAttributes() {
        setAttributes(new HashMap<>());
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            CustomItem heldItem = CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
            if (heldItem instanceof CustomWeapon) {
                for (ItemAttribute attribute : ((CustomWeapon) heldItem).getAttributes().keySet()) {
                    setAttribute(attribute, ((CustomWeapon) heldItem).getAttributeValue(attribute));
                }
            }
        }
        ItemStack[] armorPieces = player.getInventory().getArmorContents();
        int extraHealth = 0;
        for (ItemStack item : armorPieces) {
            if (item != null) {
                CustomArmor armor = (CustomArmor) CustomItem.fromName(item.getItemMeta().getDisplayName());
                if (armor != null) {
                    for (ItemAttribute attribute : armor.getAttributes().keySet()) {
                        if (attribute == ItemAttribute.HEALTH) {
                            extraHealth += armor.getAttributeValue(ItemAttribute.HEALTH);
                        } else {
                            addAttribute(attribute, armor.getAttributeValue(attribute));
                        }
                    }
                }

            }
        }
        setMaxHealth(this.baseHealth + extraHealth);
    }

    public void sendActionbarMessage() {
        String message = new StringBuilder().append(ChatColor.RED).append(this.health).append("/").append(this.maxHealth).toString();
        this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public static CustomPlayer fromPlayer(Player player) {
        return PLAYER_MAP.get(player.getUniqueId());
    }

}
