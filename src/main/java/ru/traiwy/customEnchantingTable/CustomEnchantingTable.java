package ru.traiwy.customEnchantingTable;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.traiwy.customEnchantingTable.command.GiveCommand;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.event.EnchantTableOpenListener;
import ru.traiwy.customEnchantingTable.gui.ClickService;
import ru.traiwy.customEnchantingTable.gui.inv.GuideMenu;
import ru.traiwy.customEnchantingTable.gui.inv.MainMenu;
import ru.traiwy.customEnchantingTable.manager.ItemManager;

@Getter
public final class CustomEnchantingTable extends JavaPlugin {

    @Getter
    public static CustomEnchantingTable instance;

    private GuideMenu guideMenu;

    @Override
    public void onEnable() {
        instance = this;
        final ConfigData configData = new ConfigData();

         final MainMenu mainMenu = new MainMenu(this);
        this.guideMenu = new GuideMenu(configData);

        final ItemManager itemManager = new ItemManager();



        getServer().getPluginManager().registerEvents(new EnchantTableOpenListener(mainMenu), this);
        getServer().getPluginManager().registerEvents(new ClickService(), this);
        getCommand("giveTable").setExecutor(new GiveCommand(itemManager));


    }

    @Override
    public void onDisable() {
    }
}
