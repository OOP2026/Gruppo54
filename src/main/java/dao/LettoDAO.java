package dao;

import model.Letto;
import java.util.List;

/**
 * Interfaccia DAO focalizzata sulla gestione e monitoraggio dei posti letto censiti nella struttura.
 */
public interface LettoDAO {

    /**
     * Restituisce la mappatura di tutti i letti configurati all'interno di una determinata stanza.
     * * @param numeroStanza Il numero della stanza da ispezionare.
     * @return List&lt;Letto&gt; Elenco dei letti della stanza.
     */
    List<Letto> ottieniLettiPerStanza(int numeroStanza);

    /**
     * Recupera l'entità del singolo letto mediante l'identificativo univoco di chiave primaria.
     * * @param idLetto L'ID del letto.
     * @return Letto L'istanza del modello Letto configurata sul database, o null se inesistente.
     */
    Letto ottieniLettoPerId(int idLetto);
}