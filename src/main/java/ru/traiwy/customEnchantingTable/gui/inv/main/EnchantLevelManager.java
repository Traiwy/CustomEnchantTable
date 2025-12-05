package ru.traiwy.customEnchantingTable.gui.inv.main;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bukkit.Material.GRAY_DYE;

public class EnchantLevelManager {
    private static int[] materialDye = {22, 23, 24, 31, 32, 33};
    private static int[] panelGray = {12,13,14,15,16,21,25,30,34};

    public List<Integer> getLevelsEnchant(@NotNull Enchantment enchantment) {
        final List<Integer> levels = new ArrayList<>();

        final int maxLevel = enchantment.getMaxLevel();
        final int startLevel = enchantment.getStartLevel();

        if (maxLevel == startLevel) {
            levels.add(maxLevel);
            return levels;
        }

        for (int level = startLevel; level <= maxLevel; level++) {
            levels.add(level);
        }

        return levels;
    }

    public ItemStack createDye(int level) {
        final ItemStack item = new ItemStack(GRAY_DYE);
        final ItemMeta meta = item.getItemMeta();
        String name = "Левел: " + level;
        meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;
    }

    public void updateDyeLevels(@NotNull ItemStack item, Inventory inv){
        if(item.getType() != Material.ENCHANTED_BOOK) return;
        final EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        if(meta == null) return;

        int index = 0;
        for (int slot : materialDye) {
            inv.setItem(slot, null);
        }
        for(int slot : panelGray){
            inv.setItem(slot, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }

        for(Map.Entry<Enchantment, Integer> enty : meta.getStoredEnchants().entrySet()){
            List<Integer> levels = getLevelsEnchant(enty.getKey());
            for(int i = 0; i < levels.size() && i < materialDye.length; i++){
                inv.setItem(materialDye[index++], createDye(levels.get(i)));
            }
        }
    }
}
