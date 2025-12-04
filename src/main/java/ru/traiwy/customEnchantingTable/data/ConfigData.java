package ru.traiwy.customEnchantingTable.data;


import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class ConfigData {
    @Data
    public static class GuideBook{
        public final List<ItemStack> books = List.of();
    }

    @Data
    public static class itemMenu{
        private final Component name;
        private final List<Component> lore;
    }
}
