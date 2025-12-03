package ru.traiwy.customEnchantingTable.event;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.traiwy.customEnchantingTable.gui.inv.MainMenu;

@AllArgsConstructor
public class EnchantTableOpenListener implements Listener {
    private final MainMenu mainMenu;

    @EventHandler
    public void  EnchantTableOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Block block = event.getClickedBlock();
        if (block == null) return;

        if (block.getType() == Material.ENCHANTING_TABLE) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            mainMenu.open(player);
        }
    }
}
