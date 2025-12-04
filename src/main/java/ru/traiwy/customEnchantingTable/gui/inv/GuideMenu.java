package ru.traiwy.customEnchantingTable.gui.inv;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GuideMenu implements MenuTable {
    public static final int[] GRAY_PANEL = {0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 51, 52, 53};

    private int currentPage = 0;
    private final int itemsPage = 28;
    private final List<ItemStack> enchantBooks = new ArrayList<>();

    private Inventory inventory;
    private final ConfigData configData;

    /*
    c - book
    n - null]
    a - arrow
    b - barrier
    t - table
    e - ender eye

    */

    public static final String[] inv = {
            "____c____",
            "_nnnnnnn_",
            "_nnnnnnn_",
            "_nnnnnnn_",
            "_nnnnnnn_",
            "___abte__"
    };


    public GuideMenu(ConfigData configData) {
        this.configData = configData;
        this.inventory = Bukkit.createInventory(this, 54, "Enchantments Guide page: " + currentPage);

        loadEnchantBooks();
        build();
    }

    @Override
    public void build() {
        //inventory.clear();

        Map<Character, ItemStack> item = new HashMap<>();
        item.put('_', ItemUtil.createItem(Material.BLACK_STAINED_GLASS_PANE, null, null));
        item.put('n', null);
        item.put('c', ItemUtil.createItem(Material.BOOK, null, null));
        item.put('a', ItemUtil.createItem(Material.ARROW, null, null));
        item.put('b', ItemUtil.createItem(Material.BARRIER, null, null));
        item.put('t', ItemUtil.createItem(Material.OAK_SIGN, null, null));
        item.put('e', ItemUtil.createItem(Material.ENDER_EYE, null, null));


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

        placePageItems();
    }

    private void placePageItems() {
        final int start = currentPage * itemsPage;
        final int end = Math.min(start + itemsPage, enchantBooks.size());

        int itemIndex = start;

        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= 7; col++) {
                int slot = row * 9 + col;

                if (itemIndex < end) {
                    inventory.setItem(slot, enchantBooks.get(itemIndex));
                    itemIndex++;
                }
            }
        }
    }

    @Override
    public void click(InventoryClickEvent event) {
        event.setCancelled(true);

        final int slot = event.getRawSlot();
         Player player = (Player) event.getWhoClicked();

        switch (slot) {
            case 53 -> {
                if ((currentPage + 1) * itemsPage < enchantBooks.size()) {
                    currentPage++;
                    player.sendMessage("" + currentPage);
                    refresh(player);
                    player.openInventory(inventory);
                }
            }
            case 45 -> {
                if (currentPage > 0) {
                    currentPage--;
                    refresh(player);
                    player.openInventory(inventory);
                }
            }

            case 49 -> {
                player.closeInventory();
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void refresh(Player player) {
        Inventory newInv = Bukkit.createInventory(this, inventory.getSize(),
                "Enchantments Guide page: " + currentPage);

        for (int i = 0; i < inventory.getSize(); i++) {
            newInv.setItem(i, inventory.getItem(i));
        }

        this.inventory = newInv;

        build();
        player.openInventory(newInv);
    }


    private void loadEnchantBooks() {
        enchantBooks.clear();

        enchantBooks.add(ItemUtil.createItem(Material.ENCHANTED_BOOK,
                Component.text("Sharpness I"),
                List.of(Component.text("Увеличивает урон меча"))
        ));

        enchantBooks.add(ItemUtil.createItem(Material.ENCHANTED_BOOK,
                Component.text("Efficiency V"),
                List.of(Component.text("Ускоряет добычу блоков"))
        ));

        enchantBooks.add(ItemUtil.createItem(Material.ENCHANTED_BOOK,
                Component.text("Unbreaking III"),
                List.of(Component.text("Повышает прочность вещей"))
        ));
        enchantBooks.add(ItemUtil.createItem(Material.ENCHANTED_BOOK,
                Component.text("Unbreaking III"),
                List.of(Component.text("Повышает прочность вещей"))
        ));

    }
}
