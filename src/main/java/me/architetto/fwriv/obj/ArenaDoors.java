package me.architetto.fwriv.obj;

import me.architetto.fwriv.arena.Arena;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;

import java.util.List;

public class ArenaDoors {
    private List<Block> blockList;

    public ArenaDoors(Arena arena) {
        blockList = arena.getSpawnDoors();
    }

    public void open() {

        blockList.forEach(block -> {
            if (!Tag.DOORS.getValues().contains(block.getType())
                    && !Tag.FENCE_GATES.getValues().contains(block.getType()))
                return;

            Openable openable = (Openable) block.getBlockData();
            openable.setOpen(true);
            block.setBlockData(openable, true);


        });

    }

    public void close() {

        blockList.forEach(block -> {
            if (!Tag.DOORS.getValues().contains(block.getType())
                    && !Tag.FENCE_GATES.getValues().contains(block.getType()))
                return;

            Openable openable = (Openable) block.getBlockData();
            openable.setOpen(false);
            block.setBlockData(openable, true);


        });

    }

}
