package ru.traiwy.customEnchantingTable.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public interface MenuTable extends InventoryHolder {
    default void build() {};
    default void click(InventoryClickEvent event) {};
    default void open(Player player) {};
}
