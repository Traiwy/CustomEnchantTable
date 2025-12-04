package ru.traiwy.customEnchantingTable.gui.inv;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.CustomEnchantingTable;
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.util.BookshelfPowerCalculator;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.ArrayList;
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

    private int currentLevel;
    private int bookshelfCount;


    public MainMenu(JavaPlugin plugin){
        this.plugin = plugin;
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
        item.put('_', ItemUtil.createItem(BLACK_STAINED_GLASS_PANE, null, null ));
        item.put('n', null);
        item.put('d', ItemUtil.createItem(GRAY_DYE, textName, lore));
        item.put('t', ItemUtil.createItem(ENCHANTING_TABLE, textName, lore));
        item.put('b', ItemUtil.createItem(BOOKSHELF, textName, loreBookShelf));
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


    public void open(Player player, int levelTable, int countBookShelf) {
        this.currentLevel = levelTable;
        this.bookshelfCount = countBookShelf;
        build();
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


        if (inventoryClick != null) {
            player.openInventory(inventoryClick);
            return;
        } else {
            player.sendMessage("Инвентори null");
        }


        Bukkit.getScheduler().runTask(plugin, () -> {
            final ItemStack item = inv.getItem(BOOK_SLOT);

            if (item == null || item.getType() == Material.AIR) {
                for (int slot : materialAir) {
                    inv.setItem(slot, null);
                }
                build();
                return;
            }

            List<Enchantment> enchant = getAllPossibleEnchantment(item);
            if (enchant.isEmpty()) return;

            player.sendMessage("Предмет: " + item);
            player.sendMessage("1");
            if (!canEnchantItem(item)) return;

            for (int slot : materialAir) {
                inv.setItem(slot, null);
            }
            for (int i = 0; i < enchant.size() && i < materialAir.length; i++) {
                Enchantment enchantment = enchant.get(i);
                int slot = materialAir[i];

                ItemStack book = createEnchantmentBook(enchantment);
                inv.setItem(slot, book);
            }


        });
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public Inventory getClickInventory(ItemStack itemStack) {
        if(itemStack == null) return null;
        switch (itemStack.getType()) {
            case BOOK:
                return CustomEnchantingTable.getInstance().getGuideMenu().getInventory();
            default:
                return null;
        }
    }

    private boolean canEnchantItem(ItemStack item){
        if(item == null) return false;
        final Material material = item.getType();
        for(EnchantmentTarget target : EnchantmentTarget.values()){
            if(target.includes(material)) return true;
        }
        return false;
    }

    private List<Enchantment> getAllPossibleEnchantment(ItemStack item){
        List<Enchantment> possibleEnchantments  = new ArrayList<>();
        if(item == null || item.getType() == Material.AIR) return possibleEnchantments;

        Map<Enchantment, Integer> existingEnchants = new HashMap<>();
         if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            existingEnchants = item.getEnchantments();
        }

        for(Enchantment enchantment : Enchantment.values()){
            if(!enchantment.canEnchantItem(item)) continue;

            if(existingEnchants.containsKey(enchantment)) continue;

            boolean hasConflict = false;
            for(Enchantment existing : existingEnchants.keySet()){
                if(enchantment.conflictsWith(existing)){
                    hasConflict = true;
                    break;
                }
            }
            if(hasConflict) continue;

            possibleEnchantments.add(enchantment);
        }

        return possibleEnchantments;
    }

    private ItemStack createEnchantmentBook(Enchantment enchantment){
        final ItemStack item = new ItemStack(ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

        meta.addStoredEnchant(enchantment, 1, true);
        item.setItemMeta(meta);
        return item;
    }
}

