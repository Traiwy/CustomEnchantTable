package ru.traiwy.customEnchantingTable.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.gui.inv.main.EnchantLevelManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;
import ru.traiwy.customEnchantingTable.gui.inv.main.item.ItemMenuManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.item.MenuManager;
import ru.traiwy.customEnchantingTable.util.BookshelfPowerCalculator;

import java.util.Random;

@AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnchantTableOpenListener implements Listener {
    BookshelfPowerCalculator calculator;
    JavaPlugin plugin;
    EnchantLevelManager manager;
    ConfigData configData;
    ItemMenuManager itemMenuManager;
    MenuManager menuManager;

    private final Random random = new Random();

    @EventHandler
    public void  EnchantTableOpen(PlayerInteractEvent event) {

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.ENCHANTING_TABLE) return;
        final Location locBlock = block.getLocation();

        int countBookShelf = calculator.calculatePower(locBlock);
        Bukkit.getLogger().info("Сила: " + countBookShelf);
        int levelTable =  randomLevelTable(countBookShelf);
        Bukkit.getLogger().info("Уровень: " + levelTable);

        manager.setTableData(levelTable, countBookShelf);

        event.setCancelled(true);

        Player player = event.getPlayer();

        MainMenu mainMenu = new MainMenu(plugin, configData, itemMenuManager, menuManager);
        mainMenu.open(player, levelTable, countBookShelf);
    }

    public int randomLevelTable(int countBookShelf){
        int level = 0;

        if(countBookShelf >= 0 && countBookShelf <= 2){
            level = random.nextInt(1, 5);
        } else if (countBookShelf >= 3 &&  countBookShelf <= 5) {
            level = random.nextInt(5, 10);
        } else if (countBookShelf >= 6 && countBookShelf <= 8) {
            level = random.nextInt(10, 15);
        } else if (countBookShelf >= 9 && countBookShelf <= 11) {
            level = random.nextInt(15,20);
        } else if (countBookShelf >= 12 && countBookShelf <= 14) {
            level = random.nextInt(20, 27);
        } else if (countBookShelf >= 15) {
            level = 30;
        }

        return level;
    }
}
