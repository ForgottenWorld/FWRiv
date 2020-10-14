package me.architetto.rivevent.arena;

import me.architetto.rivevent.config.ConfigManager;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class ArenaManager {

    private final int SPAWN1 = 1;
    private final int SPAWN2 = 2;
    private final int SPAWN3 = 3;
    private final int SPAWN4 = 4;
    private final int TOWER = 5;
    private final int SPECTATOR = 6;

    private HashMap<String, Arena> arenaContainer;
    private static ArenaManager arenaManager;
    private HashMap<UUID, Integer> playerArenaCreation;
    private HashMap<UUID, String> playerArenaNameCreation;
    private HashMap<UUID, HashMap<Integer, Location>> playerArenaCoordinates;


    private ArenaManager() {
        if(arenaManager != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        this.arenaContainer = new HashMap<>();
        this.playerArenaCreation = new HashMap<>();
        this.playerArenaCoordinates = new HashMap<>();
        this.playerArenaNameCreation = new HashMap<>();

    }

    public static ArenaManager getInstance() {
        if(arenaManager == null) {
            arenaManager = new ArenaManager();
        }
        return arenaManager;
    }

    public boolean newArena(String presetName, Location firstSpawnLoc, Location secondSpawnLoc, Location thirdSpawnLoc,
                            Location fourthSpawnLoc, Location towerTopLoc, Location spectatorSpawnLoc) {

        if(arenaContainer.containsKey(presetName)) {
            return false;
        }

        Arena arena = new Arena(presetName, firstSpawnLoc, secondSpawnLoc, thirdSpawnLoc, fourthSpawnLoc, towerTopLoc, spectatorSpawnLoc);

        arenaContainer.put(presetName, arena);

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.addLocation(ConfigManager.getInstance().getConfig("Preset.yml"), firstSpawnLoc ,presetName + ".SPAWN1");
        configManager.addLocation(ConfigManager.getInstance().getConfig("Preset.yml"), secondSpawnLoc ,presetName + ".SPAWN2");
        configManager.addLocation(ConfigManager.getInstance().getConfig("Preset.yml"), thirdSpawnLoc ,presetName + ".SPAWN3");
        configManager.addLocation(ConfigManager.getInstance().getConfig("Preset.yml"), fourthSpawnLoc ,presetName + ".SPAWN4");
        configManager.addLocation(ConfigManager.getInstance().getConfig("Preset.yml"), towerTopLoc ,presetName + ".TOWER");
        configManager.addLocation(ConfigManager.getInstance().getConfig("Preset.yml"), spectatorSpawnLoc ,presetName + ".SPECTATOR");

        return true;
    }

    public Optional<Arena> getArena(String name) {
        return arenaContainer.containsKey(name) ? Optional.of(arenaContainer.get(name)) : Optional.empty();
    }

    public void arenaCreationHandler(Player sender, Location location) {
        if(!playerArenaCreation.containsKey(sender.getUniqueId())) {
            return;
        }

        int senderStep = playerArenaCreation.get(sender.getUniqueId());
        switch(senderStep) {
            case SPAWN1:
                playerArenaCoordinates.put(sender.getUniqueId(), new HashMap<>());
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN1, location.add(0,1,0));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("FIRST SPAWN selected..."));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("... Select SECOND SPAWN by right clicking on the block"));
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                break;
            case SPAWN2:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN2, location.add(0,1,0));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("SECOND SPAWN selected..."));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("Select THIRD SPAWN by right clicking on the block"));
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                break;
            case SPAWN3:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN3, location.add(0,1,0));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("THIRD SPAWN selected..."));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("Select FOURTH SPAWN by right clicking on the block"));
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                break;
            case SPAWN4:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN4, location.add(0,1,0));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("FOURTH SPAWN selected..."));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("Select TOWER LOCATION by right clicking on the block"));
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                break;
            case TOWER:
                playerArenaCoordinates.get(sender.getUniqueId()).put(TOWER, location.add(0,1,0));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("TOWER LOCATION selected..."));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("Select SPECTATOR SPAWN by right clicking on the block"));
                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                spawnEffectAtBlock(location);
                break;
            case SPECTATOR:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPECTATOR, location.add(0,1,0));
                sender.sendMessage(ChatFormatter.formatSuccessMessage("SPECTATOR SPAWN selected..."));
                sender.sendMessage(ChatFormatter.formatSuccessMessage(" '" + playerArenaNameCreation.get(sender.getUniqueId()) + "'" + " arena creation completed!"));
                location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP,1,1);
                spawnEffectAtBlock(location);

                Location firstSpawnLoc = this.playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN1);
                Location secondSpawnLoc = this.playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN2);
                Location thirdSpawnLoc = this.playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN3);
                Location fourthSpawnLoc = this.playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN4);
                Location towerTopLoc = this.playerArenaCoordinates.get(sender.getUniqueId()).get(TOWER);
                Location spectatorSpawnLoc = this.playerArenaCoordinates.get(sender.getUniqueId()).get(SPECTATOR);

                newArena(this.playerArenaNameCreation.get(sender.getUniqueId()), firstSpawnLoc, secondSpawnLoc, thirdSpawnLoc,
                        fourthSpawnLoc, towerTopLoc, spectatorSpawnLoc);

                this.playerArenaNameCreation.remove(sender.getUniqueId());
                this.playerArenaCoordinates.remove(sender.getUniqueId());
                this.playerArenaCreation.remove(sender.getUniqueId());

                break;
        }
        playerArenaCreation.put(sender.getUniqueId(), senderStep+1);
    }

    public void addPlayerToArenaCreation(Player player, String arenaName) {
        this.playerArenaCreation.put(player.getUniqueId(), SPAWN1);
        this.playerArenaNameCreation.put(player.getUniqueId(), arenaName);
    }

    public boolean isPlayerInCreationMode(Player player) {
        return this.playerArenaCreation.containsKey(player.getUniqueId());
    }

    public HashMap<String, Arena> getArenaContainer() {
        return this.arenaContainer;
    }

    public void spawnEffectAtBlock(Location loc) {

        loc.add(0.5,0.5,0.5);
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 5);
        loc.getWorld().spawnParticle(Particle.REDSTONE,loc,10,dustOptions);

    }

    public void removeArena(String presetName) {

        arenaContainer.remove(presetName);
        ConfigManager.getInstance().setData(ConfigManager.getInstance().getConfig("Preset.yml"), presetName, null );

    }

}





