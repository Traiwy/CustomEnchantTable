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
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import ru.traiwy.customEnchantingTable.gui.inv.MainMenu;
import ru.traiwy.customEnchantingTable.util.BookshelfPowerCalculator;

import java.util.Random;

@AllArgsConstructor @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnchantTableOpenListener implements Listener {
    private final MainMenu mainMenu;
    private final BookshelfPowerCalculator calculator;


    private final Random random = new Random();

    @EventHandler
    public void  EnchantTableOpen(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        final Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.ENCHANTING_TABLE) return;
        final Location locBlock = block.getLocation();

        int countBookShelf = calculator.calculatePower(locBlock);
        Bukkit.getLogger().info("Количество блоков: " + countBookShelf);
        int levelTable =  randomLevelTable(countBookShelf);
        Bukkit.getLogger().info("Левел стола: "+  levelTable);


        event.setCancelled(true);

        Player player = event.getPlayer();
        mainMenu.open(player, levelTable, countBookShelf);

    }

    private int randomLevelTable(int countBookShelf){
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
