package ru.traiwy.customEnchantingTable.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ClickService implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final Inventory top = event.getView().getTopInventory();

        if (!(top.getHolder() instanceof MenuTable menu)) return;

        menu.click(event);


    }
}
