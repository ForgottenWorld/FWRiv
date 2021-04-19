package me.architetto.fwriv.partecipant;

import me.architetto.fwriv.obj.LightLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;


public class Partecipant {

    private final String name;
    private final UUID uuid;
    private final LightLocation returnLocation;

    private PartecipantStatus partecipantStatus;

    public Partecipant(Player player, Location returnLocation, PartecipantStatus partecipantStatus) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.returnLocation = new LightLocation(returnLocation);
        this.partecipantStatus = partecipantStatus;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getReturnLocation() {
        return returnLocation.loc();
    }

    public PartecipantStatus getPartecipantStatus() {
        return partecipantStatus;
    }

    public void setPartecipantStatus(PartecipantStatus partecipantStatus) {
        this.partecipantStatus = partecipantStatus;
    }


}
