package org.example.ataraxiawarmup.item.customitem;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomAttributableItem extends CustomCooldownableItem {

    private Map<ItemAttribute, Integer> attributeMap = new HashMap<>();

    public CustomAttributableItem(Material material, String name, Rarity rarity, CustomItemStack[] recipeMatrix, Map<ItemAttribute, Integer> attributeMap) {
        super(material, name, rarity, recipeMatrix);
        this.attributeMap.putAll(attributeMap);
    }

    public Integer getAttributeValue(ItemAttribute attribute) {
        if (this.attributeMap.containsKey(attribute)) {
            return this.attributeMap.get(attribute);
        }
        return null;
    }

    public Map<ItemAttribute, Integer> getAttributes() {
        return this.attributeMap;
    }

    public void setAttribute(ItemAttribute attribute, int value) {
        if (this.attributeMap.containsKey(attribute)) {
            this.attributeMap.replace(attribute, value);
        } else {
            this.attributeMap.put(attribute, value);
        }
    }

    public void setAttributes(Map<ItemAttribute, Integer> attributeMap) {
        this.attributeMap.clear();
        this.attributeMap.putAll(attributeMap);
    }

    public void addToAttribute(ItemAttribute attribute, int value) {
        if (this.attributeMap.containsKey(attribute)) {
            setAttribute(attribute, this.attributeMap.get(attribute) + value);
        } else {
            setAttribute(attribute, value);
        }
    }

    @Override
    public CustomAttributableItem clone() {
        CustomAttributableItem item = (CustomAttributableItem) super.clone();

        Map<ItemAttribute, Integer> attributeIntegerMap = new HashMap<>();
        attributeIntegerMap.putAll(this.attributeMap);
        item.attributeMap = attributeIntegerMap;

        return item;
    }
}
