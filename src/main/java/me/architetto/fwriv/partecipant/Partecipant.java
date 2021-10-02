package me.architetto.fwriv.partecipant;

import me.architetto.fwriv.obj.LightLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;


public class Partecipant {

    private final String name;
    private final UUID uuid;
    private final LightLocation returnLocation;
    private final ItemStack[] inventory;

    private PartecipantStatus partecipantStatus;

    public Partecipant(Player player, Location returnLocation, PartecipantStatus partecipantStatus) {
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.returnLocation = new LightLocation(returnLocation);
        this.partecipantStatus = partecipantStatus;
        this.inventory = player.getInventory().getContents();
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public UUID getUUID() {
        return uuid;
    }

    public Location getReturnLocation() {
        return returnLocation.loc();
    }

    public PartecipantStatus getPartecipantStatus() {
        return partecipantStatus;
    }

    public ItemStack[] getInventory() {
        return this.inventory;
    }

    public void setPartecipantStatus(PartecipantStatus partecipantStatus) {
        this.partecipantStatus = partecipantStatus;
    }


}
