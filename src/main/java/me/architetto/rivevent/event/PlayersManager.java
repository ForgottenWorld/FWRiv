package me.architetto.rivevent.event;

import me.architetto.rivevent.config.SettingsHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayersManager {

    private static PlayersManager playersManager;

    private HashMap<UUID, Vector> playersReturnVector;
    private HashMap<UUID, String> playersReturnWorldName;

    private List<UUID> activePlayers;
    private List<UUID> spectatorPlayers;

    private PlayersManager() {
        if(playersManager != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.playersReturnVector = new HashMap<>();
        this.playersReturnWorldName = new HashMap<>();

        this.activePlayers = new ArrayList<>();
        this.spectatorPlayers = new ArrayList<>();

    }

    public static PlayersManager getInstance() {
        if(playersManager == null) {
            playersManager = new PlayersManager();
        }
        return playersManager;
    }

    public List<UUID> getActivePlayers() {
        return new ArrayList<>(activePlayers);
    }

    public void addActivePlayer(UUID uuid) {
        activePlayers.add(uuid);
    }

    public void addActivePlayer(List<UUID> uuidList) {
        activePlayers.addAll(uuidList);
    }

    public void removeActivePlayer(UUID uuid) {
        activePlayers.remove(uuid);
    }

    public void removeActivePlayer() {
        activePlayers.clear();
    }

    public boolean isPlayerActive(UUID uuid) {
        return activePlayers.contains(uuid);
    }

    public List<UUID> getSpectatorPlayers() {
        return new ArrayList<>(spectatorPlayers);
    }

    public void addSpectatorPlayer(UUID uuid) {
        spectatorPlayers.add(uuid);
    }

    public void removeSpectatorPlayer() {
        spectatorPlayers.clear();
    }

    public void removeSpectatorPlayer(UUID uuid) {
        spectatorPlayers.remove(uuid);
    }

    public boolean isPlayerSpectator(UUID uuid) {
        return spectatorPlayers.contains(uuid);
    }

    public List<UUID> getPartecipants() {
        List<UUID> returnList = new ArrayList<>(activePlayers);
        returnList.addAll(spectatorPlayers);
        return returnList;
    }
    
    public boolean isPartecipants(UUID uuid) {
        return getPartecipants().contains(uuid);
    }

    public Location getReturnLocation(UUID uuid) {

        Location tpLocation = playersReturnVector.get(uuid).toLocation(Bukkit.getWorld(playersReturnWorldName.get(uuid))); //può dare qualche problema ?

        if (!tpLocation.getBlock().isPassable() || !tpLocation.getBlock().getRelative(BlockFace.UP).isPassable())
            return SettingsHandler.getInstance().safeRespawnLocation;
        else
            return tpLocation;
    }

    public void addReturnLocation(UUID playeruuid, Vector vector, String worldname) {
        playersReturnVector.put(playeruuid, vector);
        playersReturnWorldName.put(playeruuid, worldname);
    }

    public void removeReturnLocation(UUID uuid) {
        playersReturnVector.remove(uuid);
        playersReturnWorldName.remove(uuid);
    }

}
