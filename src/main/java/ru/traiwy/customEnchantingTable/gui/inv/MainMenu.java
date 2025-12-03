package ru.traiwy.customEnchantingTable.gui.inv;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.CustomEnchantingTable;
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.*;
import static org.bukkit.Material.BOOK;

public class MainMenu implements MenuTable{
    //p - panel
    //n - null
    // d - dye
    //t - enchant table
    //b - bookshelf
    //a - barriers
    //c - book
    // h - hopper
    public static final String[] inv = {
            "_________",
            "_________",
            "_n___d___",
            "_t_______",
            "_________",
            "___bach__"
    };

    //public static final String[] invBook = {
    //        "_________",
    //        "___nnnnnn_",
    //        "_n_nnnnnn_",
    //        "_t_nnnnnn_",
    //        "_________",
    //        "___bach__"
    //};

    private final int BOOK_SLOT = 19;
    private final Inventory inventory;
    private final JavaPlugin plugin;

    public MainMenu(JavaPlugin plugin){
        this.plugin = plugin;
        inventory = Bukkit.createInventory(this, 54, "Enchanting item");
        build();

    }
    @Override
    public void build() {

         final Component textName = Component.text("name")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);

        final List<Component> lore = Component.text("lore")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true).children();

        Map<Character, ItemStack> item = new HashMap<>();
        item.put('_', ItemUtil.createItem(BLACK_STAINED_GLASS_PANE, null, null ));
        item.put('n', null);
        item.put('d', ItemUtil.createItem(GRAY_DYE, textName, lore));
        item.put('t', ItemUtil.createItem(ENCHANTING_TABLE, textName, lore));
        item.put('b', ItemUtil.createItem(BOOKSHELF, textName, lore));
        item.put('a', ItemUtil.createItem(BARRIER, textName, lore));
        item.put('c', ItemUtil.createItem(BOOK, textName, lore));
        item.put('h', ItemUtil.createItem(HOPPER, textName, lore));

        int slot = 0;
        for(String row : inv){
            for(char c : row.toCharArray()){
                ItemStack itemStack = item.getOrDefault(c, null);
                inventory.setItem(slot, itemStack);
                slot++;
            }
            while (slot % 9 != 0){
                slot++;
            }
        }
    }


    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    private int[] materialAir = {12,13,14,15,16,21,22,23,24,25,30,31,32,33,34};


    @Override
    public void click(InventoryClickEvent event) {
        final Inventory inv = event.getInventory();

        final Player player = (Player) event.getWhoClicked();
        final ItemStack newItem = new ItemStack(AIR);

        final ItemStack itemInv = event.getCurrentItem();
        final Inventory inventoryClick = getClickInventory(itemInv);


        if(inventoryClick != null){
            player.openInventory(inventoryClick);
            return;
        }else{
            player.sendMessage("Инвентори null");
        }


        Bukkit.getScheduler().runTask(plugin, () -> {
            final ItemStack item = inv.getItem(BOOK_SLOT);
            player.sendMessage("Предмет: " + item);
            if (item != null && item.getType() != AIR) {
                player.sendMessage("1");
                for (int i : materialAir) {
                    inv.setItem(i, newItem);
                }

            } else {
                build();
            }
        });
        ;

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public Inventory getClickInventory(ItemStack itemStack) {

        if (itemStack == null) {
            Bukkit.getLogger().info("Итем из нул");
            return null;
        }
        Bukkit.getLogger().info("" + itemStack.getType());

        switch (itemStack.getType()) {
            case BOOK:
                return CustomEnchantingTable.getInstance().getGuideMenu().getInventory();
            default:
                return null;
        }
    }

}

