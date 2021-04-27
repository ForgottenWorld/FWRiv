package me.architetto.fwriv.obj;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.UUID;

public class LightLocation {

    private final String worldName;
    private final UUID worldUUID;
    private final Vector vector;
    private final long chunkKey;

    public LightLocation(Location location) {
        this.vector = location.toVector();
        this.worldUUID = location.getWorld().getUID();
        this.worldName = location.getWorld().getName();
        this.chunkKey = location.getChunk().getChunkKey();
    }

    public Vector getVector() {
        return vector;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public String getWorldName() {
        return worldName;
    }

    public long getChunkKey() {
        return chunkKey;
    }

    public int getBlockY() {
        return vector.getBlockY();
    }

    public int getBlockX() {
        return vector.getBlockX();
    }

    public int getBlockZ() {
        return vector.getBlockZ();
    }

    public Location setBlockX(double x) {
        this.vector.setX(x);
        return this.vector.toLocation(Objects.requireNonNull(Bukkit.getWorld(worldUUID)));
    }

    public Location setBlockY(double y) {
        this.vector.setY(y);
        return this.vector.toLocation(Objects.requireNonNull(Bukkit.getWorld(worldUUID)));
    }

    public Location setBlockZ(double z) {
        this.vector.setZ(z);
        return this.vector.toLocation(Objects.requireNonNull(Bukkit.getWorld(worldUUID)));
    }

    public Location loc() {
        return vector.toLocation(Objects.requireNonNull(Bukkit.getWorld(worldUUID)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightLocation ll = (LightLocation) o;
        return Objects.equals(vector, ll.vector) &&
                Objects.equals(worldUUID, ll.worldUUID) &&
                Objects.equals(worldName, ll.worldName) &&
                Objects.equals(chunkKey, ll.chunkKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vector, worldUUID, worldName, chunkKey);
    }

}
