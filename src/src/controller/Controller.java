package controller;

import dao.LettoDAO;
import dao.RepartoDAO;
import dao.StanzaDAO;
import dao.UtenteDAO;
import dao.TurnoLavorativoDAO;
import dao.PrestazioneDAO;
import dao.RicoveroDAO;
import model.*;
import postgres_dao.PostgresLettoDAO;
import postgres_dao.PostgresRepartoDAO;
import postgres_dao.PostgresStanzaDAO;
import postgres_dao.PostgresUtenteDAO;
import postgres_dao.PostgresTurnoLavorativoDAO;
import postgres_dao.PostgresPrestazioneDAO;
import postgres_dao.PostgresRicoveroDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Componente Controller principale del pattern MVC dell'applicazione.
 * Coordina i flussi di dati tra l'interfaccia grafica (GUI) e le interfacce DAO.
 */
public class Controller {
    private final UtenteDAO utenteDAO;
    private final RepartoDAO repartoDAO;
    private final StanzaDAO stanzaDAO;
    private final LettoDAO lettoDAO;
    private final TurnoLavorativoDAO turnoLavorativoDAO;
    private final PrestazioneDAO prestazioneDAO;
    private final RicoveroDAO ricoveroDAO;

    private String usernameLoggato;

    /**
     * Costruttore del Controller. Inizializza le implementazioni concrete dei DAO
     * per la comunicazione persistente con il database PostgreSQL.
     */
    public Controller() {
        this.utenteDAO = new PostgresUtenteDAO();
        this.repartoDAO = new PostgresRepartoDAO();
        this.stanzaDAO = new PostgresStanzaDAO();
        this.lettoDAO = new PostgresLettoDAO();
        this.turnoLavorativoDAO = new PostgresTurnoLavorativoDAO();
        this.prestazioneDAO = new PostgresPrestazioneDAO();
        this.ricoveroDAO = new PostgresRicoveroDAO();
    }

    /**
     * Esegue la verifica delle credenziali d'accesso dell'utente nel sistema.
     * @param username Lo username inserito nella schermata di login.
     * @param password La password inserita nella schermata di login.
     * @return String Il ruolo dell'utente ("AMMINISTRATORE", "MEDICO") o codice "ERRORE".
     */
    public String verificaLogin(String username, String password) {
        Utente utenteLoggato = utenteDAO.login(username, password);
        if (utenteLoggato == null) {
            return "ERRORE";
        }
        this.usernameLoggato = username;
        if (utenteLoggato instanceof Amministratore) {
            return "AMMINISTRATORE";
        } else if (utenteLoggato instanceof Medico) {
            return "MEDICO";
        }
        return "Errore";
    }

    /**
     * Recupera la lista di tutti i reparti presenti nella struttura ospedaliera.
     * @return List&lt;Reparto&gt; Elenco completo dei reparti.
     */
    public List<Reparto> ottieniTuttiIReparti() {
        return repartoDAO.ottieniTuttiIReparti();
    }

    /**
     * Recupera le stanze associate a uno specifico reparto ospedaliero.
     * @param codiceReparto L'identificativo univoco del reparto.
     * @return List&lt;Stanza&gt; Elenco delle stanze appartenenti al reparto.
     */
    public List<Stanza> ottieniStanzePerReparto(int codiceReparto) {
        return stanzaDAO.ottieniStanzePerReparto(codiceReparto);
    }

    /**
     * Recupera i letti censiti all'interno di una determinata stanza.
     * @param numeroStanza Il numero identificativo della stanza.
     * @return List&lt;Letto&gt; Elenco dei letti presenti nella stanza.
     */
    public List<Letto> ottieniLettiPerStanza(int numeroStanza) {
        return lettoDAO.ottieniLettiPerStanza(numeroStanza);
    }

    /**
     * Genera il report testuale dei turni settimanali programmati per il medico loggato.
     * @return String Testo formattato contenente i giorni e le fasce orarie lavorative.
     */
    public String ottieniAgendaMedico() {
        if (usernameLoggato == null) {
            return "Errore: Nessun medico autenticato nella sessione corrente.";
        }
        List<TurnoLavorativo> turni = turnoLavorativoDAO.ottieniTurniPerMedico(usernameLoggato);
        if (turni.isEmpty()) {
            return "Non sono presenti turni lavorativi registrati per questa settimana.";
        }
        StringBuilder reportAgenda = new StringBuilder();
        reportAgenda.append("--- AGENDA TURNI SETTIMANALI ---\n\n");
        for (TurnoLavorativo t : turni) {
            reportAgenda.append("📅 ").append(t.getGiorno().toUpperCase())
                    .append(" 🕒 Fascia: ").append(t.getFasciaOraria())
                    .append("\n");
        }
        return reportAgenda.toString();
    }

