package me.architetto.fwriv.partecipant;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.localization.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        partecipantStatsHashMap.putIfAbsent(player.getUniqueId(), new PartecipantStats(player));
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

    public void resetPartecipantsStats() {
        this.partecipantStatsHashMap.values().forEach(PartecipantStats::reset);
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

    public void printStats() {

        new BukkitRunnable() {
            private int i = 0;
            private Set<Player> partecipants = uuidPartecipantMap.keySet().stream()
                    .map(Bukkit::getPlayer).collect(Collectors.toSet());
            private Set<PartecipantStats> stats = new HashSet<>(partecipantStatsHashMap.values());
            @Override
            public void run() {
                switch (i) {
                    case 0:
                        stats.stream()
                                .collect(Collectors.toMap(PartecipantStats::getPlayerName,PartecipantStats::getKills))
                                .entrySet().stream()
                                .max((entry1,entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                                .ifPresent(entry -> partecipants.stream()
                                        .filter(Player::isOnline)
                                        .forEach(p -> p.sendMessage(Message.STATS_KILLS.asString(entry.getKey(),entry.getValue()))));
                        i++;
                        break;
                    case 1:
                        stats.stream()
                                .collect(Collectors.toMap(PartecipantStats::getPlayerName,PartecipantStats::getDamageDealt))
                                .entrySet().stream()
                                .max((entry1,entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                                .ifPresent(entry -> partecipants.stream()
                                        .filter(Player::isOnline)
                                        .forEach(p -> p.sendMessage(Message.STATS_DAMAGEDONE.asString(entry.getKey(),entry.getValue()))));
                        i++;
                        break;
                    case 2:
                        stats.stream()
                                .collect(Collectors.toMap(PartecipantStats::getPlayerName,PartecipantStats::getDamageTaken))
                                .entrySet().stream()
                                .max((entry1,entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                                .ifPresent(entry -> partecipants.stream()
                                        .filter(Player::isOnline)
                                        .forEach(p -> p.sendMessage(Message.STATS_DAMAGETAKEN.asString(entry.getKey(),entry.getValue()))));
                        i++;
                        break;
                    case 3:
                        stats.stream()
                                .collect(Collectors.toMap(PartecipantStats::getPlayerName,PartecipantStats::getTowerRewards))
                                .entrySet().stream()
                                .max((entry1,entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                                .ifPresent(entry -> partecipants.stream()
                                        .filter(Player::isOnline)
                                        .forEach(p -> p.sendMessage(Message.STATS_TOWERREWARDS.asString(entry.getKey(),entry.getValue()))));
                        i++;
                        break;
                    case 4:
                        stats.stream()
                                .collect(Collectors.toMap(PartecipantStats::getPlayerName,PartecipantStats::getTargetBlockReward))
                                .entrySet().stream()
                                .max((entry1,entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                                .ifPresent(entry -> partecipants.stream()
                                        .filter(Player::isOnline)
                                        .forEach(p -> p.sendMessage(Message.STATS_TARGETREWARDS.asString(entry.getKey(),entry.getValue()))));
                        resetPartecipantsStats();
                        this.cancel();
                        break;
                }
            }
        }.runTaskTimer(FWRiv.getPlugin(FWRiv.class),20L,40);
    }


}
