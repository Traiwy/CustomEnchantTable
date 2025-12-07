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



public class MainMenu implements MenuTable {

    public static final String[] inv = {
            "ggggggggg",
            "g_______g",
            "on___d__o",
            "ot______o",
            "g_______g",
            "gggbacggg"
    };

    public static final int ITEM_SLOT = 19;

    private final Inventory inventory;
    private final JavaPlugin plugin;
    private final EnchantLevelManager enchantLevelManager = CustomEnchantingTable.getInstance().getEnchantLevelManager();
    private final EnchantManager manager = CustomEnchantingTable.getInstance().getEnchantManager();
    private int currentLevel;
    private int bookshelfCount;
    private final ConfigData configData;

    private static final int[] materialAir = {12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 39, 40, 41, 42, 43};

    public MainMenu(JavaPlugin plugin, ConfigData configData) {
        this.plugin = plugin;
        this.configData = configData;
        inventory = Bukkit.createInventory(this, 54, "Чародейский стол");
    }

    @Override
    public void build() {
        Map<Character, ItemStack> itemMap = createItems();
        fillPattern(inventory, inv, itemMap);
    }

    private Map<Character, ItemStack> createItems() {
        Map<Character, ItemStack> items = new HashMap<>();

        Component name = styled("name");
        List<Component> loreEmpty = styledLore("lore");

        List<Component> loreBookShelf = List.of(
                styled("Количество полок: " + bookshelfCount),
                styled("Уровень стола: " + currentLevel)
        );

        items.put('_', pane(Material.GRAY_STAINED_GLASS_PANE));
        items.put('n', new ItemStack(Material.AIR));
        items.put('d', item(Material.GRAY_DYE, name, loreEmpty));
        items.put('t', item(Material.ENCHANTING_TABLE, Component.text(""), null));
        items.put('b', item(Material.BOOKSHELF, name, loreBookShelf));
        items.put('a', item(Material.BARRIER, name, loreEmpty));
        items.put('c', item(Material.BOOK, name, loreEmpty));
        items.put('h', item(Material.HOPPER, name, loreEmpty));
        items.put('g', pane(Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        items.put('o', pane(Material.ORANGE_STAINED_GLASS_PANE));

        return items;
    }

    private void fillPattern(Inventory inv, String[] pattern, Map<Character, ItemStack> map) {
        int slot = 0;
        for (String row : pattern) {
            for (char c : row.toCharArray()) {
                inv.setItem(slot++, map.getOrDefault(c, null));
            }
        }
    }

    private Component styled(String text) {
        return Component.text(text)
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);
    }

    private List<Component> styledLore(String text) {
        return List.of(styled(text));
    }

    private ItemStack pane(Material material) {
        return item(material, null, null);
    }

    private ItemStack item(Material material, Component name, List<Component> lore) {
        return ItemUtil.createItem(material, name, lore);
    }

    public void open(Player player, int levelTable, int countBookShelf) {
        this.currentLevel = levelTable;
        this.bookshelfCount = countBookShelf;
        build();
        player.openInventory(inventory);
    }

    public void open(Player player) {
        ItemStack saved = CustomEnchantingTable.instance.getItem(player);
        player.sendMessage("item" + saved);
        inventory.setItem(ITEM_SLOT, saved);

        player.openInventory(inventory);
    }

    @Override
    public void click(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof MenuTable)) return;

        final int slot = event.getRawSlot();
        final ItemStack clickedItem = event.getCurrentItem();

        if (slot < top.getSize()) {
            event.setCancelled(slot != ITEM_SLOT);

            switch (slot){
                case 49 -> player.closeInventory();
                case 50 -> {
                    CustomEnchantingTable.getInstance().setMenu(player, this);
                    player.openInventory(CustomEnchantingTable.getInstance().getGuideMenu().getInventory());
                    return;
                }
            }
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
                CustomEnchantingTable.getInstance().setItem(player, itemInBookSlot);
                LevelMenu levelMenu = new LevelMenu(clickedItem, configData, player);
                Inventory levelInv = levelMenu.getInventory();

                CustomEnchantingTable.getInstance().setMenu(player, this);
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

}
