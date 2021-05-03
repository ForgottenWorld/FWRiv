package me.architetto.fwriv.arena;

import me.architetto.fwriv.obj.LightLocation;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Arena {

    private final String arenaName;

    private final LightLocation spawn1;
    private final LightLocation spawn2;
    private final LightLocation spawn3;
    private final LightLocation spawn4;
    private final LightLocation tower;

    public Arena(String arenaName, Location spawn1, Location spawn2, Location spawn3,
                 Location spawn4, Location tower) {

        this.arenaName = arenaName;
        this.spawn1 = new LightLocation(spawn1);
        this.spawn2 = new LightLocation(spawn2);
        this.spawn3 = new LightLocation(spawn3);
        this.spawn4 = new LightLocation(spawn4);
        this.tower = new LightLocation(tower);

    }

    public String getName() {
        return this.arenaName;
    }

    public Location getSpawn1() {
        return spawn1.loc();
    }

    public Location getSpawn2() {
        return spawn2.loc();
    }

    public Location getSpawn3() {
        return spawn3.loc();
    }

    public Location getSpawn4() {
        return spawn4.loc();
    }

    public Location getTower() {
        return tower.loc();
    }

    public List<Block> getSpawnDoors() {

        List<Block> doorsList = new ArrayList<>();
        List<Location> spawnLoc = new ArrayList<>();
        spawnLoc.add(spawn1.loc());
        spawnLoc.add(spawn2.loc());
        spawnLoc.add(spawn3.loc());
        spawnLoc.add(spawn4.loc());


        for (Location loc : spawnLoc) {
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

    public Location getCenteredLowestLocation() {
        Location loc = this.tower.loc();
        loc.setY(Math.min(Math.min(spawn1.getVector().getBlockY(),spawn2.getVector().getBlockY()),
                Math.min(spawn3.getVector().getBlockY(),spawn4.getVector().getBlockY())));
        return loc;
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
