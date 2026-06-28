package dao;

import model.TurnoLavorativo;
import java.util.List;

/**
 * Interfaccia DAO per la gestione della pianificazione oraria del personale medico.
 * Permette di validare la presenza in servizio del medico durante le attività cliniche.
 */
public interface TurnoLavorativoDAO {

    /**
     * Recupera l'intera pianificazione settimanale dei turni associati ad un determinato medico.
     * * @param usernameMedico Lo username dell'utente medico.
     * @return List&lt;TurnoLavorativo&gt; L'elenco strutturato dei turni orari del medico.
     */
    List<TurnoLavorativo> ottieniTurniPerMedico(String usernameMedico);
}