package me.noeffort.spawnershops;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil extends Main {

    public static ItemStack addItem(List<String> lore, String displayName, Material material) {
        ItemStack i = new ItemStack(material);
        ItemMeta meta = i.getItemMeta();
        meta.setLore(lore);
        meta.setDisplayName(displayName);
        i.setItemMeta(meta);
        return i;
    }

    public static SpawnShopItem getSpawnerItem(ItemStack itemStack) {
        for(SpawnShopItem i : Main.getItems()) {
            if(itemStack.equals(i.getItem())) {
                return i;
            }
        }
        return null;
    }

    public static SpawnShopItem getReturnedItem(ItemStack itemStack) {
        for(SpawnShopItem i : Main.getItems()) {
            if(itemStack.equals(i.getReturnedItem())) {
                return i;
            }
        }
        return null;
    }


}
