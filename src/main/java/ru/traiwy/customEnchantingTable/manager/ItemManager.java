package ru.traiwy.customEnchantingTable.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemManager {

    public ItemStack createItem(){
        final ItemStack itemStack = new ItemStack(Material.ENCHANTING_TABLE);
        final ItemMeta meta = itemStack.getItemMeta();

        final Component textName = Component.text("Custom enchanting Table")
                .decoration(TextDecoration.ITALIC, false)
                .decoration(TextDecoration.BOLD, true);

        final List<Component> textLore = Component.text("lore")
                .decoration(TextDecoration.ITALIC, false).children();

        meta.displayName(textName);
        meta.lore(textLore);

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
