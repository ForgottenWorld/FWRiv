package me.architetto.rivevent.event;

import me.architetto.rivevent.config.SettingsHandler;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayersManager {

    private static PlayersManager playersManager;

    private HashMap<UUID, Location> playersLocation;

    private List<UUID> activePlayers;
    private List<UUID> deathPlayers;

    private PlayersManager() {
        if(playersManager != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.playersLocation = new HashMap<>();

        this.activePlayers = new ArrayList<>();
        this.deathPlayers = new ArrayList<>();

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

    public List<UUID> getDeathPlayers() {
        return new ArrayList<>(deathPlayers);
    }

    public void addDeathPlayer(UUID uuid) {
        deathPlayers.add(uuid);
    }

    public void removeDeathPlayer() {
        deathPlayers.clear();
    }

    public void removeDeathPlayer(UUID uuid) {
        deathPlayers.remove(uuid);
    }

    public boolean isPlayerDeath(UUID uuid) {
        return deathPlayers.contains(uuid);
    }

    public List<UUID> getAllEventPlayers() {
        List<UUID> returnList = new ArrayList<>(activePlayers);
        returnList.addAll(deathPlayers);
        return returnList;
    }
    
    public boolean isInsideEvent(UUID uuid) {
        return getAllEventPlayers().contains(uuid);
    }

    public Location getPlayerLocation(UUID uuid) {
        Location tpLocation = playersLocation.get(uuid);

        if (tpLocation == null)
            return SettingsHandler.getInstance().safeRespawnLocation;
        else if (!tpLocation.getBlock().isPassable() || !tpLocation.getBlock().getRelative(BlockFace.UP).isPassable())
            return SettingsHandler.getInstance().safeRespawnLocation;
        else
            return playersLocation.get(uuid);
    }

    public void addPlayerLocation(UUID uuid, Location location) {
        playersLocation.put(uuid, location);
    }

    public void removePlayerLocation(UUID uuid) {
        playersLocation.remove(uuid);
    }

    public void clearPlayerInformation(UUID uuid) {
        this.playersLocation.remove(uuid);
    }



}
