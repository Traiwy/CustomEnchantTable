package ru.traiwy.customEnchantingTable.gui.inv.main;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.ENCHANTED_BOOK;

public class EnchantManager {
    public boolean canEnchantItem(@NotNull ItemStack item) {
        final Material material = item.getType();
        for (EnchantmentTarget target : EnchantmentTarget.values()) {
            if (target.includes(material)) return true;
        }
        return false;
    }

    public List<Enchantment> getAllPossibleEnchantment(@NotNull ItemStack item) {
        List<Enchantment> possibleEnchantments = new ArrayList<>();
        if (item.getType() == Material.AIR) return possibleEnchantments;

        Map<Enchantment, Integer> existingEnchants = new HashMap<>();
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            existingEnchants = item.getEnchantments();
        }

        for (Enchantment enchantment : Enchantment.values()) {
            if (!enchantment.canEnchantItem(item)) continue;

            if (existingEnchants.containsKey(enchantment)) continue;

            boolean hasConflict = false;
            for (Enchantment existing : existingEnchants.keySet()) {
                if (enchantment.conflictsWith(existing)) {
                    hasConflict = true;
                    break;
                }
            }
            if (hasConflict) continue;

            possibleEnchantments.add(enchantment);
        }

        return possibleEnchantments;
    }

    public ItemStack createEnchantmentBook(Enchantment enchantment) {
        final ItemStack item = new ItemStack(ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();

        meta.addStoredEnchant(enchantment, 1, true);
        item.setItemMeta(meta);
        return item;
    }

}
