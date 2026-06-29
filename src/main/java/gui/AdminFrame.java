package gui;

import controller.Controller;
import model.Letto;
import model.Reparto;
import model.Stanza;

import javax.swing.*;
import java.awt.*;

/**
 * Finestra principale dell'interfaccia grafica (GUI) dedicata all'Amministratore.
 * Fornisce i moduli per il monitoraggio in tempo reale dei posti letto ospedalieri,
 * la registrazione controllata dei nuovi ricoveri e la consultazione dello scadenziario dimissioni.
 */
public class AdminFrame extends JFrame {
    private JPanel mainPanel;

    // RISOLTO ERRORE BINDING: Ripristinato il campo grafico per l'editor .form con soppressione del warning
    @SuppressWarnings("unused")
    private JTabbedPane tabbedPane1;

    private JTextField txtNomePaziente;
    private JTextField txtLetto;
    private JButton btnSalvaRicovero;
    private JTextField txtReparto;
    private JButton btnCercaLetti;
    private JTextArea txtRisultatiLetti;

    // RISOLTI CODE SMELLS: Aggiunto il modificatore 'final' richiesto da SonarQube
    private final Controller controller;
    private final JButton btnDimissioniOggi;

    /**
     * Costruttore della dashboard dell'Amministratore.
     * Configura il layout di contenimento grafico, inizializza i componenti aggiuntivi
     * e registra i listener di eventi per le componenti di input.
     *
     * @param controller Il Controller centrale per l'inoltro delle richieste logiche.
     */
    public AdminFrame(Controller controller){
        this.controller = controller;

        // 1. Creiamo un pannello contenitore neutro con BorderLayout nativo
        JPanel rootPanel = new JPanel(new BorderLayout());

        // 2. Ci inseriamo dentro il tuo mainPanel grafico al centro per preservare il GridLayoutManager
        rootPanel.add(mainPanel, BorderLayout.CENTER);

        // 3. Impostiamo il pannello neutro come ContentPane del Frame
        setContentPane(rootPanel);

        setTitle("Dashboard Amministratore");
        setSize(520,480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 4. Ora possiamo aggiungere in totale sicurezza il bottone in basso al rootPanel
        btnDimissioniOggi = new JButton("📋 Controlla Scadenziario Dimissioni");
        btnDimissioniOggi.setBackground(new Color(230, 242, 255));
        rootPanel.add(btnDimissioniOggi, BorderLayout.SOUTH);

        // RISOLTO CODE SMELL: Sostituito Anonymous ActionListener con Lambda Expression
        btnCercaLetti.addActionListener(e -> {
            txtRisultatiLetti.setText("");

            String repartoScelto = txtReparto.getText().trim();

            StringBuilder report = new StringBuilder();
            report.append("--- SITUAZIONE OSPEDALE ---\n\n");

            java.util.List<model.Reparto> reparti = this.controller.ottieniTuttiIReparti();
            boolean repartoTrovato = false;

            for (model.Reparto r : reparti) {
                if (!repartoScelto.isEmpty() && !r.getNome().equalsIgnoreCase(repartoScelto)) {
                    continue;
                }

                repartoTrovato = true;
                report.append("🔹 REPARTO: ").append(r.getNome().toUpperCase()).append("\n");

                java.util.List<model.Stanza> stanze = this.controller.ottieniStanzePerReparto(r.getIdReparto());

                if (stanze.isEmpty()) {
                    report.append("   (Nessuna stanza registrata in questo reparto)\n\n");
                    continue;
                }

                for (model.Stanza s : stanze) {
                    report.append("   🏠 Stanza n° ").append(s.getNumeroStanza())
                            .append(" (Piano ").append(s.getPiano()).append("):\n");

                    java.util.List<model.Letto> letti = this.controller.ottieniLettiPerStanza(s.getNumeroStanza());

                    if (letti.isEmpty()) {
                        report.append("      [Nessun letto presente]\n");
                    }

                    for (model.Letto l : letti) {
                        String stato = l.isOccupato() ? "🔴 OCCUPATO" : "🟢 LIBERO";
                        report.append("      🛏️ Letto ").append(l.getNumeroLetto())
                                .append(" -> ").append(stato).append("\n");
                    }
                }
                report.append("\n----------------------------------------\n\n");
            }

            if (!repartoTrovato) {
                txtRisultatiLetti.setText("⚠️ Nessun reparto trovato con il nome: \"" + repartoScelto + "\"");
            } else {
                txtRisultatiLetti.setText(report.toString());
            }
        });

        // RISOLTO CODE SMELL: Sostituito Anonymous ActionListener con Lambda Expression
        btnSalvaRicovero.addActionListener(e -> {
            String inputPaziente = txtNomePaziente.getText().trim();
            String inputLetto = txtLetto.getText().trim();

            if (inputPaziente.isEmpty() || inputLetto.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel,
                        "⚠️ ATTENZIONE: Tutti i campi della schermata sono obbligatori.",
                        "Validazione Fallita", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] datiAnagrafici = inputPaziente.split(",");
            if (datiAnagrafici.length != 3) {
                JOptionPane.showMessageDialog(mainPanel,
                        "⚠️ FORMATO MANCANTE: Inserisci i dati del paziente nel formato:\nCodiceFiscale, Nome, Cognome",
                        "Errore Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cfPaziente = datiAnagrafici[0].trim();
            String nomePaziente = datiAnagrafici[1].trim();
            String cognomePaziente = datiAnagrafici[2].trim();

            String dataInizio = JOptionPane.showInputDialog(mainPanel,
                    "Inserisci la DATA INIZIO ricovero (Formato: GG-MM-AAAA o AAAA-MM-GG):",
                    "Data Inizio", JOptionPane.QUESTION_MESSAGE);
            if (dataInizio == null || dataInizio.trim().isEmpty()) return;

            String dataFine = JOptionPane.showInputDialog(mainPanel,
                    "Inserisci la DATA FINE ricovero prevista (Formato: GG-MM-AAAA o AAAA-MM-GG):",
                    "Data Fine Dimissione", JOptionPane.QUESTION_MESSAGE);
            if (dataFine == null || dataFine.trim().isEmpty()) return;

            try {
                int idLetto = Integer.parseInt(inputLetto);

                String risultato = this.controller.registraRicovero(
                        cfPaziente,
                        nomePaziente,
                        cognomePaziente,
                        idLetto,
                        dataInizio.trim(),
                        dataFine.trim()
                );

                // RISOLTO CODE SMELL: Sostituita la catena complessa di 'if' con un costrutto 'switch'
                switch (risultato) {
                    case "OK":
                        JOptionPane.showMessageDialog(mainPanel,
                                "✅ Ricovero registrato con successo nel sistema!",
                                "Operazione Riuscita", JOptionPane.INFORMATION_MESSAGE);
                        txtNomePaziente.setText("");
                        txtLetto.setText("");
                        break;
                    case "FORMATO_NON_VALIDO":
                        JOptionPane.showMessageDialog(mainPanel,
                                "⚠️ ERRORE: Formato date non valido. Usa il formato standard GG-MM-AAAA.",
                                "Errore Input", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "LETTO_INESISTENTE":
                        JOptionPane.showMessageDialog(mainPanel,
                                "⚠️ ERRORE: L'ID letto inserito non esiste nell'ospedale.",
                                "Errore Input", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "DATA_FINE_ANTECEDENTE":
                        JOptionPane.showMessageDialog(mainPanel,
                                "⚠️ ERRORE LOGICO: La data di dimissione non può essere precedente alla data di inizio ricovero!",
                                "Errore Cronologia Date", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "ERRORE_DB":
                        JOptionPane.showMessageDialog(mainPanel,
                                "❌ Errore interno nel salvataggio dei dati sul Database.",
                                "Errore Persistenza", JOptionPane.ERROR_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(mainPanel, risultato,
                                "Letto Occupato - Soluzioni Disponibili", JOptionPane.WARNING_MESSAGE);
                        break;
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel,
                        "⚠️ ERRORE FORMATO: L'ID del letto deve essere un valore numerico intero.",
                        "Errore Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // RISOLTO CODE SMELL: Sostituito Anonymous ActionListener con Lambda Expression
        btnDimissioniOggi.addActionListener(e -> {
            String dataOggi = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            String dataScelta = JOptionPane.showInputDialog(AdminFrame.this,
                    "Inserisci la data delle dimissioni da verificare (es. GG-MM-AAAA):",
                    dataOggi);

            if (dataScelta == null || dataScelta.trim().isEmpty()) {
                return;
            }

            String reportDimissioni = this.controller.ottieniReportDimissioniData(dataScelta.trim());

            if (reportDimissioni.equals("FORMATO_KO")) {
                JOptionPane.showMessageDialog(AdminFrame.this,
                        "⚠️ Formato data non valido. Controlla la sintassi dell'input.",
                        "Errore Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JTextArea textArea = new JTextArea(reportDimissioni);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(420, 260));

            JOptionPane.showMessageDialog(AdminFrame.this, scrollPane,
                    "Scadenziario Dimissioni - Data: " + dataScelta.trim(), JOptionPane.INFORMATION_MESSAGE);
        });
    }
}