package me.architetto.fwriv.obj;

import me.architetto.fwriv.arena.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RoundSpawn {
    private Location loc1,loc2,loc3,loc4;
    private int i = 0;

    public RoundSpawn(Arena arena) {
        this.loc1 = arena.getSpawn1();
        this.loc2 = arena.getSpawn2();
        this.loc3 = arena.getSpawn3();
        this.loc4 = arena.getSpawn4();
    }

    public void teleport(Player player) {
        switch (i) {
            case 0:
                player.teleport(loc1);
                this.i++;
                break;
            case 1:
                player.teleport(loc2);
                this.i++;
                break;
            case 2:
                player.teleport(loc3);
                this.i++;
                break;
            case 3:
                player.teleport(loc4);
                this.i = 0;
        }
    }



}
