package ru.traiwy.customEnchantingTable.command;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.traiwy.customEnchantingTable.manager.ItemManager;
import ru.traiwy.customEnchantingTable.util.ItemUtil;

import java.util.List;

@AllArgsConstructor
public class GiveCommand implements CommandExecutor {
    private final ItemManager itemManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.getInventory().addItem(itemManager.createItem());
        return true;
    }
}
