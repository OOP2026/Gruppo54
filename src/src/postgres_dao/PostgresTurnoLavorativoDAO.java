package postgres_dao;

import dao.TurnoLavorativoDAO;
import model.TurnoLavorativo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link TurnoLavorativoDAO} per il DBMS PostgreSQL.
 * Gestisce il recupero e la mappatura dei turni settimanali del personale medico per la validazione oraria.
 */
public class PostgresTurnoLavorativoDAO implements TurnoLavorativoDAO {

    /**
     * Recupera dal database la pianificazione dei turni lavorativi associati a uno specifico medico.
     * Sfrutta il costrutto try-with-resources per garantire il rilascio controllato delle risorse JDBC.
     *
     * @param usernameMedico Lo username identificativo del medico.
     * @return List&lt;TurnoLavorativo&gt; L'elenco dei turni riscontrati, o una lista vuota in caso di errore.
     */
    @Override
    public List<TurnoLavorativo> ottieniTurniPerMedico(String usernameMedico) {
        List<TurnoLavorativo> listaTurni = new ArrayList<>();
        String query = "SELECT giorno, fascia_oraria FROM turno_lavorativo WHERE username_medico = ? " +
                "ORDER BY CASE " +
                "  WHEN UPPER(giorno) LIKE 'LUN%' THEN 1 " +
                "  WHEN UPPER(giorno) LIKE 'MAR%' THEN 2 " +
                "  WHEN UPPER(giorno) LIKE 'MER%' THEN 3 " +
                "  WHEN UPPER(giorno) LIKE 'GIO%' THEN 4 " +
                "  WHEN UPPER(giorno) LIKE 'VEN%' THEN 5 " +
                "  WHEN UPPER(giorno) LIKE 'SAB%' THEN 6 " +
                "  WHEN UPPER(giorno) LIKE 'DOM%' THEN 7 " +
                "  ELSE 8 END ASC, fascia_oraria ASC";
        try (Connection conn = database.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, usernameMedico);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String giorno = rs.getString("giorno");
                    String fasciaOraria = rs.getString("fascia_oraria");

                    // Creiamo l'oggetto di modello (lasciamo medico a null perché conosciamo già lo username)
                    TurnoLavorativo turno = new TurnoLavorativo(giorno, fasciaOraria, null);
                    listaTurni.add(turno);
                }
            }

        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Sostituito e.printStackTrace() con la stampa del messaggio d'errore nativo
            System.err.println("ERRORE CRITICO NEL RECUPERO DEI TURNI DEL MEDICO " + usernameMedico + ": " + e.getMessage());
        }

        return listaTurni;
    }
}