package dao;

import model.Reparto;
import java.util.List;

/**
 * Interfaccia DAO per la gestione delle macro-strutture ospedaliere rappresentate dai reparti.
 */
public interface RepartoDAO {

    /**
     * Estrae il catalogo completo di tutti i reparti configurati nell'ospedale.
     * * @return List&lt;Reparto&gt; Elenco di tutte le entità reparto censite.
     */
    List<Reparto> ottieniTuttiIReparti();
}