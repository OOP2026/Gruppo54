package dao;

import model.Stanza;
import java.util.List;

/**
 * Interfaccia DAO per la gestione dell'allocazione e consultazione delle stanze ospedaliere.
 */
public interface StanzaDAO {

    /**
     * Recupera l'elenco delle stanze associate logisticamente ad un determinato reparto.
     * * @param codiceReparto L'identificativo univoco del reparto.
     * @return List&lt;Stanza&gt; Elenco delle stanze appartenenti al reparto specificato.
     */
    List<Stanza> ottieniStanzePerReparto(int codiceReparto);
}