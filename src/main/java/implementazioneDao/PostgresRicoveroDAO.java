package implementazioneDao;

import dao.RicoveroDAO;
import model.Ricovero;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta dell'interfaccia {@link RicoveroDAO} per PostgreSQL.
 * Gestisce i flussi transazionali critici per l'accettazione e la registrazione dei pazienti,
 * garantendo la consistenza dei vincoli temporali di degenza e lo stato dei posti letto.
 */
public class PostgresRicoveroDAO implements RicoveroDAO {

    /**
     * Registra un nuovo ricovero ospedaliero eseguendo una serie di controlli di integrità.
     * Questa operazione è gestita all'interno di una transazione SQL protetta: se una qualsiasi
     * fase fallisce, viene invocato un rollback per preservare la consistenza dei dati.
     *
     * @param ricovero         L'oggetto modello contenente i dati del ricovero.
     * @param nomePaziente     Il nome del paziente da sottoporre a ricovero.
     * @param cognomePaziente  Il cognome del paziente da sottoporre a ricovero.
     * @return boolean         True se l'intera transazione si conclude con successo, false altrimenti.
     */
    @Override
    public boolean registraNuovoRicovero(Ricovero ricovero, String nomePaziente, String cognomePaziente) {
        String queryVerificaAnagrafica = "SELECT nome, cognome FROM paziente WHERE codice_fiscale = ?";

        String queryControlloPaziente = "SELECT COUNT(*) FROM ricovero WHERE cf_paziente = ? AND (" +
                "(data_inizio <= ? AND (data_fine >= ? OR data_fine IS NULL)) OR " +
                "(data_inizio >= ? AND data_inizio <= ?))";

        String queryPaziente = "INSERT INTO paziente (codice_fiscale, nome, cognome) VALUES (?, ?, ?) ON CONFLICT (codice_fiscale) DO NOTHING";
        String queryRicovero = "INSERT INTO ricovero (data_inizio, data_fine, cf_paziente, id_letto) VALUES (?, ?, ?, ?)";
        String queryLetto = "UPDATE letto SET occupato = TRUE WHERE id_letto = ?";

        Connection conn = null;
        try {
            conn = database_connection.ConnessioneDB.getConnection();
            conn.setAutoCommit(false); // RISOLTO: Inizio esplicito della transazione ACID

            // 1. CONTROLLO INCONGRUENZA ANAGRAFICA
            try (PreparedStatement psCheckAnagrafica = conn.prepareStatement(queryVerificaAnagrafica)) {
                psCheckAnagrafica.setString(1, ricovero.getCfPaziente());
                try (ResultSet rs = psCheckAnagrafica.executeQuery()) {
                    if (rs.next()) {
                        String nomeDB = rs.getString("nome");
                        String cognomeDB = rs.getString("cognome");

                        if (!nomeDB.equalsIgnoreCase(nomePaziente.trim()) || !cognomeDB.equalsIgnoreCase(cognomePaziente.trim())) {
                            System.err.println("❌ ERRORE: Il Codice Fiscale " + ricovero.getCfPaziente() +
                                    " appartiene già a " + nomeDB + " " + cognomeDB);
                            conn.rollback();
                            return false;
                        }
                    }
                }
            }

            // 2. CONTROLLO INTEGRITÀ PAZIENTE (Evita ricoveri sovrapposti dello stesso utente)
            try (PreparedStatement psCheck = conn.prepareStatement(queryControlloPaziente)) {
                psCheck.setString(1, ricovero.getCfPaziente());
                psCheck.setDate(2, java.sql.Date.valueOf(ricovero.getDataInizio()));
                psCheck.setDate(3, java.sql.Date.valueOf(ricovero.getDataInizio()));
                psCheck.setDate(4, java.sql.Date.valueOf(ricovero.getDataInizio()));
                psCheck.setDate(5, java.sql.Date.valueOf(ricovero.getDataFine()));

                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.err.println("❌ ERRORE: Il paziente è già ricoverato in un altro letto in questo periodo!");
                        conn.rollback();
                        return false;
                    }
                }
            }

            // 3. Inserimento Paziente (Idempotente tramite ON CONFLICT)
            try (PreparedStatement psPaziente = conn.prepareStatement(queryPaziente)) {
                psPaziente.setString(1, ricovero.getCfPaziente());
                psPaziente.setString(2, nomePaziente);
                psPaziente.setString(3, cognomePaziente);
                psPaziente.executeUpdate();
            }

            // 4. Inserimento Record di Ricovero
            try (PreparedStatement psRicovero = conn.prepareStatement(queryRicovero)) {
                psRicovero.setDate(1, java.sql.Date.valueOf(ricovero.getDataInizio()));
                psRicovero.setDate(2, java.sql.Date.valueOf(ricovero.getDataFine()));
                psRicovero.setString(3, ricovero.getCfPaziente());
                psRicovero.setInt(4, ricovero.getIdLetto());
                psRicovero.executeUpdate();
            }

