package ru.traiwy.customEnchantingTable.manager;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BuyEnchantManager {

    public void setExp(Player player, int level){
        if(level < 0) return;
        player.setLevel(level);
    }

    public int getExp(@NotNull Player player){
        return player.getLevel();
    }

    public void removeExp(@NotNull Player player, int level) {
        int totalExp = player.getLevel();
        int newTotalExp = totalExp - level;

        if(newTotalExp < 0){
            player.sendMessage("У вас недостаточно опыта");
            return;
        }

        player.setLevel(newTotalExp);
    }
}
