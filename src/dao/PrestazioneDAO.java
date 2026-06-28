package dao;

import model.Prestazione;
import java.util.List;

/**
 * Interfaccia DAO per la gestione della persistenza delle prestazioni cliniche.
 * Definisce i metodi per la registrazione, verifica e aggiornamento di visite e interventi.
 */
public interface PrestazioneDAO {

    /**
     * Registra una nuova prestazione medica nel sistema.
     * * @param prestazione L'oggetto contenente i dettagli della prestazione da salvare.
     * @return boolean True se l'inserimento è riuscito, false altrimenti.
     */
    boolean aggiungiPrestazione(Prestazione prestazione);

    /**
     * Verifica se il medico ha già una prestazione programmata in una determinata data e ora.
     * * @param usernameMedico Lo username identificativo del medico.
     * @param data La data della prestazione da controllare.
     * @param orario L'orario della prestazione da controllare.
     * @return boolean True se esiste già una sovrapposizione oraria, false altrimenti.
     */
    boolean verificaPrestazioneSovrapposta(String usernameMedico, String data, String orario);

    /**
     * Recupera lo storico di tutte le prestazioni erogate da un determinato medico.
     * * @param usernameMedico Lo username identificativo del medico.
     * @return List&lt;Prestazione&gt; L'elenco delle prestazioni associate al medico.
     */
    List<Prestazione> ottieniPrestazioniPerMedico(String usernameMedico);

    /**
     * Aggiorna il referto o l'esito clinico di una prestazione esistente.
     * * @param idRicovero L'identificativo del ricovero associato.
     * @param data La data in cui è stata eseguita la prestazione.
     * @param orario L'orario in cui è stata eseguita la prestazione.
     * @param nuovoEsito Il nuovo testo descrittivo dell'esito clinico.
     * @return boolean True se la modifica è andata a buon fine, false altrimenti.
     */
    boolean aggiornaEsitoPrestazione(int idRicovero, String data, String orario, String nuovoEsito);
}