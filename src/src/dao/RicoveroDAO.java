package dao;

import model.Ricovero;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dei ricoveri dei pazienti e dell'occupazione posti letto.
 * Fornisce le operazioni per garantire l'integrità temporale delle allocazioni.
 */
public interface RicoveroDAO {

    /**
     * Registra l'anagrafica di un paziente (se non presente) e inserisce una transazione di ricovero.
     * * @param ricovero L'istanza contenente i dettagli della degenza e del letto assegnato.
     * @param nome Il nome del paziente.
     * @param cognome Il cognome del paziente.
     * @return boolean True se la transazione atomica è confermata con successo, false altrimenti.
     */
    boolean registraNuovoRicovero(Ricovero ricovero, String nome, String cognome);

    /**
     * Verifica la presenza di sovrapposizioni temporali per un determinato letto in un intervallo di date.
     * * @param idLetto L'identificativo univoco del posto letto.
     * @param dataInizio La data d'inizio del nuovo ricovero da verificare.
     * @param dataFine La data di dimissione prevista da verificare.
     * @return boolean True se il letto risulta occupato nel periodo indicato, false se è libero.
     */
    boolean verificaLettoOccupatoNelleDate(int idLetto, String dataInizio, String dataFine);

    /**
     * Recupera un record di ricovero specifico partendo dal suo codice identificativo univoco.
     * * @param idRicovero L'ID del ricovero da cercare.
     * @return Ricovero L'oggetto contenente i dati del ricovero associato, o null se non trovato.
     */
    Ricovero ottieniRicoveroPerId(int idRicovero);

    /**
     * Estrae i ricoveri la cui data di dimissione coincide con il giorno specificato.
     * * @param dataFormatoIso La data da analizzare in formato standard ISO (AAAA-MM-GG).
     * @return List&lt;Ricovero&gt; Elenco dei pazienti in scadenza di dimissione.
     */
    List<Ricovero> ottieniRicoveriInScadenza(String dataFormatoIso);
}