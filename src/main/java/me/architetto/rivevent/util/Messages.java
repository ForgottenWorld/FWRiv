package me.architetto.rivevent.util;

import org.bukkit.ChatColor;

public class Messages{

    public static final String NO_CONSOLE = "Comando non eseguibile in console.";
    public static final String NO_PERM = "Non hai i permessi necessari per eseguire questo comando.";
    public static final String NO_PRESET_NAME = "Non hai inserito il nome del preset.";
    public static final String NO_PARAM = "Non hai inserito correttamente i parametri.";
    public static final String NO_PRESET = "Nessun preset con questo nome. Usa '/rivevent list' per la lista dei preset presenti.";
    public static final String NO_EVENT_JOINED = "Non stai partecipando a nessun evento!";
    public static final String NO_PLAYER_JOINED = "Non ci sono abbastanza partecipanti per iniziare l'evento!";

    public static final String OK_INIT = "Evento inizializzato...";
    public static final String OK_JOIN = "Sei stato inserito nella lista dei partecipanti.";
    public static final String OK_SETUP = "Tutti i player sono stati teletrasportati. Per iniziare l'evento /rivevent start .";
    public static final String OK_SPECTATE = "Sei stato inserito nella lista degli spettatori.";
    public static final String OK_LEAVE = "Grazie per aver partecipato all'evento!";
    public static final String OK_PRESET = "Il preset è stato correttamente inserito.";

    public static final String ERR_INSERT_PRESET = "Si e' presentato un errore nell'inserimento del preset. Ripetere l'operazione.";
    public static final String ERR_EVENT = "Un evento e' già in corso.";
    public static final String ERR_MINIEVENT = "Un mini-evento e' già in corso.";
    public static final String ERR_NO_EVENT = "Nessun evento in corso.";
    public static final String ERR_PRESET = "Esiste gia' un preset con questo nome, utilizzare un nome diverso.";
    public static final String ERR_PRESET2 = "Stai gia' creando un preset ...";
    public static final String ERR_SETUP_DONE = "Setup gia' effettuato.";
    public static final String ERR_SETUP_NOTREADY = "Setup non effettuato o non ancora terminato.";
    public static final String ERR_START = "Per iniziare un mini-evento l'evento deve essere già iniziato...";
    public static final String ERR_JOIN = "Sei gia' presente nella lista dei partecipanti.";
    public static final String ERR_JOIN2 = "Sei gia' presente nella lista degli spettatori." +
            "\n" + "Usa prima /Rivevent leave -- e poi --> /Rivevent join " +
            "\n" + "per entrare nella lista dei partecipanti !";

    public static final String ERR_SPECTATE = "Sei gia' presente nella lista degli spettatori.";
    public static final String ERR_SPECTATE2 = "Sei gia' presente nella lista dei partecipanti." +
            "\n" + "Usa prima /Rivevent leave -- e poi --> /Rivevent spectate " +
            "\n" + "per entrare nella lista degli spettatori !";

    public static final String SUCCESS_DELETE = "Preset eliminato.";

    public static final String VOID_PRESET_LIST = "Nessun preset salvato.";

    public static final String STOP_EVENT = "Evento terminato.";

    public static final String USE_SPECTATE = "L'evento e' gia' iniziato! Usa /rivevent spectate per guardare l'evento.";

    public static final String START_ALLERT_TITLE = "READY ...";
    public static final String START_TITLE = ChatColor.DARK_RED + "GO !";
    public static final String START_SUBTITLE = "Conquista la torre se ne avete la forza !";

    public static final String BROADCAST_EVENT =
            "\n" + "## ---------- EVENTO : RESTA IN VETTA ---------- ##" + "\n" +
            " - Per partecipare -> /rivevent join " + "\n" +
            " - Per assistere -> /rivevent spectate" + "\n" +
            ChatColor.RED + ChatColor.ITALIC + ChatColor.UNDERLINE + "PS: Partecipando l'inventario verra' perso!";
    public static final String NEW_EVENT_TITLE = "Partecipa all'evento 'Resta in vetta' !" + ChatColor.AQUA + " /rivevent join";

    public static final String ANTI_CAMPER_MSG = "I superni si accaniscono su di te perche' sei ultimo!";

    public static final String DEATH_MESSAGE = "Sei stato bravo, ma hai perso !"
            + "\n" + "Sarai più fortunato la prossima volta ."
            + "\n" + "Se vuoi abbandonare l'evento fai /rivevent leave .";

    public static final String VICTORY_SUBTITLE = "E' il vincitore dell'evento, COMPLIMENTI !";

    public static final String ALLERT_END_EVENT = "Reminder : Terminate l'evento con /rivevent stop";

}
