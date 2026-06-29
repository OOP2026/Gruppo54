package implementazioneDao;

import dao.RepartoDAO;
import model.Reparto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link RepartoDAO} per PostgreSQL.
 * Gestisce il recupero delle informazioni strutturali relative ai reparti ospedalieri dal DB.
 */
public class PostgresRepartoDAO implements RepartoDAO {

    /**
     * Esegue l'interrogazione sul database per estrarre l'elenco completo dei reparti censiti.
     * Sfrutta il try-with-resources per l'apertura e chiusura automatica delle risorse JDBC.
     *
     * @return List&lt;Reparto&gt; Lista contenente tutti i reparti trovati, o una lista vuota in caso di errore.
     */
    @Override
    public List<Reparto> ottieniTuttiIReparti() {
        List<Reparto> listaReparti = new ArrayList<>();
        String query = "SELECT codice, nome_reparto FROM reparto";

        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("codice");
                String nome = rs.getString("nome_reparto");
                Reparto r = new Reparto(id, nome);
                listaReparti.add(r);
            }
        } catch (SQLException e) {
            // RISOLTO CODE SMELL: Rimosso e.printStackTrace() ed eliminato l'import inutile di SSLException
            System.err.println("ERRORE CRITICO NEL RECUPERO DEI REPARTI DAL DATABASE: " + e.getMessage());
        }
        return listaReparti;
    }
}