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
    private Location spectator;

    public Arena(String arenaName, Location Spawn1, Location Spawn2, Location spawn3,
                 Location spawn4, Location towerTopLoc, Location spectatorspawnloc) {
        this.arenaName = arenaName;
        this.spawn1 = Spawn1;
        this.spawn2 = Spawn2;
        this.spawn3 = spawn3;
        this.spawn4 = spawn4;
        this.tower = towerTopLoc;
        this.spectator = spectatorspawnloc;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public Location getSpawn3() {
        return spawn3;
    }

    public void setSpawn3(Location spawn3) {
        this.spawn3 = spawn3;
    }

    public Location getSpawn4() {
        return spawn4;
    }

    public void setSpawn4(Location spawn4) {
        this.spawn4 = spawn4;
    }

    public Location getTower() {
        return tower;
    }

    public void setTower(Location tower) {
        this.tower = tower;
    }

    public Location getSpectator() {
        return spectator;
    }

    public void setSpectator(Location spectator) {
        this.spectator = spectator;
    }

    public String getName() {
        return this.arenaName;
    }

    public void setName(String name) {
        this.arenaName = name;
    }

    public List<Location> getAllSpawnLocations() {

        return new ArrayList<>(
                Arrays.asList(spawn1,
                        spawn2,
                        spawn3,
                        spawn4));

    }

    public int getLowestY() {

        int y = Math.min(1000, spawn1.getBlockY());
        y = Math.min(y, spawn2.getBlockY());
        y = Math.min(y, spawn3.getBlockY());
        y = Math.min(y, spawn4.getBlockY());

        return y;

    }

    public List<Block> getSpawnDoors() {

        List<Block> doorsList = new ArrayList<>();

        for (Location loc : getAllSpawnLocations()) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return Objects.equals(spawn1, arena.spawn1) &&
                Objects.equals(spawn2, arena.spawn2) &&
                Objects.equals(spawn3, arena.spawn3) &&
                Objects.equals(spawn4, arena.spawn4) &&
                Objects.equals(tower, arena.tower) &&
                Objects.equals(spectator, arena.spectator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spawn1, spawn2, spawn3, spawn4, tower, spectator);
    }

}