            // 5. Aggiornamento Letto se il ricovero risulta attivo a sistema
            LocalDate inizioRicovero = LocalDate.parse(ricovero.getDataInizio());
            if (!inizioRicovero.isAfter(LocalDate.now())) {
                try (PreparedStatement psLetto = conn.prepareStatement(queryLetto)) {
                    psLetto.setInt(1, ricovero.getIdLetto());
                    psLetto.executeUpdate();
                }
            }

            conn.commit(); // RISOLTO: Chiusura atomica con successo
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Annullamento modifiche parziali
                } catch (SQLException ex) {
                    System.err.println("Errore durante l'esecuzione del rollback: " + ex.getMessage());
                }
            }
            System.err.println("ERRORE CRITICO DURANTE LA TRANSAZIONE DI RICOVERO: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Errore nella chiusura della connessione: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Verifica la presenza di un ricovero già programmato o attivo su uno specifico letto nell'intervallo date fornito.
     *
     * @param idLetto      L'ID identificativo del letto.
     * @param dataInizio   La data iniziale della finestra temporale da controllare.
     * @param dataFine     La data finale della finestra temporale da controllare.
     * @return boolean     True se riscontra una sovrapposizione d'occupazione, false se è libero.
     */
    @Override
    public boolean verificaLettoOccupatoNelleDate(int idLetto, String dataInizio, String dataFine) {
        String query = "SELECT COUNT(*) FROM ricovero WHERE id_letto = ? AND (" +
                "(data_inizio <= ? AND (data_fine >= ? OR data_fine IS NULL)) OR " +
                "(data_inizio >= ? AND data_inizio <= ?))";

        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idLetto);
            pstmt.setDate(2, java.sql.Date.valueOf(dataInizio));
            pstmt.setDate(3, java.sql.Date.valueOf(dataInizio));
            pstmt.setDate(4, java.sql.Date.valueOf(dataInizio));
            pstmt.setDate(5, java.sql.Date.valueOf(dataFine));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRORE NELLA VERIFICA SOVRAPPOSIZIONE DATE LETTO: " + e.getMessage());
        }
        return false;
    }

    /**
     * Recupera le specifiche di un ricovero partendo dal suo ID univoco.
     *
     * @param idRicovero L'ID identificativo del ricovero.
     * @return Ricovero   L'istanza popolata dell'oggetto Ricovero, o null se non localizzato.
     */
    @Override
    public Ricovero ottieniRicoveroPerId(int idRicovero) {
        String query = "SELECT data_inizio, data_fine, cf_paziente, id_letto FROM ricovero WHERE id_ricovero = ?";
        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idRicovero);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dataInizio = rs.getDate("data_inizio").toString();
                    java.sql.Date dataFineSql = rs.getDate("data_fine");
                    String dataFine = (dataFineSql != null) ? dataFineSql.toString() : null;
                    String cfPaziente = rs.getString("cf_paziente");
                    int idLetto = rs.getInt("id_letto");

                    return new Ricovero(dataInizio, dataFine, cfPaziente, idLetto);
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRORE NEL RECUPERO DEL RICOVERO PER ID " + idRicovero + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Fornisce l'elenco di tutti i ricoveri la cui data di dimissione prevista coincide esattamente con il giorno specificato.
     *
     * @param dataFormatoIso La stringa della data d'interesse strutturata nel formato ISO (AAAA-MM-GG).
     * @return List&lt;Ricovero&gt; Elenco dei ricoveri in scadenza rilevati.
     */
    @Override
    public List<Ricovero> ottieniRicoveriInScadenza(String dataFormatoIso) {
        List<Ricovero> lista = new ArrayList<>();
        String query = "SELECT id_ricovero, data_inizio, data_fine, cf_paziente, id_letto FROM ricovero WHERE data_fine = ?";

        try (Connection conn = database_connection.ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, java.sql.Date.valueOf(dataFormatoIso));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String dataInizio = rs.getDate("data_inizio").toString();
                    String dataFine = rs.getDate("data_fine").toString();
                    String cfPaziente = rs.getString("cf_paziente");
                    int idLetto = rs.getInt("id_letto");

                    lista.add(new Ricovero(dataInizio, dataFine, cfPaziente, idLetto));
                }
            }
        } catch (SQLException e) {
            System.err.println("ERRORE NEL RECUPERO DEI RICOVERI IN SCADENZA PER DATA " + dataFormatoIso + ": " + e.getMessage());
        }
        return lista;
    }
}