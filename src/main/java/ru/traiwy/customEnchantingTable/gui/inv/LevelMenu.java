package ru.traiwy.customEnchantingTable.gui.inv;

import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.CustomEnchantingTable;
import ru.traiwy.customEnchantingTable.data.ConfigData;
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;
import ru.traiwy.customEnchantingTable.manager.BuyEnchantManager;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.*;

@FieldDefaults(makeFinal = true)
public class LevelMenu implements MenuTable {


    public static final String[] inv = {
            "ggggggggg",
            "g_______g",
            "on__nnn_o",
            "ot__nnn_o",
            "g_______g",
            "gggbacggd"
    };

    Inventory inventory;
    ItemStack item;
    ItemStack bookItem;
    ConfigData configData;


    public LevelMenu(ItemStack item, ItemStack bookItem, ConfigData configData) {
        this.bookItem = bookItem;
        this.configData = configData;
        this.inventory = Bukkit.createInventory(this, 54, "Enchanting item -> Levels");
        this.item = item.clone();
        build();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    @Override
    public void build() {
        final Component textName = Component.text("name")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);

        final List<Component> lore = Component.text("lore")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true).children();


        Map<Character, ItemStack> items = new HashMap<>();
        items.put('_', ItemUtil.createItem(GRAY_STAINED_GLASS_PANE, null, null));
        items.put('n', new ItemStack(Material.AIR));
        items.put('t', ItemUtil.createItem(ENCHANTING_TABLE, textName, lore));
        items.put('b', ItemUtil.createItem(BOOKSHELF, textName, lore));
        items.put('a', ItemUtil.createItem(BARRIER, textName, lore));
        items.put('c', ItemUtil.createItem(BOOK, textName, lore));
        items.put('d', ItemUtil.createItem(ARROW, textName, lore));


        items.put('g', ItemUtil.createItem(LIGHT_BLUE_STAINED_GLASS_PANE, null, null));
        items.put('o', ItemUtil.createItem(ORANGE_STAINED_GLASS_PANE, null, null));

        int slot = 0;
        for (String row : inv) {
            for (char c : row.toCharArray()) {
                ItemStack itemStack = items.getOrDefault(c, null);
                inventory.setItem(slot, itemStack);
                slot++;
            }
            while (slot % 9 != 0) {
                slot++;
            }
        }


        inventory.setItem(MainMenu.ITEM_SLOT, item);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public void click(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory top = event.getView().getTopInventory();
        if (!(top.getHolder() instanceof MenuTable)) return;

        final int slot = event.getRawSlot();

        if (slot < top.getSize()) {
            if (slot == MainMenu.ITEM_SLOT) {
                event.setCancelled(false);
            } else {
                event.setCancelled(true);
                switch (slot) {
                    case 22 -> applyEnchantment(player, 1, safeGetCostExp(0, 0));
                    case 23 -> applyEnchantment(player, 2, safeGetCostExp(1, 1));
                    case 24 -> applyEnchantment(player, 3, safeGetCostExp(2, 2));
                    case 31 -> applyEnchantment(player, 4, safeGetCostExp(3, 3));
                    case 32 -> applyEnchantment(player, 5, safeGetCostExp(4, 4));
                    case 33 -> applyEnchantment(player, 6, safeGetCostExp(5, 5));
                }
            }


        }

    }

    private int safeGetCostExp(int enchantIndex, int levelIndex) {
        if (enchantIndex < 0 || enchantIndex >= configData.getEnchantments().size()) return 0;
        List<Integer> costExp = configData.getEnchantments().get(enchantIndex).getCostExp();
        if (levelIndex < 0 || levelIndex >= costExp.size()) return 0;
        return costExp.get(levelIndex);
    }

    private void applyEnchantment(Player player, int level, int costEnchant) {
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) bookItem.getItemMeta();
        if (bookMeta.getStoredEnchants().isEmpty()) return;

        Map.Entry<Enchantment, Integer> entry = bookMeta.getStoredEnchants().entrySet().iterator().next();
        Enchantment enchant = entry.getKey();

        BuyEnchantManager buyEnchantManager = new BuyEnchantManager();
        int exp = buyEnchantManager.getExp(player);
        if (exp < costEnchant) {
            player.sendMessage("У вас недостаточно опыта");
            return;
        }

        buyEnchantManager.removeExp(player, costEnchant);


        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);

        CustomEnchantingTable.instance.updateItem(player, item);

        MainMenu menu = CustomEnchantingTable.instance.getMenu(player);
        menu.open(player);

    }
}
