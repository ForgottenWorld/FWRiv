package me.architetto.fwriv.echelon;

import it.forgottenworld.echelonapi.FWEchelonApi;
import it.forgottenworld.echelonapi.services.MutexActivityService;
import me.architetto.fwriv.listener.MyMutexActivity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EchelonHolder {

    private static EchelonHolder echelonHolder;

    private MutexActivityService mutexActivityService;

    private EchelonHolder() {
        if(echelonHolder != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

    }

    public static EchelonHolder getEchelonHolder() {
        if(echelonHolder ==null)
            echelonHolder = new EchelonHolder();

        return echelonHolder;
    }

    public boolean loadEchelonService() {

        FWEchelonApi echelon = (FWEchelonApi) Bukkit.getPluginManager().getPlugin("FWEchelon");
        this.mutexActivityService = echelon.getMutexActivityService();

        return this.mutexActivityService.registerMutexActivity(("FWRiv"), new MyMutexActivity());
    }

    public boolean isPlayerInMutexActivity(Player player) {
        return mutexActivityService.isPlayerInMutexActivity(player);
    }

    public void addPlayerMutexActivity(Player player) {
        mutexActivityService.playerJoinMutexActivity(player,"FWRiv", true);
    }

    public void removePlayerMutexActivity(Player player) {
        mutexActivityService.removePlayerFromMutexActivity(player, "FWRiv");
    }

    public String getPlayerMutexActivityName(Player player) {
        return mutexActivityService.getPlayerMutexActivityName(player);
    }

    public void removeAllMutexActivityRIVPlayer() {
        mutexActivityService.forceRemoveAllPlayersFromMutexActivity("FWRiv","Event stopped");
    }

}