    /**
     * Registra una prestazione medica effettuando i controlli di coerenza temporale
     * rispetto alle date di degenza del ricovero e all'orario di turno del medico.
     * @param data La data di esecuzione della prestazione.
     * @param tipo Il tipo di prestazione eseguita (es. visita, intervento).
     * @param orario L'orario specifico di erogazione della prestazione.
     * @param esito L'esito clinico o referto della prestazione.
     * @param idRicovero L'identificativo del ricovero del paziente associato.
     * @return String "OK" in caso di esito positivo, altrimenti una stringa di errore specifica.
     */
    public String aggiungiPrestazione(String data, String tipo, String orario, String esito, int idRicovero) {
        if (usernameLoggato == null) {
            return "NO_SESSIONE";
        }

        LocalDate dataPrestazione;
        try {
            DateTimeFormatter formatoItaliano = DateTimeFormatter.ofPattern("[dd-MM-yyyy][dd/MM/yyyy][dd-MM-yy][dd/MM/yy]");
            dataPrestazione = LocalDate.parse(data, formatoItaliano);
        } catch (Exception e) {
            return "FORMATO_DATA_KO";
        }

        LocalTime orarioPrestazione;
        try {
            String orarioPulito = orario.trim();
            if (!orarioPulito.contains(":")) {
                if (orarioPulito.length() == 1) orarioPulito = "0" + orarioPulito;
                orarioPulito += ":00";
            }
            if (orarioPulito.indexOf(":") == 1) {
                orarioPulito = "0" + orarioPulito;
            }
            orarioPrestazione = LocalTime.parse(orarioPulito);
        } catch (Exception e) {
            return "FORMATO_ORARIO_KO";
        }

        Ricovero ricoveroAssociato = ricoveroDAO.ottieniRicoveroPerId(idRicovero);
        if (ricoveroAssociato == null) {
            return "RICOVERO_INESISTENTE";
        }

        LocalDate inizioRicovero = LocalDate.parse(ricoveroAssociato.getDataInizio());
        LocalDate fineRicovero = (ricoveroAssociato.getDataFine() != null) ? LocalDate.parse(ricoveroAssociato.getDataFine()) : null;

        if (dataPrestazione.isBefore(inizioRicovero) || (fineRicovero != null && dataPrestazione.isAfter(fineRicovero))) {
            return "DATA_FUORI_RICOVERO";
        }

        List<TurnoLavorativo> turniMedico = turnoLavorativoDAO.ottieniTurniPerMedico(usernameLoggato);
        String giornoSettimana = dataPrestazione.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALIAN);

        boolean turnoValidoTrovato = false;
        for (TurnoLavorativo turno : turniMedico) {
            String turnoDB = turno.getGiorno().toUpperCase();
            String giornoCercato = giornoSettimana.toUpperCase();

            // MODIFICATO: Confronto parziale sulle prime 3 lettere per evitare i conflitti di accenti o di codifica
            if (turnoDB.startsWith(giornoCercato.substring(0, 3))) {
                String[] partiFascia = turno.getFasciaOraria().split(" - ");
                if (partiFascia.length == 2) {
                    LocalTime inizioTurno = LocalTime.parse(partiFascia[0].trim());
                    LocalTime fineTurno = LocalTime.parse(partiFascia[1].trim());

                    if (!orarioPrestazione.isBefore(inizioTurno) && !orarioPrestazione.isAfter(fineTurno)) {
                        turnoValidoTrovato = true;
                        break;
                    }
                }
            }
        }

        if (!turnoValidoTrovato) {
            return "FUORI_TURNO";
        }

        boolean giaImpegnato = prestazioneDAO.verificaPrestazioneSovrapposta(usernameLoggato, dataPrestazione.toString(), orarioPrestazione.toString());
        if (giaImpegnato) {
            return "SOVRAPPOSIZIONE_MEDICO";
        }

