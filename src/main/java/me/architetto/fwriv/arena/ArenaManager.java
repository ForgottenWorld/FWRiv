package me.architetto.fwriv.arena;

import me.architetto.fwriv.FWRiv;
import me.architetto.fwriv.config.ConfigManager;
import me.architetto.fwriv.localization.Message;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaManager {
    private final int SPAWN1 = 1;
    private final int SPAWN2 = 2;
    private final int SPAWN3 = 3;
    private final int SPAWN4 = 4;
    private final int TOWER = 5;

    private static ArenaManager arenaManager;

    private HashMap<String, Arena> arenaContainer;

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

    public void loadArenas() {

        ConfigManager configManager = ConfigManager.getInstance();
        FileConfiguration fc = configManager.getConfig("Arena.yml");

        for (String presetName : fc.getKeys(false)) {
            arenaContainer.put(presetName, new Arena(presetName,
                    configManager.getLocation(fc,presetName + ".SPAWN1" ),
                    configManager.getLocation(fc,presetName + ".SPAWN2"),
                    configManager.getLocation(fc,presetName + ".SPAWN3"),
                    configManager.getLocation(fc,presetName + ".SPAWN4"),
                    configManager.getLocation(fc,presetName + ".TOWER")));
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "        -- " + presetName);
        }
    }

    public boolean saveArena(String arenaName, Location spawn1, Location spawn2, Location spawn3,
                             Location spawn4, Location tower) {

        Arena arena = new Arena(arenaName, spawn1, spawn2, spawn3, spawn4, tower);

        if (arenaContainer.putIfAbsent(arenaName, arena) == null) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin,() -> {
                ConfigManager configManager = ConfigManager.getInstance();
                FileConfiguration fc = configManager.getConfig("Arena.yml");
                configManager.addLocation(fc, spawn1 ,arenaName + ".SPAWN1");
                configManager.addLocation(fc, spawn2 ,arenaName + ".SPAWN2");
                configManager.addLocation(fc, spawn3 ,arenaName + ".SPAWN3");
                configManager.addLocation(fc, spawn4 ,arenaName + ".SPAWN4");
                configManager.addLocation(fc, tower ,arenaName + ".TOWER");
            });
            return true;
        }
        return false;
    }

    public void arenaCreationHandler(Player sender, Location location) {
        if(!playerArenaCreation.containsKey(sender.getUniqueId())) return;

        int senderStep = playerArenaCreation.get(sender.getUniqueId());
        switch(senderStep) {
            case SPAWN1:
                playerArenaCoordinates.put(sender.getUniqueId(), new HashMap<>());
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN1, location.add(0,1,0));

                Message.CREATION_MODE_STEP.send(sender,"SPAWN 2");

                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                particleEffect(location);
                break;
            case SPAWN2:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN2, location.add(0,1,0));

                Message.CREATION_MODE_STEP.send(sender,"SPAWN 3");

                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                particleEffect(location);
                break;
            case SPAWN3:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN3, location.add(0,1,0));

                Message.CREATION_MODE_STEP.send(sender,"SPAWN 4");

                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                particleEffect(location);
                break;
            case SPAWN4:
                playerArenaCoordinates.get(sender.getUniqueId()).put(SPAWN4, location.add(0,1,0));

                Message.CREATION_MODE_STEP.send(sender,"TOWER");

                location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
                particleEffect(location);
                break;
            case TOWER:
                playerArenaCoordinates.get(sender.getUniqueId()).put(TOWER, location.add(0,1,0));
                location.getWorld().playSound(location, Sound.ENTITY_PLAYER_LEVELUP,1,1);
                particleEffect(location);

                if (saveArena(playerArenaNameCreation.get(sender.getUniqueId()),
                        playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN1),
                        playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN2),
                        playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN3),
                        playerArenaCoordinates.get(sender.getUniqueId()).get(SPAWN4),
                        playerArenaCoordinates.get(sender.getUniqueId()).get(TOWER)))
                    Message.SUCCESS_ARENA_CREATION.send(sender, playerArenaNameCreation.get(sender.getUniqueId()).replace("_"," "));
                else
                    Message.ERR_ARENA_CREATION.send(sender);

                this.playerArenaNameCreation.remove(sender.getUniqueId());
                this.playerArenaCoordinates.remove(sender.getUniqueId());
                this.playerArenaCreation.remove(sender.getUniqueId());

                return;
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

    public Optional<Arena> getArena(String name) {
        return arenaContainer.containsKey(name) ? Optional.of(arenaContainer.get(name)) : Optional.empty();
    }

    public boolean isArenaName(String arenaName) {
        return arenaContainer.containsKey(arenaName);
    }

    public List<String> getArenaNameList() {
        return new ArrayList<>(arenaContainer.keySet());
    }

    public void particleEffect(Location loc) {
        Location location = loc.toCenterLocation();
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 69, 0), 5);
        location.getWorld().spawnParticle(Particle.REDSTONE,location,5,dustOptions);
    }

    public void removeArena(String arenaName) {

        if (arenaContainer.remove(arenaName) != null)
            Bukkit.getScheduler().scheduleSyncDelayedTask(FWRiv.plugin,() -> {
                ConfigManager configManager = ConfigManager.getInstance();
                configManager.setData(configManager.getConfig("Arena.yml"), arenaName, null );
            });

    }
}






