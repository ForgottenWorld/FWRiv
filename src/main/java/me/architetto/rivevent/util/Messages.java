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
    public static String ERR_NO_RESPAWN_LOCATION = "Errore: Zona di respawn mancante. Utilizza " + ChatColor.YELLOW + "/rivevent set respawn ";

    public static String CONFIG_RELOADED = "Config file ricaricato";


    public static String JOIN_STARTED_EVENT = "Sei stato aggiunto alla lista dei partecipanti";
    public static String JOIN_EVENT = "Sei stato aggiunto alla lista dei partecipanti";

    public static String DELETE_CDM_SUCCESS = "Arena eliminata con successo";

    public static String RESTART_CMD_PLAYER_MESSAGE = "Evento restartato. Sei stato automaticamente aggiunto alla lista dei partecipanti";

    public static String STOP_CMD_PLAYER_MESSAGE = "Evento concluso";
    public static String STOP_CMD_SENDER_MESSAGE = "Evento terminato con successo";

    public static String LEAVE_OK = "Hai abbandonato l'evento";

    public static String CURSE_MSG1 = "Ti hanno passato la maledizione. Colpisci qualcuno per liberartene.";
    public static String CURSE_MSG2 = "Ti sei liberato della maledizione.";


    public static String RIV_GAMEPLAY_INFO = ChatColor.YELLOW + "[+] " + ChatColor.RESET + "Il cibo non scende mai a zero" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Ricevi premi stando in cima alla torre" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "Ricevi danni stando sotto l'altezza minima dell'anti-camper" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Non è possibile droppare items, piazzare e rompere blocchi" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "I target block danno effetti particolari (click destro)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Le snowball hanno knockback e danno" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "Puoi scambiarti di posizione con la canna da pesca (monouso)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.GREEN + "Il tridente ha un buon knockback quando utilizzato corpo a corpo (monouso)" +
            ChatColor.YELLOW + "\n[+] " + ChatColor.RESET + "Il tridente, quando lanciato, applica slowness al bersaglio (monouso)";



}
