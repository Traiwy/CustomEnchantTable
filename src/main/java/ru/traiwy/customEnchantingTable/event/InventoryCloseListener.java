package ru.traiwy.customEnchantingTable.event;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.customEnchantingTable.gui.inv.LevelMenu;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;
import ru.traiwy.customEnchantingTable.gui.inv.main.item.ItemMenuManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.item.MenuManager;

@AllArgsConstructor
public class InventoryCloseListener implements Listener {
    private final ItemMenuManager manager;
    private final MenuManager menuManager;

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Inventory inventory = event.getInventory();

        Player player = (Player) event.getPlayer();
        if (menuManager.isSwitching(player)) return;

        if (!(inventory.getHolder() instanceof MainMenu || inventory.getHolder() instanceof LevelMenu)) return;
        menuManager.removeMenu(player);

        int slot = 19;
        ItemStack item = inventory.getItem(slot);

        if (item == null || item.getType() == Material.AIR) return;

        manager.removeItem(player);
        menuManager.unmarkSwitching(player);
        player.getWorld().dropItemNaturally(player.getLocation(), item);
    }
}
