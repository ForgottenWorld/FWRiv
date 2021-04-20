package me.architetto.fwriv.partecipant;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class PartecipantsManager {

    private static PartecipantsManager partecipantsManager;

    private HashMap<UUID, Partecipant> uuidPartecipantMap;
    private HashMap<UUID, PartecipantStats> partecipantStatsHashMap;


    private PartecipantsManager() {
        if(partecipantsManager != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        this.uuidPartecipantMap = new HashMap<>();
        this.partecipantStatsHashMap = new HashMap<>();

    }

    public static PartecipantsManager getInstance() {
        if(partecipantsManager == null) {
            partecipantsManager = new PartecipantsManager();
        }
        return partecipantsManager;
    }

    public boolean addPartecipant(Player player, Location loc, PartecipantStatus status) {
        partecipantStatsHashMap.putIfAbsent(player.getUniqueId(), new PartecipantStats());
        return uuidPartecipantMap.putIfAbsent(player.getUniqueId(), new Partecipant(player, loc, status)) == null;
    }

    public boolean removePartecipant(Player player) {
        return uuidPartecipantMap.remove(player.getUniqueId()) != null;
    }

    public boolean isPresent(Player player) {
        return uuidPartecipantMap.containsKey(player.getUniqueId());
    }

    public Optional<Partecipant> getPartecipant(Player player) {
        return uuidPartecipantMap.containsKey(player.getUniqueId()) ? Optional.of(uuidPartecipantMap.get(player.getUniqueId())) : Optional.empty();
    }

    public Optional<Partecipant> getPartecipant(UUID uuid) {
        return uuidPartecipantMap.containsKey(uuid) ? Optional.of(uuidPartecipantMap.get(uuid)) : Optional.empty();
    }

    public Optional<PartecipantStats> getPartecipantStats(Player player) {
        return partecipantStatsHashMap.containsKey(player.getUniqueId()) ?
                Optional.of(partecipantStatsHashMap.get(player.getUniqueId())) : Optional.empty();
    }

    public void clearPartecipantsStats() {
        this.partecipantStatsHashMap.clear();
    }

    public Set<UUID> getPartecipantsUUID(PartecipantStatus partecipantStatus) {
        return partecipantStatus.equals(PartecipantStatus.ALL) ? new HashSet<>(uuidPartecipantMap.keySet()) :
                uuidPartecipantMap.entrySet().stream()
                        .filter(entry -> entry.getValue().getPartecipantStatus().equals(partecipantStatus))
                        .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public void replacePartecipantsStatus(PartecipantStatus from, PartecipantStatus to) {
        this.uuidPartecipantMap.forEach((uuid, partecipant) -> {
            if (partecipant.getPartecipantStatus().equals(from))
                partecipant.setPartecipantStatus(to);
        });
    }



    /*


    public boolean addPartecipantReturnLocation(UUID uuid, Location location) {
        return partecipantsReturnLocation.putIfAbsent(uuid, new LightLocation(location)) == null;
    }

    public boolean removePartecipantReturnLocation(UUID uuid) {
        return partecipantsReturnLocation.remove(uuid) != null;
    }

    public Location getPartecipantReturnLocation(UUID uuid) {
        return partecipantsReturnLocation.containsKey(uuid) ? partecipantsReturnLocation.get(uuid).loc() : null;
    }

    public PartecipantStatus getPartecipantStatus(UUID uuid) {
        return partecipantsStatus.get(uuid);
    }

    public void removePartecipantStatus(UUID uuid) {
        partecipantsStatus.remove(uuid);
    }

    public void setPartecipantStatus(UUID uuid, PartecipantStatus partecipantStatus) {
        partecipantsStatus.put(uuid, partecipantStatus);
    }

    public boolean isPartecipant(UUID uuid) {
        return partecipantsStatus.containsKey(uuid);
    }

    public boolean isPartecipantInThisStatus(UUID uuid, PartecipantStatus partecipantStatus) {
        return partecipantsStatus.containsKey(uuid) && partecipantsStatus.get(uuid).equals(partecipantStatus);
    }

    public void replacePartecipantsStatus(PartecipantStatus from, PartecipantStatus to) {
        partecipantsStatus.replaceAll((uuid, status) ->
            status.equals(from) ? to : from
        );
    }

    public Set<UUID> getPartecipantsSet(PartecipantStatus partecipantStatus) {
        return partecipantStatus != PartecipantStatus.ALL ? partecipantsStatus.entrySet().stream()
                .filter(entry -> entry.getValue().equals(partecipantStatus))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()) : new HashSet<>(partecipantsStatus.keySet());

    }

     */

}
