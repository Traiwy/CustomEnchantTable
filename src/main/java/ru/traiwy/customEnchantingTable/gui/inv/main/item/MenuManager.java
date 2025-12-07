package ru.traiwy.customEnchantingTable.gui.inv.main.item;

import org.bukkit.entity.Player;
import ru.traiwy.customEnchantingTable.gui.inv.main.MainMenu;

import java.util.*;

public class MenuManager {
    private final Map<UUID, MainMenu> menus = new HashMap<>();
    private final Set<UUID> switchingMenus = new HashSet<>();

    public MainMenu getMenu(Player player) {
        return menus.get(player.getUniqueId());
    }
    public void setMenu(Player player, MainMenu menu){
          menus.put(player.getUniqueId(), menu);
    }

    public void setMark(Player player){
        switchingMenus.add(player.getUniqueId());
    }

    public boolean isSwitching(Player player){
        return switchingMenus.remove(player.getUniqueId());
    }

    public void unmarkSwitching(Player player){
        switchingMenus.remove(player.getUniqueId());
    }

    public void removeMenu(Player player){
        menus.remove(player.getUniqueId());
    }


}
