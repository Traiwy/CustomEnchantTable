package ru.traiwy.customEnchantingTable.data;


import lombok.Data;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigData {
    private GuideBook guideBook = new GuideBook();
    private MenuItem itemMenu = new MenuItem();
    private List<EnchantData> enchantments = new ArrayList<>();

    @Data
    public static class GuideBook {
        public List<String> books = new ArrayList<>();
    }

    @Data
    public static class MenuItem {
        private String name = "Название предмета";
        private List<String> lore = List.of("Описание 1", "Описание 2");
    }

    @Data
    public static class EnchantData {
        private String name = "sharpness";
        private List<Integer> levels = List.of(1, 2, 3);
        private int costExp = 5;
    }
}
