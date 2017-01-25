package me.noeffort.spawnershops;

import lombok.*;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@Data
public class SpawnShopItem {

    public SpawnShopItem(ItemStack item, String type, int cost, EntityType eType) {
        this.item = item;
        this.type = type;
        this.cost = cost;
        this.entityType = eType.name();
        ItemStack r = item.clone();
        ItemMeta meta = r.getItemMeta();
        meta.setLore(new ArrayList<>());
        r.setItemMeta(meta);
        this.returnedItem = r;
    }

    private ItemStack item;
    private String type;
    private String entityType;
    private int cost;
    @Setter(AccessLevel.NONE) private ItemStack returnedItem;

}
