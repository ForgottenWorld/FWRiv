package me.architetto.rivevent.util;

import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;

public class Messages{

    //TODO: DA SISTEMARE (PROBABILMENTE CI SONO MESSAGGI CHE NON SERVONO, MENTRE QUALCUNO CHE SERVE NON C'E')

    public static final String NO_CONSOLE = "Comando non eseguibile in console.";
    public static final String NO_PERM = "Non hai i permessi necessari.";
    public static final String NO_PARAM = "Non hai inserito correttamente i parametri.";
    public static final String NO_PRESET = "Nessun preset con questo nome. Usa '/rivevent list' per la lista dei preset presenti.";

    public static final String ERR_EVENT = "Un evento e' già in corso.";
    public static final String ERR_NO_EVENT = "Nessun evento in corso.";
    public static final String ERR_PRESET = "Preset già esistente.";

    public static final String SUCCESS_DELETE = "Preset eliminato.";

    public static final String VOID_PRESET_LIST = "Nessun preset salvato.";

    public static final String ERR_SETUP_DONE = "Setup gia' effettuato.";
    public static final String ERR_SETUP_NOTREADY = "Setup non effettuato o non ancora terminato.";

    public static final String ERR_JOIN = "Sei gia' presente nella lista dei partecipanti.";
    public static final String ERR_SPECTATE = "Sei gia' presente nella lista dei spettatori.";

    public static final String NO_EVENT_JOINED = "Non stai partecipando a nessun evento!";

    public static final String STOP_EVENT = "Evento cancellato.";

    public static final String OKPRESET = "Il preset è stato correttamente inserito.";
    public static final String OK_INIT = "Evento inizializzato. I player possono joinare con il comando /rivevent join .";
    public static final String OK_JOIN = "Sei stato inserito nella lista dei partecipanti.";
    public static final String OK_SETUP = "Tutti i player sono stati teletrasportati. Per iniziare l'evento /rivevent start .";
    public static final String OK_SPECTATE = "Sei stato inserito nella lista dei spettatori.";

    public static final String USE_SPECTATE = "L'evento e' gia' iniziato! Usa /rivevent spectate per guardare l'evento.";

    public static final String OK_LEAVE = "Grazie per aver partecipato all'evento!";


    public static final String START_TITLE = "3,2,1...." + ChatColor.DARK_RED + "VIA !!!";
    public static final String START_SUBTITLE = "Conquistate la torre se ne avete la forza !";

}
