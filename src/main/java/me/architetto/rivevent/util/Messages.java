package me.architetto.rivevent.util;

import org.bukkit.ChatColor;

public class Messages {

    public static String ERR_PERMISSION = "Errore: Non possiedi i permessi necessari per seguire questo comando";

    public static String ERR_NO_EVENT_RUNNING = "Errore: Nessun evento in corso";
    public static String ERR_EVENT_RUNNING = "Errore: Un evento è in esecuzione";
    public static String ERR_ALREADY_JOINED = "Errore: Stai già partecipando all'evento";
    public static String ERR_NO_EVENT_JOINED = "Errore: Non stai partecipando a nessun evento";
    public static String ERR_ARENA_CMD_SYNTAX = "Errore: Inserisci il nome dell'arena";
    public static String ERR_NO_ARENA = "Errore: Nessuna arena salvata";
    public static String ERR_NO_ARENA_NAME = "Errore: Nessuna arena con questo nome";
    public static String ERR_ARENA_NAME = "Errore: E' già presente un'arena con questo nome";
    public static String ERR_CREATION1 = "Errore: Stai già creando un'arena";
    public static String ERR_NO_RESPAWN_LOCATION = "Errore: Zona di respawn sicuro mancante. Utilizza " + ChatColor.YELLOW + "/rivevent set respawn ";

    public static String CONFIG_RELOADED = "Config file ricaricato";

    public static String JOIN_STARTED_EVENT = "Sei stato aggiunto alla lista dei partecipanti";
    public static String JOIN_EVENT = "Sei stato aggiunto alla lista dei partecipanti";

    public static String DELETE_CDM_SUCCESS = "Arena eliminata con successo";

    public static String NOT_ENOUGH_PLAYERS = "Non ci sono abastanza players ...";

    public static String START_CD_MSG = "L'evento iniziera' tra 10 secondi ...";

    public static String RESTART_CMD_PLAYER_MESSAGE = "Evento restartato. Sei stato automaticamente aggiunto alla lista dei partecipanti";

    public static String STOP_CMD_PLAYER_MESSAGE = "Evento concluso";
    public static String STOP_CMD_SENDER_MESSAGE = "Evento terminato con successo";

    public static String LEAVE_OK = "Hai abbandonato l'evento";

    public static String REWARD_INVENTORY_FULL = "Inventario pieno! Nessun reward ottenuto.";
    public static String REWARD_NO_UNIQUE = "Nessun reward ottenuto... sarai più fortunato la prossima volta.";

    public static String CURSED_PLAYER_START_TITLE = ChatColor.MAGIC + "Architetto" + ChatColor.RESET + ChatColor.RED + " ti ha maledetto!";
    public static String CURSED_PLAYER_START_SUBTITLE = ChatColor.ITALIC + "colpisci qualcuno per passare la maledizione";
    public static String NOT_CURSED_PLAYER_ALLERT = ChatColor.DARK_RED + "!! WARNING !!" + ChatColor.RESET + " uno dei partecipanti è stato maledetto!";
    public static String CURSE_MSG1 = "Ti hanno passato la maledizione. Colpisci qualcuno per liberartene.";
    public static String CURSE_MSG2 = "Ti sei liberato della maledizione.";
    public static String CURSE_MSG3 = "La maledizione si è abbattuta su di te!";
    public static String CURSED_PLAYER_DIE = "Il player maledetto e' morto !";
    public static String CURSED_PLAYER_DIE_WITH_TOTEM = "La maledizione era troppo potente, il totem non è riuscito a proteggerti!";


    public static String DEATHRACE_START_MSG = "EVENTO " + ChatColor.RED + "DEATH RACE " + ChatColor.RESET + ": 'ad ogni giro' muore il player più in basso. ";
    public static String DEATHRACE_DEATH_MSG = "E' la prima regola del " + ChatColor.DARK_RED + "'deathreace club'" + ChatColor.RESET + " : non essere l'ultimo!";

    public static String RIV_GAMEPLAY_INFO = ChatColor.YELLOW + "[+] " + ChatColor.RESET + "Il cibo non scende mai a zero" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Ricevi premi stando in cima alla torre" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "Ricevi danni stando sotto l'altezza minima dell'anti-camper" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Non è possibile droppare items, piazzare e rompere blocchi" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "I target block danno effetti particolari (click destro)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Le snowball hanno knockback e danno" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "Puoi scambiarti di posizione con la canna da pesca (monouso)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Il tridente ha un buon knockback quando utilizzato corpo a corpo (monouso)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "Il tridente, quando lanciato, applica slowness al bersaglio (monouso)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Il totem of undying ti teleporta in cima alla torre quando si muore";



}
