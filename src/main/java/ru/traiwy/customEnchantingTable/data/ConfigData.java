package ru.traiwy.customEnchantingTable.data;


import lombok.Data;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

@Data
public class ConfigData {
    @Data
    public static class BOOk{
        public List<Enchantment> books = List.of(
            Enchantment.BINDING_CURSE,
            Enchantment.DEPTH_STRIDER,
            Enchantment.DAMAGE_ALL,
            Enchantment.DAMAGE_ARTHROPODS,
            Enchantment.DAMAGE_UNDEAD,
            Enchantment.FIRE_ASPECT,
            Enchantment.FROST_WALKER,
            Enchantment.IMPALING,
            Enchantment.KNOCKBACK,
            Enchantment.LOOT_BONUS_BLOCKS,
            Enchantment.LOOT_BONUS_MOBS,
            Enchantment.LOYALTY,
            Enchantment.LUCK,
            Enchantment.LURE,
            Enchantment.MENDING,
            Enchantment.MULTISHOT,
            Enchantment.PIERCING,
            Enchantment.PROTECTION_ENVIRONMENTAL,
            Enchantment.PROTECTION_EXPLOSIONS,
            Enchantment.PROTECTION_FALL,
            Enchantment.PROTECTION_PROJECTILE,
            Enchantment.QUICK_CHARGE,
            Enchantment.RIPTIDE,
            Enchantment.SILK_TOUCH,
            Enchantment.SWEEPING_EDGE,
            Enchantment.THORNS
        );
    }
}
