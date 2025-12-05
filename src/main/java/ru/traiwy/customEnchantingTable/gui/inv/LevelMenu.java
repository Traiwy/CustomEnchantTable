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
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.*;

@FieldDefaults(makeFinal = true)
public class LevelMenu implements MenuTable {


    public static final String[] inv = {
            "_________",
            "_________",
            "_n__nnn__",
            "_t__nnn__",
            "_________",
            "d__bac___"
    };

    Inventory inventory;
    ItemStack item;
    ItemStack bookItem;


    public LevelMenu(ItemStack item, ItemStack bookItem){
        this.bookItem = bookItem;
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
        items.put('_', ItemUtil.createItem(BLACK_STAINED_GLASS_PANE, null, null));
        items.put('n', new ItemStack(Material.AIR));
        items.put('t', ItemUtil.createItem(ENCHANTING_TABLE, textName, lore));
        items.put('b', ItemUtil.createItem(BOOKSHELF, textName, lore));
        items.put('a', ItemUtil.createItem(BARRIER, textName, lore));
        items.put('c', ItemUtil.createItem(BOOK, textName, lore));
        items.put('d', ItemUtil.createItem(ARROW, textName, lore));

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

    public void open(Player player){
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
                    case 45 -> CustomEnchantingTable.instance.getMenu(player).open(player);
                    case 22 -> applyEnchantment(player, 1);
                    case 23 -> applyEnchantment(player, 2);
                    case 24 -> applyEnchantment(player, 3);
                    case 31 -> applyEnchantment(player, 4);
                    case 32 -> applyEnchantment(player, 5);
                    case 33 -> applyEnchantment(player, 6);
                }
            }


        }

    }

    private void applyEnchantment(Player player, int level) {
        player.sendMessage("1");
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) bookItem.getItemMeta();
        if (bookMeta.getStoredEnchants().isEmpty()) return;

        Map.Entry<Enchantment, Integer> entry = bookMeta.getStoredEnchants().entrySet().iterator().next();
        Enchantment enchant = entry.getKey();

        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(enchant, level, true);
        item.setItemMeta(meta);

        CustomEnchantingTable.instance.updateItem(player, item);

        MainMenu menu = CustomEnchantingTable.instance.getMenu(player);
        menu.open(player);

    }
}
