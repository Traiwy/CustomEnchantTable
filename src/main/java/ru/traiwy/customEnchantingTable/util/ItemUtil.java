package ru.traiwy.customEnchantingTable.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@UtilityClass
public class ItemUtil {
    public static ItemStack createItem(Material material, Component name, List<Component> lore){
        final ItemStack itemStack = new ItemStack(material);
        final ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(name);
        meta.lore(lore);

        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
