package ru.traiwy.customEnchantingTable.gui.inv;

import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.CustomEnchantingTable;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bukkit.Material.LIGHT_BLUE_STAINED_GLASS_PANE;
import static org.bukkit.Material.ORANGE_STAINED_GLASS_PANE;

@Slf4j
public class GuideMenu implements InventoryHolder, Listener {
    public static final int[] GRAY_PANEL = {0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 51, 52, 53};

    private int currentPage = 0;
    private final int itemsPage = 28;
    private final List<ItemStack> enchantBooks = new ArrayList<>();

    private Inventory inventory;
    private final ConfigData configData;

    String title = "(%s/2)  Справочник зачарований".replace("%s", String.valueOf(currentPage));

    /*
    c - book
    n - null]
    a - arrow
    b - barrier
    t - table
    e - ender eye

    */

    public static final String[] inv = {
            "ggggcgggg",
            "gnnnnnnng",
            "onnnnnnno",
            "rnnnnnnnr",
            "gnnnnnnng",
            "ggggggggr"
    };


    public GuideMenu(ConfigData configData) {
        this.configData = configData;
        this.inventory = Bukkit.createInventory(this, 54, getTitle());

        loadEnchantBooks();
        build();
    }

    public void build() {
        //inventory.clear();

        Map<Character, ItemStack> item = new HashMap<>();
        item.put('_', ItemUtil.createItem(Material.BLACK_STAINED_GLASS_PANE, null, null));
        item.put('n', null);
        item.put('c', ItemUtil.createItem(Material.BOOK, null, null));
        item.put('r', ItemUtil.createItem(Material.ARROW, null, null));
        item.put('b', ItemUtil.createItem(Material.BARRIER, null, null));
        item.put('t', ItemUtil.createItem(Material.OAK_SIGN, null, null));
        item.put('e', ItemUtil.createItem(Material.ENDER_EYE, null, null));
        item.put('g', ItemUtil.createItem(LIGHT_BLUE_STAINED_GLASS_PANE, null, null));
        item.put('o', ItemUtil.createItem(ORANGE_STAINED_GLASS_PANE, null, null));


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

    private String getTitle() {
        int totalPages = (int) Math.ceil((double) enchantBooks.size() / itemsPage);
        return String.format("(%d/2)  Справочник зачарований", currentPage + 1);
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

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof GuideMenu)) return;

        final int slot = event.getRawSlot();

        if (slot < top.getSize()) {
            event.setCancelled(true);
        }

        Player player = (Player) event.getWhoClicked();

        switch (slot) {
            case 35 -> {
                if ((currentPage + 1) * itemsPage < enchantBooks.size()) {
                    currentPage++;
                    player.sendMessage("" + currentPage);
                    refresh(player);
                    player.openInventory(inventory);
                }
            }
            case 27 -> {
                if (currentPage > 0) {
                    currentPage--;
                    refresh(player);
                    player.openInventory(inventory);
                }
            }

            case 53 -> {
                MainMenu mainMenu = CustomEnchantingTable.instance.getMenu(player);
                player.openInventory(mainMenu.getInventory());
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void refresh(Player player) {
        Inventory newInv = Bukkit.createInventory(this, inventory.getSize(), getTitle());

        for (int i = 0; i < inventory.getSize(); i++) {
            newInv.setItem(i, inventory.getItem(i));
        }

        this.inventory = newInv;

        build();
        player.openInventory(newInv);
    }


    private void loadEnchantBooks() {
        enchantBooks.clear();

        for (ConfigData.GuideBookItem book : configData.getGuideBook().getBooks()) {
            String name = book.getName();
            List<String> loreList = book.getLore();

            List<Component> lore = loreList.stream()
                    .map(Component::text)
                    .collect(Collectors.toList());

            ItemStack enchantedBook = ItemUtil.createItem(
                    Material.ENCHANTED_BOOK,
                    Component.text(name),
                    lore
            );
             enchantBooks.add(enchantedBook);
        }

    }
}
