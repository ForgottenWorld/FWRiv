package me.architetto.rivevent.command.superuser;

import me.architetto.rivevent.command.SubCommand;
import me.architetto.rivevent.event.EventService;
import me.architetto.rivevent.util.ChatFormatter;
import org.bukkit.entity.Player;

import java.util.List;

public class StartCommand extends SubCommand{
    @Override
    public String getName(){
        return "start";
    }

    @Override
    public String getDescription(){
        return "Start event.";
    }

    @Override
    public String getSyntax(){
        return "/rivevent start";
    }

    /*

    GameHandler global = GameHandler.getInstance();
    SettingsHandler settings = SettingsHandler.getInstance();

    List<Material> noDoubleItem = new ArrayList<>(Arrays.asList(Material.LEATHER_BOOTS,
            Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS, Material.FISHING_ROD));

    public int redLineY;

     */

    @Override
    public void perform(Player sender, String[] args){

        if (!sender.hasPermission("rivevent.start")) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Permission Error: you do not have permission to use that command"));
            return;
        }

        EventService eventService = EventService.getInstance();

        if (!eventService.isRunning()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: no event is running"));
            return;

        } else if (!eventService.isSetupDone()) {
            sender.sendMessage(ChatFormatter.formatErrorMessage("Error: you have to do the /rivevent setup "));
            return;
        }

        eventService.startEventTimer();

                /*


        if (settings.rewardPlayersOnTopToggle) {
            rewardPlayerOnTop();
        }

         */



    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args){
        return null;
    }


    /*

    //-------------------- REWARD PLAYER ------------------------//


    public void rewardPlayerOnTop() {

        new BukkitRunnable() {

            private final int towerTopY = LocSerialization.getDeserializedLocation(global.riveventPreset
                    .get(global.presetSummon).get(RightClickListener.Step.TOWER)).getBlockY();

            @Override
            public void run(){

                if (global.playerJoined.size() < settings.rewardMinPlayer || !global.setupStartFlag) {
                    this.cancel();
                    return;
                }

                for (UUID key : global.playerJoined) {

                    Player player = Bukkit.getPlayer(key);

                    if (player == null)
                        continue;

                    if (player.getInventory().firstEmpty() == -1){
                        player.sendMessage(ChatMessages.AQUA("Non hai ricevuto nulla perchè il tuo inventario è pieno!"));
                        continue;
                    }

                    if (player.getLocation().getBlockY() >= towerTopY) {

                        Material material = global.pickRandomItem();

                        if (noDoubleItem.contains(material) && player.getInventory().contains(material)){
                            player.sendMessage(ChatMessages.AQUA("Nulla di utile, sarai piu' fortunato al prossimo giro!"));
                            continue;
                        }

                        ItemStack itemStack = new ItemStack(material, global.pickRandomAmount(material));

                        player.getInventory().addItem(itemStack);

                        player.sendMessage(ChatMessages.AQUA("Hai ricevuto : " + itemStack.getI18NDisplayName()));

                        player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP,1,1);

                    }
                }
            }
        }.runTaskTimer(RIVevent.plugin,0,settings.rewardPlayerPeriod);

    }

    //--------------------  ANTI CAMPER  ------------------------//

*/








}
