package ru.traiwy.customEnchantingTable.gui.inv;

import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.CustomEnchantingTable;
import ru.traiwy.customEnchantingTable.gui.MenuTable;
import ru.traiwy.customEnchantingTable.gui.inv.main.EnchantLevelManager;
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



    public LevelMenu(ItemStack item){
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
    public void click(InventoryClickEvent event){
        if(!(event.getWhoClicked() instanceof Player player)) return;

        final Inventory top = event.getView().getTopInventory();
        if(!(top.getHolder() instanceof MenuTable)) return;

         final int slot = event.getRawSlot();



         if(slot < top.getSize()){
             if(slot == MainMenu.ITEM_SLOT){
                 event.setCancelled(false);
             }

             if(slot == 45){
                MainMenu mainMenu = CustomEnchantingTable.instance.getMenu(player);
                mainMenu.open(player);
             }
         }
    }
}
