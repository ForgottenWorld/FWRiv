package me.architetto.rivevent.listener;

import me.architetto.rivevent.command.GlobalVar;
import me.architetto.rivevent.util.ChatMessages;
import me.architetto.rivevent.util.LocSerialization;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class TopOfTowerListener implements Listener{

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){

        GlobalVar global = GlobalVar.getInstance();

        if (global.playerJoined.contains(event.getPlayer().getUniqueId())){

            if (event.getPlayer().getLocation().distance(LocSerialization  //Probabilmente sostituirò 'distance' con qualcosa di più leggero.
                    .getDeserializedLocation(global.riveventPreset.get(global.presetSummon).get(LeftClickListener.LOC.TOWER))) <= 1) {  //Magari check solo della y


                Player player = event.getPlayer();


                Material material = randomItem();

                ItemStack itemStack = new ItemStack(material,randomAmount(material));

                player.getInventory().addItem(itemStack);

                //MESSAGGIO PER IL PLAYER

                //TODO: INSERIRE COOLDOWN

            }
        }
    }

    public Material randomItem() {

        GlobalVar global = GlobalVar.getInstance();
        int randomNum = global.itemList.size();

        randomNum = ThreadLocalRandom.current().nextInt(1, randomNum);

        return global.itemList.get(randomNum);

    }

    public int randomAmount(Material material) {

        if (material.getMaxStackSize()==1)
            return 1;

        int randomNum = 10;
        randomNum = ThreadLocalRandom.current().nextInt(1, randomNum+1);

        return randomNum;

    }

}
