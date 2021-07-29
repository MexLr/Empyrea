package org.example.ataraxiawarmup.image;

import org.bukkit.ChatColor;
import org.example.ataraxiawarmup.item.customitem.*;

import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomItemRenderer {

    public void drawItem(CustomAttributableItem item) throws IOException {
        String baseImagePath = "";
        baseImagePath = "D:\\Games\\Eidolon\\items\\pngs\\";
        baseImagePath += item.getMaterial().toString().toLowerCase() + ".png";
        String resultImagePath = "";
        if (item instanceof CustomArmor) {
            resultImagePath = "D:\\Games\\Eidolon\\items\\armor\\";
            resultImagePath += ChatColor.stripColor(item.getItemMeta().getDisplayName()) + ".png";
        }
        if (item instanceof CustomWeapon) {
            resultImagePath = "D:\\Games\\Eidolon\\items\\weapon\\";
            resultImagePath += ChatColor.stripColor(item.getItemMeta().getDisplayName()) + ".png";
        }

        List<String> extraLore = new ArrayList<>();
        if (item instanceof CustomWeapon) {
            extraLore = ((CustomWeapon) item).getExtraLore();
        }
        if (item instanceof CustomArmor) {
            extraLore = ((CustomArmor) item).getExtraLore();
        }

        int size = item.getAttributes().keySet().size() * 40 + 464 + (extraLore.size() + 1) * 25;
        if (item instanceof CustomWeapon) {
            size += ((CustomWeapon) item).getElements().size() * 20;
        }
        BufferedImage result = new BufferedImage(512, size, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();

        BufferedImage startingImage = ImageIO.read(new File(baseImagePath));
        g.drawImage(startingImage, 128, 128, null);

        Font titleFont = new Font("18thCentury", Font.BOLD, 75 - item.getItemMeta().getDisplayName().length());
        g.setFont(titleFont);
        g.setColor(getNameColor(item));

        FontMetrics fm = g.getFontMetrics();
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        int actualWidth = fm.stringWidth(name);
        int blackSpace = result.getWidth() - actualWidth;
        int x = result.getWidth() - blackSpace / 2 - actualWidth;
        g.drawString(name, x, 100);

        Font rarityFont = new Font("18thCentury", Font.BOLD, 50);
        String rarity = ChatColor.stripColor(item.getRarity().getName().toUpperCase() + " ITEM");
        g.setFont(rarityFont);
        fm = g.getFontMetrics();
        actualWidth = fm.stringWidth(rarity);
        blackSpace = result.getWidth() - actualWidth;
        x = result.getWidth() - blackSpace / 2 - actualWidth;
        g.drawString(rarity, x, 424);

        int elements = 0;
        if (item instanceof CustomWeapon) {
            CustomWeapon weapon = (CustomWeapon) item;
            for (Element element : Element.getReverseElementOrder()) {
                if (weapon.getElements().contains(element)) {
                    Font elementFont = new Font("GNU Unifont", Font.BOLD, 20);
                    String elementString = getUnicode(element.getRepresentingChar()) + weapon.getLowerBounds().get(weapon.getElements().indexOf(element)) + "-" + weapon.getUpperBounds().get(weapon.getElements().indexOf(element));;
                    g.setFont(elementFont);
                    g.setColor(getColor(element.getColor() + element.getName()));
                    fm = g.getFontMetrics();
                    actualWidth = fm.stringWidth(elementString);
                    blackSpace = result.getWidth() - actualWidth;
                    x = result.getWidth() - blackSpace / 2 - actualWidth;
                    g.drawString(elementString, x, 454 + elements * 20);
                    elements++;
                }
            }
        }

        Font combatMinFont = new Font("18thCentury", Font.BOLD, 30);
        String combatMinString = "Combat Lv. Min: " + item.getCombatLevelReq();
        g.setFont(combatMinFont);
        g.setColor(Color.LIGHT_GRAY);
        fm = g.getFontMetrics();
        actualWidth = fm.stringWidth(combatMinString);
        blackSpace = result.getWidth() - actualWidth;
        x = result.getWidth() - blackSpace / 2 - actualWidth;
        g.drawString(combatMinString, x, 459 + elements * 20);

        int attributes = 1;
        for (ItemAttribute attribute : ItemAttribute.getAttributeOrder()) {
            if (attribute.getName() == "All") {
                continue;
            }
            if (item.getAttributes().keySet().contains(attribute)) {
                Font attributeFont = new Font("18thCentury", Font.BOLD, 40);
                if (attribute.getName() == "♥") {
                    attributeFont = new Font("GNU Unifont", Font.BOLD, 40);
                }
                String attributeString = "+" + item.getAttributeValue(attribute) + ChatColor.stripColor(attribute.getName());
                g.setFont(attributeFont);
                g.setColor(getColor(attribute));
                fm = g.getFontMetrics();
                actualWidth = fm.stringWidth(attributeString);
                blackSpace = result.getWidth() - actualWidth;
                x = result.getWidth() - blackSpace / 2 - actualWidth;
                g.drawString(attributeString, x, 459 + elements * 20 + attributes * 40);
                attributes++;
            }
        }

        int loreLines = 0;
        for (String string : extraLore) {
            Font loreFont = new Font("18thCentury", Font.BOLD, 30);
            String loreString = ChatColor.stripColor(string);
            g.setFont(loreFont);
            g.setColor(Color.DARK_GRAY);
            fm = g.getFontMetrics();
            actualWidth = fm.stringWidth(loreString);
            blackSpace = result.getWidth() - actualWidth;
            x = result.getWidth() - blackSpace / 2 - actualWidth;
            g.drawString(loreString, x, 459 + elements * 20 + attributes * 40 + loreLines * 25);
            loreLines++;
        }

        ImageIO.write(result, "png", new File(resultImagePath));
    }

    public Color getColor(ItemAttribute attribute) {
        char attColor = attribute.getColor().getChar();
        switch (attColor) {
            case '0':
                return Color.BLACK;
            case '1':
                return Color.BLUE;
            case '2':
                return new Color(0, 127, 0);
            case '3':
                return new Color(0, 127, 127);
            case '4':
                return new Color(127, 0, 0);
            case '5':
                return new Color(127, 0, 127);
            case '6':
                return Color.ORANGE;
            case '7':
                return Color.GRAY;
            case '8':
                return Color.DARK_GRAY;
            case '9':
                return new Color(63, 63, 255);
            case 'a':
                return Color.GREEN;
            case 'b':
                return Color.CYAN;
            case 'c':
                return Color.RED;
            case 'd':
                return new Color(255, 0, 255);
            case 'e':
                return Color.YELLOW;
            case 'f':
                return Color.WHITE;
            default:
                return null;
        }
    }

    public Color getNameColor(CustomItem item) {
        char colorChar = item.getRarity().getColor().getChar();
        switch (colorChar) {
            case '0':
                return Color.BLACK;
            case '1':
                return Color.BLUE;
            case '2':
                return new Color(0, 127, 0);
            case '3':
                return new Color(0, 127, 127);
            case '4':
                return new Color(127, 0, 0);
            case '5':
                return new Color(127, 0, 127);
            case '6':
                return Color.ORANGE;
            case '7':
                return Color.GRAY;
            case '8':
                return Color.DARK_GRAY;
            case '9':
                return new Color(63, 63, 255);
            case 'a':
                return Color.GREEN;
            case 'b':
                return Color.CYAN;
            case 'c':
                return Color.RED;
            case 'd':
                return new Color(255, 0, 255);
            case 'e':
                return Color.YELLOW;
            case 'f':
                return Color.WHITE;
            default:
                return null;
        }
    }

    public Color getColor(String string) {
        switch (string.charAt(1)) {
            case '0':
                return Color.BLACK;
            case '1':
                return Color.BLUE;
            case '2':
                return new Color(0, 127, 0);
            case '3':
                return new Color(0, 127, 127);
            case '4':
                return new Color(127, 0, 0);
            case '5':
                return new Color(127, 0, 127);
            case '6':
                return Color.ORANGE;
            case '7':
                return Color.GRAY;
            case '8':
                return Color.DARK_GRAY;
            case '9':
                return new Color(63, 63, 255);
            case 'a':
                return Color.GREEN;
            case 'b':
                return Color.CYAN;
            case 'c':
                return Color.RED;
            case 'd':
                return new Color(255, 0, 255);
            case 'e':
                return Color.YELLOW;
            case 'f':
                return Color.WHITE;
            default:
                return null;
        }
    }

    public String getUnicode(String character) {
        switch (character) {
            case "✤":
                return "\u2724";
            case "✹":
                return "\u2739";
            case "❉":
                return "\u2749";
            case "✦":
                return "\u2726";
            case "❋":
                return "\u274B";
            case "✯":
                return "\u272F";
            default:
                return "";
        }
    }

}
