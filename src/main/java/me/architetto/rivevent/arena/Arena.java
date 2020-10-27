package me.architetto.rivevent.arena;

import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Arena {

    private String arenaName;

    private Location spawn1;
    private Location spawn2;
    private Location spawn3;
    private Location spawn4;
    private Location tower;

    public Arena(String arenaName, Location Spawn1, Location Spawn2, Location spawn3,
                 Location spawn4, Location towerTopLoc) {
        this.arenaName = arenaName;
        this.spawn1 = Spawn1;
        this.spawn2 = Spawn2;
        this.spawn3 = spawn3;
        this.spawn4 = spawn4;
        this.tower = towerTopLoc;
    }

    public String getName() {
        return this.arenaName;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public Location getSpawn3() {
        return spawn3;
    }

    public Location getSpawn4() {
        return spawn4;
    }

    public Location getTower() {
        return tower;
    }


    public List<Location> getSpawnLocations() {

        return new ArrayList<>(
                Arrays.asList(spawn1,
                        spawn2,
                        spawn3,
                        spawn4));

    }

    public List<Block> getSpawnDoors() {

        List<Block> doorsList = new ArrayList<>();

        for (Location loc : getSpawnLocations()) {
            Block middle = loc.getBlock();
            for (int x = 10; x >= -10; x--) {
                for (int y = 10; y >= -10; y--) {
                    for(int z = 10; z >= -10; z--) {
                        if (Tag.DOORS.getValues().contains(middle.getRelative(x, y, z).getType())
                                ||Tag.FENCE_GATES.getValues().contains(middle.getRelative(x, y, z).getType())) {

                            doorsList.add(middle.getRelative(x, y, z).getLocation().getBlock());

                        }
                    }
                }
            }
        }

        return doorsList;
    }


    public int getLowestY() {

        int y = Math.min(1000, spawn1.getBlockY());
        y = Math.min(y, spawn2.getBlockY());
        y = Math.min(y, spawn3.getBlockY());
        y = Math.min(y, spawn4.getBlockY());

        return y;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return Objects.equals(spawn1, arena.spawn1) &&
                Objects.equals(spawn2, arena.spawn2) &&
                Objects.equals(spawn3, arena.spawn3) &&
                Objects.equals(spawn4, arena.spawn4) &&
                Objects.equals(tower, arena.tower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spawn1, spawn2, spawn3, spawn4, tower);
    }

}
