package ru.traiwy.customEnchantingTable.data;


import lombok.Data;

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

        public GuideBook() {
            GuideBookItem item = new GuideBookItem();
            item.setName("Sharpness VI");
            item.setLore(List.of("Описание", "ещё строка"));
            books.add(item);
        }
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

    @Data
    public static class GuideBookItem {
        private String name;
        public List<String> lore;
    }

}
