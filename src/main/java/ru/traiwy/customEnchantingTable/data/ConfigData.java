package ru.traiwy.customEnchantingTable.data;


import lombok.Data;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConfigData {
    private GuideBook guideBook = new GuideBook();
    private MenuItem itemMenu = new MenuItem();
    private List<EnchantData> enchantments = new ArrayList<>();

    @Data
    public static class GuideBook {
        public List<GuideBookItem> books = new ArrayList<>();
    }

    @Data
    public static class GuideBookItem {
        private String name;
        public List<String> lore;
    }

    @Data
    public static class MenuItem {
        private String name = "Название предмета";
        private List<EnchantData> lore = List.of();
    }

    @Data
    public static class EnchantData {
        private String name = "sharpness";
        private List<Integer> levels = new ArrayList<>();
        private List<Integer> costExp = new ArrayList<>();
        private List<Integer> requiredTableLevel = new ArrayList<>();
    }

}
