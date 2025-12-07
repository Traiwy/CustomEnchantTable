package ru.traiwy.customEnchantingTable;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.customEnchantingTable.command.GiveCommand;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.event.EnchantTableOpenListener;
import ru.traiwy.customEnchantingTable.event.InventoryCloseListener;
import ru.traiwy.customEnchantingTable.gui.ClickService;
import ru.traiwy.customEnchantingTable.gui.inv.GuideMenu;
import ru.traiwy.customEnchantingTable.gui.inv.main.EnchantLevelManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.EnchantManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.item.ItemMenuManager;
import ru.traiwy.customEnchantingTable.gui.inv.main.item.MenuManager;
import ru.traiwy.customEnchantingTable.manager.ConfigManager;
import ru.traiwy.customEnchantingTable.manager.ItemManager;
import ru.traiwy.customEnchantingTable.util.BookshelfPowerCalculator;

@Getter
public final class CustomEnchantingTable extends JavaPlugin {

    @Getter
    public static CustomEnchantingTable instance;

    private GuideMenu guideMenu;
    private EnchantLevelManager enchantLevelManager;
    private EnchantManager enchantManager;
    private ItemMenuManager itemMenuManager;
    private MenuManager menuManager;


    @Override
    public void onEnable() {
        instance = this;
        final ConfigManager configManager = new ConfigManager(this);
         final ConfigData configData = configManager.getConfigData();
        enchantLevelManager = new EnchantLevelManager(configData);
        enchantManager = new EnchantManager();
        itemMenuManager = new ItemMenuManager();
        menuManager = new MenuManager();


        this.guideMenu = new GuideMenu(configData, menuManager);

        final ItemManager itemManager = new ItemManager();
        final BookshelfPowerCalculator calculator = new BookshelfPowerCalculator();



        getServer().getPluginManager().registerEvents(new EnchantTableOpenListener(calculator, this, enchantLevelManager, configData, itemMenuManager, menuManager), this);
        getServer().getPluginManager().registerEvents(new ClickService(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(itemMenuManager, menuManager), this);
        getServer().getPluginManager().registerEvents(guideMenu, this);
        getCommand("giveTable").setExecutor(new GiveCommand(itemManager));


    }

    @Override
    public void onDisable() {
    }

}