        Prestazione nuovaPrestazione = new Prestazione(tipo, dataPrestazione.toString(), orarioPrestazione.toString(), esito, idRicovero, usernameLoggato);
        boolean successo = prestazioneDAO.aggiungiPrestazione(nuovaPrestazione);
        return successo ? "OK" : "ERRORE_DB";
    }

    /**
     * Ottiene l'elenco di tutte le prestazioni erogate dal medico correntemente autenticato.
     * @return List&lt;Prestazione&gt; Elenco delle prestazioni del medico loggato.
     */
    public List<Prestazione> ottieniPrestazioniMedicoLoggato() {
        if (usernameLoggato == null) return new java.util.ArrayList<>();
        return prestazioneDAO.ottieniPrestazioniPerMedico(usernameLoggato);
    }

    /**
     * Modifica l'esito clinico descrittivo di una determinata prestazione già registrata.
     * @param idRicovero ID del ricovero associato alla prestazione.
     * @param data Data di esecuzione della prestazione.
     * @param orario Orario di esecuzione della prestazione.
     * @param nuovoEsito Il nuovo testo dell'esito clinico da memorizzare.
     * @return boolean True se la modifica sul database è andata a buon fine, false altrimenti.
     */
    public boolean modificaEsitoPrestazione(int idRicovero, String data, String orario, String nuovoEsito) {
        return prestazioneDAO.aggiornaEsitoPrestazione(idRicovero, data, orario, nuovoEsito);
    }

    /**
     * Registra un nuovo ricovero ospedaliero verificando preventivamente che il posto
     * letto selezionato risulti libero nell'intervallo di tempo richiesto.
     * @param cfPaziente Codice Fiscale del paziente da ricoverare.
     * @param nome Nome del paziente.
     * @param cognome Cognome del paziente.
     * @param idLetto ID univoco del letto da assegnare.
     * @param dataInizio Data di inizio degenza.
     * @param dataFine Data programmata per le dimissioni cliniche.
     * @return String "OK" se registrato con successo, o testo descrittivo dei letti alternativi liberi.
     */
    public String registraRicovero(String cfPaziente, String nome, String cognome, int idLetto, String dataInizio, String dataFine) {
        LocalDate inizio;
        LocalDate fine;

        try {
            DateTimeFormatter formatoItaliano = DateTimeFormatter.ofPattern("[dd-MM-yyyy][dd/MM/yyyy][dd-MM-yy][dd/MM/yy][yyyy-MM-dd]");
            inizio = LocalDate.parse(dataInizio, formatoItaliano);
            fine = LocalDate.parse(dataFine, formatoItaliano);
        } catch (Exception e) {
            return "FORMATO_NON_VALIDO";
        }

        if (fine.isBefore(inizio)) {
            return "DATA_FINE_ANTECEDENTE";
        }

        Letto lettoScelto = lettoDAO.ottieniLettoPerId(idLetto);
        if (lettoScelto == null) {
            return "LETTO_INESISTENTE";
        }

        boolean haSovrapposizioni = ricoveroDAO.verificaLettoOccupatoNelleDate(idLetto, inizio.toString(), fine.toString());
        if (haSovrapposizioni) {
            StringBuilder alternative = new StringBuilder();
            alternative.append("❌ Il letto ").append(idLetto).append(" è già impegnato in queste date.\n");
            alternative.append("Ecco i posti letto attualmente disponibili nell'ospedale:\n\n");

            List<Reparto> reparti = repartoDAO.ottieniTuttiIReparti();
            boolean ciSonoLettiLiberi = false;

            for (Reparto r : reparti) {
                List<Stanza> stanze = stanzaDAO.ottieniStanzePerReparto(r.getIdReparto());
                for (Stanza s : stanze) {
                    List<Letto> letti = lettoDAO.ottieniLettiPerStanza(s.getNumeroStanza());
                    for (Letto l : letti) {
                        if (!l.isOccupato()) {
                            alternative.append("🛏️ Letto ID: ").append(l.getIdLetto())
                                    .append(" -> Stanza ").append(s.getNumeroStanza())
                                    .append(" (").append(r.getNome()).append(")\n");
                            ciSonoLettiLiberi = true;
                        }
                    }
                }
            }

            if (!ciSonoLettiLiberi) {
                alternative.append("⚠️ Al momento non ci sono altri letti liberi in tutta la struttura.");
            }
            return alternative.toString();
        }

        Ricovero nuovoRicovero = new Ricovero(inizio.toString(), fine.toString(), cfPaziente, idLetto);
        boolean successo = ricoveroDAO.registraNuovoRicovero(nuovoRicovero, nome, cognome);
        return successo ? "OK" : "ERRORE_DB";
    }

    /**
     * Verifica i ricoveri per generare un report strutturato dei
     * pazienti la cui dimissione coincide con la data specificata in input.
     * @param dataInput La stringa della data da monitorare (es. GG-MM-AAAA).
     * @return String Report formattato dello scadenziario o stringa d'errore formato "FORMATO_KO".
     */
    public String ottieniReportDimissioniData(String dataInput) {
        LocalDate dataRicerca;
        try {
            DateTimeFormatter formatoItaliano = DateTimeFormatter.ofPattern("[dd-MM-yyyy][dd/MM/yyyy][dd-MM-yy][dd/MM/yy][yyyy-MM-dd]");
            dataRicerca = LocalDate.parse(dataInput, formatoItaliano);
        } catch (Exception e) {
            return "FORMATO_KO";
        }

        List<Ricovero> inScadenza = ricoveroDAO.ottieniRicoveriInScadenza(dataRicerca.toString());

        if (inScadenza.isEmpty()) {
            return "🟢 Nessun paziente in scadenza di dimissione per la data: " + dataInput;
        }

        StringBuilder report = new StringBuilder();
        report.append("📋 PAZIENTI IN DIMISSIONE IN DATA ").append(dataInput).append(":\n");
        report.append("--------------------------------------------------\n\n");
        for (Ricovero r : inScadenza) {
            report.append("👤 Paziente (CF): ").append(r.getCfPaziente()).append("\n")
                    .append("🛏️ Letto Assegnato ID: ").append(r.getIdLetto()).append("\n")
                    .append("📅 Ricoverato dal: ").append(r.getDataInizio()).append("\n")
                    .append("--------------------------------------------------\n");
        }
        return report.toString();
    }
}