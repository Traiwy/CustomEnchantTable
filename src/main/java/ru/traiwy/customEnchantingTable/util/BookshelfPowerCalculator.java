package ru.traiwy.customEnchantingTable.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BookshelfPowerCalculator {

    record Offset(int dx, int dy, int dz){}

    private static final Offset[] OFFSETS = {
            new Offset( 2, 0, 0),
            new Offset(-2, 0, 0),
            new Offset( 0, 0, 2),
            new Offset( 0, 0,-2),

            new Offset( 2, 0, 1),
            new Offset( 2, 0,-1),
            new Offset(-2, 0, 1),
            new Offset(-2, 0,-1),

            new Offset( 1, 0, 2),
            new Offset(-1, 0, 2),
            new Offset( 1, 0,-2),
            new Offset(-1, 0,-2),

            new Offset( 2, 1, 0),
            new Offset(-2, 1, 0),
            new Offset( 0, 1, 2),
            new Offset( 0, 1,-2),

            new Offset( 2, 1, 1),
            new Offset( 2, 1,-1),
            new Offset(-2, 1, 1),
            new Offset(-2, 1,-1),

            new Offset( 1, 1, 2),
            new Offset(-1, 1, 2),
            new Offset( 1, 1,-2),
            new Offset(-1, 1,-2),
    };


    public int calculatePower(Location tableLocation){
        int count = 0;
        for(Offset off : OFFSETS){
            Block bookshelf = tableLocation.getBlock().getRelative(off.dx, off.dy, off.dz);

            if(!isBookshelf(bookshelf)) continue;
            if(!isLineClear(tableLocation, off)) continue;

            count++;

        }
        return count;
    }

    private boolean isBookshelf(Block block){
       return block.getType() == Material.BOOKSHELF;
    }

    private boolean isLineClear(Location table, Offset off){
        Block airCheck = table.getBlock().getRelative(
                off.dx/2,
                off.dy,
                off.dz/2
        );

        return airCheck.getType() == Material.AIR;
    }
}
