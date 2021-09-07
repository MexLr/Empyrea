package org.example.ataraxiawarmup.player;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;
import org.example.ataraxiawarmup.Main;
import org.example.ataraxiawarmup.item.customitem.*;
import org.example.ataraxiawarmup.mob.CustomMob;
import org.example.ataraxiawarmup.party.Party;
import org.example.ataraxiawarmup.shop.Reward;
import org.example.ataraxiawarmup.sql.MySQL;
import org.example.ataraxiawarmup.sql.SqlGetter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CustomPlayer {

    private static final Map<UUID, CustomPlayer> PLAYER_MAP = new HashMap<>();

    private final UUID uuid;
    private final Player player;
    private int baseHealth;
    private int maxHealth;
    private int health;
    private Map<ItemAttribute, Integer> attributes;
    private String projTrail;
    private int baseTotalAbilityCharge;
    private int totalAbilityCharge;
    private int abilityCharge;

    private Location privateAreaLocation;

    private int combatLevel = 0;
    private double combatExp = 0;
    private double totalCombatExp = 0;

    private RadioSongPlayer currentRadio;

    private PlayerHomeGenerator generator;

    private CustomPlayer inviteFrom;
    private Party party;

    private Chat chat;

    private List<Reward> pendingRewards = new ArrayList<>();

    private Scoreboard scoreboard;

    private String recentInput; // the player's most recent input, for example a number of items to dedicate towards filling an order

    public CustomPlayer(Player player) {
        SqlGetter getter = new SqlGetter();
        MySQL mySQL = new MySQL();
        mySQL.refreshConnection();
        if (getter.playerIdExists(player.getUniqueId())) {
            try {
                PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM PlayerStats WHERE uuid=?");
                statement.setString(1, player.getUniqueId().toString());

                ResultSet results = statement.executeQuery();
                results.next();

                this.combatExp = results.getDouble("combatExp");
                this.totalCombatExp = results.getDouble("totalCombatExp");
                this.projTrail = results.getString("projectileTrail");
                int combatLevel = results.getInt("combatLevel");
                this.baseHealth = 100 + combatLevel * 5;
                this.maxHealth = baseHealth;
                this.health = maxHealth;

                this.baseTotalAbilityCharge = 25 + (int) (Math.floor(combatLevel / 5) * 5);
                this.totalAbilityCharge = baseTotalAbilityCharge;
                this.abilityCharge = totalAbilityCharge;

                this.combatLevel = combatLevel;

                double x = results.getInt("privateLocX");
                double y = results.getInt("privateLocY");
                double z = results.getInt("privateLocZ");

                this.privateAreaLocation = new Location(Bukkit.getWorld("Hub"), x, y, z);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            this.baseHealth = 100;
            this.maxHealth = 100;
            this.health = 100;
            this.projTrail = "Basic";
            this.baseTotalAbilityCharge = 25;
            this.totalAbilityCharge = 25;
            this.abilityCharge = 25;

            // getting the new private island location
            try {
                PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM PlayerStats WHERE 1=1");

                ResultSet results = statement.executeQuery();
                if (results.last()) {
                    double x = results.getInt("privateLocX");
                    double y = results.getInt("privateLocY");
                    double z = results.getInt("privateLocZ");
                    this.privateAreaLocation = new Location(Bukkit.getWorld("Hub"), x, y, z - 1000);
                } else {
                    this.privateAreaLocation = new Location(Bukkit.getWorld("Hub"), 1000, 70, -10000);
                }

                this.generator = new PlayerHomeGenerator(privateAreaLocation);
                this.generator.generate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.attributes = new HashMap<>();
        this.uuid = player.getUniqueId();
        this.player = player;

        this.chat = Chat.ALL;

        this.addAttribute(ItemAttribute.LIFESTEAL, 1);

        for (Reward reward : Reward.getRewards()) {
            for (UUID uuid : reward.getReceivers()) {
                player.sendMessage(uuid.toString());
                if (uuid.equals(player.getUniqueId())) {
                    pendingRewards.add(reward);
                }
            }
        }

        // scoreboard for player
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("test", "dummy", "Hello");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Display Name");

        player.setScoreboard(scoreboard);

        PLAYER_MAP.put(uuid, this);
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth > 0) {
            this.maxHealth = maxHealth;
            int playerMaxHealth = 0;
            if (maxHealth > 0) {
                playerMaxHealth = 4;
            }
            if (maxHealth > 10) {
                playerMaxHealth = 8;
            }
            if (maxHealth > 25) {
                playerMaxHealth = 12;
            }
            if (maxHealth > 50) {
                playerMaxHealth = 16;
            }
            if (maxHealth > 100) {
                playerMaxHealth = 20;
            }
            if (maxHealth > 200) {
                playerMaxHealth = 22;
            }
            if (maxHealth > 400) {
                playerMaxHealth = 24;
            }
            if (maxHealth > 1600) {
                playerMaxHealth = 26;
            }
            if (maxHealth > 3200) {
                playerMaxHealth = 28;
            }
            if (maxHealth > 6400) {
                playerMaxHealth = 30;
            }
            if (maxHealth < this.health) {
                this.health = maxHealth;
            }
            this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(playerMaxHealth);
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

    public int getCombatLevel() {
        return combatLevel;
    }

    public void setCombatLevel(int newLevel) {
        this.combatLevel = newLevel;
        this.baseHealth += 5;
        updateAttributes();
        if (this.combatLevel % 5 == 0) {
            this.totalAbilityCharge += 5;
        }
        for (int i = 0; i < 3; i++)
        {
            Location locationOffset = new Location(Bukkit.getWorld("hub"), (Math.random() - 0.5) * 3, 0, (Math.random() - 0.5) * 3);
            Firework fw = (Firework) Bukkit.getWorld("hub").spawnEntity(this.player.getLocation().add(locationOffset), EntityType.FIREWORK);
            FireworkMeta fwm = fw.getFireworkMeta();

            Color[] colors = {Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME,
                    Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW};

            double min = 0;
            double max = colors.length;
            double diff = max - min;

            int random = (int) (min + Math.random() * diff);

            FireworkEffect fwe = FireworkEffect.builder().withColor(colors[random]).flicker(true).build();
            fwm.addEffect(fwe);
            fwm.setPower(0);
            fw.setFireworkMeta(fwm);
        }
        this.player.sendMessage("----------------------------");
        this.player.sendMessage(ChatColor.YELLOW + "Combat Level Up!");
        this.player.sendMessage(this.combatLevel - 1 + " -> " + this.combatLevel);
        this.player.sendMessage("Bonuses:");
        this.player.sendMessage(ChatColor.DARK_RED + "+5 ♥");
        if (this.combatLevel % 5 == 0) {
            this.player.sendMessage(ChatColor.GREEN + "+5 max ability charge!");
        } else {
            this.player.sendMessage(ChatColor.GREEN + "" + (5 - (this.combatLevel % 5)) + " more levels until you gain +5 max ability charge!");
        }
        this.player.sendMessage("----------------------------");
    }

    public void setCombatExp(double combatExp) {
        this.combatExp = combatExp;
        this.combatExp = Math.floor(this.combatExp * 100.0) / 100.0;
        double toNextLevel = calcCombatToNextLevel();
        while (combatExp > toNextLevel) {
            toNextLevel = calcCombatToNextLevel();
            setCombatLevel(this.combatLevel + 1);
            this.combatExp = combatExp - toNextLevel;
        }
    }

    public void addCombatExp(double combatExp) {
        setCombatExp(this.combatExp + combatExp);
        this.totalCombatExp += combatExp;
        String message = new StringBuilder().append(ChatColor.BLUE).append("+").append(combatExp).append(" ").append("(").append(this.combatExp).append("/").append(calcCombatToNextLevel()).append(")").toString();
        this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public double getCurrentExp() {
        return combatExp;
    }

    public double getTotalCombatExp() {
        return totalCombatExp;
    }

    public void getCombatExpFromMob(CustomMob mob) {
        double exp = mob.getExperience();
        double xpBonusMulti = (double) getValueOfAttribute(ItemAttribute.XPBONUS) / 100;
        exp *= 1D + xpBonusMulti;
        double levelMulti = Math.abs(this.combatLevel - mob.getLevel()) * 0.03;
        if (levelMulti > 1) {
            levelMulti = 1;
        }
        exp *= (1D - levelMulti);
        addCombatExp(exp);
    }

    public double calcCombatToNextLevel() {
        int nextLevel = this.combatLevel + 1;
        return calcCombatToLevel(nextLevel);
    }

    public double calcCombatToLevel(int level) {
        if (level <= 10) {
            return level * 100;
        }
        if (level <= 15) {
            return 1000 + (level - 10) * 200;
        }
        if (level <= 20) {
            return 2000 + (level - 15) * 400;
        }
        if (level <= 25) {
            return 4000 + (level - 20) * 800;
        }
        if (level <= 30) {
            return 8000 + (level - 25) * 1000;
        }
        if (level <= 32) {
            return 13000 + (level - 30) * 2000;
        }
        if (level == 33) {
            return 20000;
        }
        if (level <= 50) {
            return 20000 + (level - 33) * 5000;
        }
        if (level <= 65) {
            return 105000 + (level - 50) * 10000;
        }
        if (level <= 75) {
            double combatToNext = 255000;
            double power = level - 65;
            while (power > 0) {
                combatToNext *= 1.1;
                power--;
            }
            return combatToNext;
        }
        return Integer.MAX_VALUE;
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
        int extraHealth = 0;
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            CustomItem heldItem = CustomItem.fromName(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
            if (heldItem instanceof CustomWeapon) {
                if (((CustomWeapon) heldItem).getCombatLevelReq() <= this.combatLevel) {
                    for (ItemAttribute attribute : ((CustomWeapon) heldItem).getAttributes().keySet()) {
                        if (attribute == ItemAttribute.HEALTH) {
                            extraHealth += ((CustomWeapon) heldItem).getAttributeValue(ItemAttribute.HEALTH);
                        } else {
                            setAttribute(attribute, ((CustomWeapon) heldItem).getAttributeValue(attribute));
                        }
                    }
                }
            }
        }
        ItemStack[] armorPieces = player.getInventory().getArmorContents();
        for (ItemStack item : armorPieces) {
            if (item != null) {
                CustomArmor armor = (CustomArmor) CustomItem.fromName(item.getItemMeta().getDisplayName());
                if (armor != null) {
                    if (armor.getCombatLevelReq() <= this.combatLevel) {
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
        }
        int newHealth = 1;
        if (this.baseHealth + extraHealth > 0) {
            newHealth = this.baseHealth + extraHealth;
        }
        setMaxHealth(newHealth);
        setHealth(this.health);
        addAttribute(ItemAttribute.LIFESTEAL, 1);
    }

    public Player getPlayer() {
        return player;
    }

    public Location getPrivateAreaLocation() {
        return privateAreaLocation;
    }

    public PlayerHomeGenerator getGenerator() {
        return generator;
    }

    public void sendActionbarMessage() {
        String message = new StringBuilder().append(ChatColor.RED).append(this.health).append("/").append(this.maxHealth).append("       ").append(ChatColor.BLUE).append("(").append(this.combatExp).append("/").append(calcCombatToNextLevel()).append(")").toString();
        this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public List<CustomItemStack> getItemsInInventory() {
        List<CustomItemStack> returnedItems = new ArrayList<>();
        for (ItemStack item : this.player.getInventory()) {
            if (item == null) {
                continue;
            }
            if (!item.hasItemMeta()) {
                continue;
            }
            CustomItem customItem = CustomItem.fromName(item.getItemMeta().getDisplayName());
            if (customItem != null) {
                if (customItem instanceof CustomIngredient) {
                    int amountToAdd = 0;
                    for (CustomItemStack returnedItem : returnedItems) {
                        if (returnedItem.getItem().equals(customItem)) {
                            returnedItems.remove(returnedItem);
                            amountToAdd = returnedItem.getAmount();
                            break;
                        }
                    }
                    returnedItems.add(new CustomItemStack(customItem, item.getAmount() + amountToAdd));
                }
            }
        }
        return returnedItems;
    }

    public Map<CustomItem, CustomItemStack> getInventoryItemMap() {
        Map<CustomItem, CustomItemStack> returnedItems = new HashMap<>();
        for (ItemStack item : this.player.getInventory()) {
            if (item == null) {
                continue;
            }
            if (!item.hasItemMeta()) {
                continue;
            }
            CustomItem customItem = CustomItem.fromName(item.getItemMeta().getDisplayName());
            if (customItem != null) {
                if (customItem instanceof CustomIngredient) {
                    int amountToAdd = 0;
                    for (CustomItem customItem1 : returnedItems.keySet()) {
                        if (customItem1.equals(customItem)) {
                            amountToAdd = returnedItems.get(customItem1).getAmount();
                            returnedItems.remove(customItem1);
                            break;
                        }
                    }
                    returnedItems.put(customItem, new CustomItemStack(customItem, item.getAmount() + amountToAdd));
                }
            }
        }
        return returnedItems;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void invite(Party party) {
        this.inviteFrom = party.getLeader();
        String leaderName = party.getLeader().getPlayer().getName();
        TextComponent text = new TextComponent(leaderName + " has invited you to join their party!");
        text.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
        TextComponent text2 = new TextComponent("Click here to accept, or type /party accept!");
        text2.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        text2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Join " + leaderName + "'s party!")));
        text2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));
        player.spigot().sendMessage(text);
        player.spigot().sendMessage(text2);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            this.inviteFrom = null;
        }, 1200L);
    }

    public CustomPlayer getInviteFrom() {
        return this.inviteFrom;
    };

    public Party getParty() {
        return this.party;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Chat getChat() {
        return chat;
    }

    public List<Reward> getPendingRewards() {
        return pendingRewards;
    }

    public void removeReward(Reward reward) {
        if (pendingRewards.contains(reward)) {
            pendingRewards.remove(reward);
        }
    }

    public void addReward(Reward reward) {
        if (!pendingRewards.contains(reward)) {
            pendingRewards.add(reward);
        }
    }

    public void setRecentInput(String input) {
        this.recentInput = input;
    }

    public String getRecentInput() {
        return recentInput;
    }

    public static CustomPlayer fromPlayer(Player player) {
        return PLAYER_MAP.get(player.getUniqueId());
    }

}
