package ru.traiwy.customEnchantingTable;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.customEnchantingTable.command.GiveCommand;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.event.EnchantTableOpenListener;
import ru.traiwy.customEnchantingTable.gui.ClickService;
import ru.traiwy.customEnchantingTable.gui.inv.GuideMenu;
import ru.traiwy.customEnchantingTable.gui.inv.LevelMenu;
import ru.traiwy.customEnchantingTable.gui.inv.main.EnchantLevelManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.EnchantManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;
import ru.traiwy.customEnchantingTable.manager.ConfigManager;
import ru.traiwy.customEnchantingTable.manager.ItemManager;
import ru.traiwy.customEnchantingTable.util.BookshelfPowerCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public final class CustomEnchantingTable extends JavaPlugin {

    @Getter
    public static CustomEnchantingTable instance;

    private GuideMenu guideMenu;
    private EnchantLevelManager enchantLevelManager;
    private EnchantManager enchantManager;


    @Override
    public void onEnable() {
        instance = this;
        enchantLevelManager = new EnchantLevelManager();
        enchantManager = new EnchantManager();
         final ConfigManager configManager = new ConfigManager(this);
        final ConfigData configData = configManager.getConfigData();


        this.guideMenu = new GuideMenu(configData);


        final ItemManager itemManager = new ItemManager();
        final BookshelfPowerCalculator calculator = new BookshelfPowerCalculator();



        getServer().getPluginManager().registerEvents(new EnchantTableOpenListener(calculator, this, enchantLevelManager), this);
        getServer().getPluginManager().registerEvents(new ClickService(), this);
        getCommand("giveTable").setExecutor(new GiveCommand(itemManager));


    }

    @Override
    public void onDisable() {
    }

    private final Map<UUID, MainMenu> menus = new HashMap<>();

    public MainMenu getMenu(Player p) {
        return menus.get(p.getUniqueId());
    }

    public void setMenu(Player p, MainMenu menu) {
        menus.put(p.getUniqueId(), menu);
    }


    private final Map<UUID, ItemStack> items = new HashMap<>();

    public void updateItem(Player player, ItemStack item) {
        items.put(player.getUniqueId(), item.clone());
    }

    public ItemStack getItem(Player player) {
        return items.get(player.getUniqueId());
    }
}
