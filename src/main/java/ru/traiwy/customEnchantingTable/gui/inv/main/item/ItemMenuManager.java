package ru.traiwy.customEnchantingTable.gui.inv.main.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemMenuManager {
    private final Map<UUID, ItemStack> items = new HashMap<>();

    public void setItem(Player player, ItemStack item) {
        if (item == null) return;
        items.put(player.getUniqueId(), item);
    }

    public ItemStack getItem(Player player) {
        return items.get(player.getUniqueId());
    }

    public void removeItem(Player player) {
        items.remove(player.getUniqueId());
    }
}
