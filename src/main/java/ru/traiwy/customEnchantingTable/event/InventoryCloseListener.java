package ru.traiwy.customEnchantingTable.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.customEnchantingTable.gui.inv.LevelMenu;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof MainMenu || inventory.getHolder() instanceof LevelMenu)) return;

        Player player = (Player) event.getPlayer();
        int slot = 19;
        ItemStack item = inventory.getItem(slot);


        if (item == null || item.getType() == Material.AIR) return;

        player.getWorld().dropItemNaturally(player.getLocation(), item);
    }
}
