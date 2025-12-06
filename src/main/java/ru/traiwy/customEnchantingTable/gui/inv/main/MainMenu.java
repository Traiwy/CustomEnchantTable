package ru.traiwy.customEnchantingTable.gui.inv.main;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.CustomEnchantingTable;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.gui.inv.LevelMenu;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.*;

public class MainMenu implements MenuTable {

    public static final String[] inv = {
            "_________",
            "_________",
            "_n___d___",
            "_t_______",
            "_________",
            "___bach__"
    };

    public static final int ITEM_SLOT = 19;
    private final Inventory inventory;
    private final JavaPlugin plugin;
    private final EnchantLevelManager enchantLevelManager = CustomEnchantingTable.getInstance().getEnchantLevelManager();
    private final EnchantManager manager = CustomEnchantingTable.getInstance().getEnchantManager();

    private int currentLevel;
    private int bookshelfCount;
    private final ItemStack targetItem;
    private final ConfigData configData;

    public MainMenu(JavaPlugin plugin, ItemStack targetItem, ConfigData configData) {
        this.plugin = plugin;
        this.targetItem = targetItem;
        this.configData = configData;
        inventory = Bukkit.createInventory(this, 54, "Enchanting item");
    }

    @Override
    public void build() {

        final Component textName = Component.text("name")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);

        final List<Component> lore = Component.text("lore")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true).children();

        final List<Component> loreBookShelf = List.of(
                Component.text("Количество полок: " + bookshelfCount)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true),
                Component.text("Уровень стола: " + currentLevel)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true));


        Map<Character, ItemStack> item = new HashMap<>();
        item.put('_', ItemUtil.createItem(BLACK_STAINED_GLASS_PANE, null, null));
        item.put('n', new ItemStack(Material.AIR));
        item.put('d', ItemUtil.createItem(GRAY_DYE, textName, lore));
        item.put('t', ItemUtil.createItem(ENCHANTING_TABLE, textName, lore));
        item.put('b', ItemUtil.createItem(BOOKSHELF, textName, loreBookShelf));
        item.put('a', ItemUtil.createItem(BARRIER, textName, lore));
        item.put('c', ItemUtil.createItem(BOOK, textName, lore));
        item.put('h', ItemUtil.createItem(HOPPER, textName, lore));

        int slot = 0;
        for (String row : inv) {
            for (char c : row.toCharArray()) {
                ItemStack itemStack = item.getOrDefault(c, null);
                inventory.setItem(slot, itemStack);
                slot++;
            }
            while (slot % 9 != 0) {
                slot++;
            }
        }

    }


    public void open(Player player, int levelTable, int countBookShelf) {
        this.currentLevel = levelTable;
        this.bookshelfCount = countBookShelf;
         build();
        player.openInventory(inventory);
    }

    public void open(Player player) {

        ItemStack saved = CustomEnchantingTable.instance.getItem(player);

        if (saved != null) {
            inventory.setItem(ITEM_SLOT, saved.clone());
        } else if (targetItem != null) {
            inventory.setItem(ITEM_SLOT, targetItem.clone());
        }

        player.openInventory(inventory);
    }

    private static int[] materialAir = {12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34};


    @Override
    public void click(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof MenuTable)) return;

        final int slot = event.getRawSlot();
        final ItemStack clickedItem = event.getCurrentItem();


        if (slot < top.getSize()) {
            if (slot == ITEM_SLOT) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
            }
        }

        final Inventory inventoryClick = getClickInventory(clickedItem);
        if (inventoryClick != null) {
            player.openInventory(inventoryClick);
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            final ItemStack itemInBookSlot = top.getItem(ITEM_SLOT);

            if (itemInBookSlot == null || itemInBookSlot.getType() == Material.AIR) {
                clearSlots(top, materialAir);
                build();
                return;
            }

            List<Enchantment> enchantments = manager.getAllPossibleEnchantment(itemInBookSlot);
            if (!enchantments.isEmpty() && manager.canEnchantItem(itemInBookSlot)) {
                clearSlots(top, materialAir);
                for (int i = 0; i < enchantments.size() && i < materialAir.length; i++) {
                    ItemStack book = manager.createEnchantmentBook(enchantments.get(i));
                    top.setItem(materialAir[i], book);
                }
            }

            if (clickedItem != null && clickedItem.getType() == Material.ENCHANTED_BOOK) {
                LevelMenu levelMenu = new LevelMenu(itemInBookSlot, clickedItem, configData);
                Inventory levelInv = levelMenu.getInventory();
                CustomEnchantingTable.getInstance().setMenu(player, this);
                inventory.setItem(ITEM_SLOT, null);
                enchantLevelManager.updateDyeLevels(clickedItem, levelInv);


                levelMenu.open(player);
            }
        });
    }

    private void clearSlots(Inventory inv, int[] slots) {
        for (int slot : slots) {
            inv.setItem(slot, null);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public Inventory getClickInventory(ItemStack itemStack) {
        if (itemStack == null) return null;
        switch (itemStack.getType()) {
            case BOOK:
                return CustomEnchantingTable.getInstance().getGuideMenu().getInventory();
            default:
                return null;
        }
    }


}



