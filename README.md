**FWRiv** - Plugin per la gestione dell'evento "Resta in vetta" (RIV)

### LISTA COMANDI

(permesso necessario: rivevent.admin)
```sh
/fwriv create <nome _arena>
```
Inizia la procedura di creazione di una nuova arena.
```sh
/fwriv delete <nome_arena>
```
Rimuove l'arena.
```sh
/fwriv reload
```
Ricarica tutti i file di configurazione (Setting.yml, RespawnPoint.yml, Preset.yml).

(permesso necessario: rivevent.eventmanager)

```sh
/fwriv setrespawn
```
Imposta il punto di respawn sicuro.
```sh
/fwriv arena <nome_arena>
```
Stampa a schermo le coordinate dei punti salvati per l'arena indicata.
```sh
/fwriv init <nome_arena>
```
Inizializza l'evento RIV per l'arena indicata (da questo momento i players potranno partecipare all'evento tramite il comando /fwriv join).
```sh
/fwriv eventinfo
```
Mostra i nomi dei players partecipanti all'evento (se l'evento è in corso mostra anche la lista dei players ancora in gioco).
```sh
/fwriv start
```
Fa partire l'evento RIV.
```sh
/fwriv restart
```
Ripristina l'evento riportando i partecipanti ai punti di spawn.
```sh
/fwriv stop
```
Termina l'evento e teletrasporta tutti i partecipanti nel punto in cui avevano eseguito /fwriv join. (Se la posizione è ostruita il player viene teletrasportano nel punto di respawn sicuro).

(permesso necessario: rivevent.user)

```sh
/fwriv join
```
Teletrasporta il player nell'arena dell'evento e lo inserisce nella lista dei partecipanti.
```sh
/fwriv leave
```
Teletrasporta il player nel punto in cui aveva eseguito /fwriv join (se la posizione è ostruita il player viene teletrasportato nel punto di respawn sicuro).
```sh
/fwriv info
```
Stampa a schermo le principali caratteristiche del gameplay del RIV.





