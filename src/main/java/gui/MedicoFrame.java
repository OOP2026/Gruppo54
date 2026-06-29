package gui;

import controller.Controller;
import model.Prestazione;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Finestra principale dell'interfaccia grafica (GUI) dedicata al Medico.
 * Consenterà la consultazione della propria agenda di turni settimanali, l'inserimento
 * controllato di nuove prestazioni cliniche (previa verifica dei vincoli temporali)
 * e la gestione tramite griglia interattiva del registro delle prestazioni erogate.
 */
public class MedicoFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton btnMostraAgenda;
    private JTextArea txtAgenda;
    private JTextField txtData;
    private JTextField txtTipoVisita;
    private JButton btnSalvaPrestazione;

    // RISOLTO CODE SMELL: Applicato modificatore 'final' come richiesto dall'analisi statica
    private final Controller controller;

    // Componenti aggiunte via codice per la gestione ed editing delle prestazioni
    private JTable tabellaPrestazioni;
    private DefaultTableModel tableModel;
    private JButton btnRinfrescaPrestazioni;
    private JButton btnModificaEsito;

    /**
     * Costruttore della dashboard del Medico.
     * Configura l'ambiente di visualizzazione Swing e aggancia la logica dei listener.
     *
     * @param controller Il Controller centrale deputato al coordinamento dei flussi informativi.
     */
    public MedicoFrame(Controller controller) {
        this.controller = controller;

        setContentPane(mainPanel);
        setTitle("Dashboard Medico");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Costruzione dinamica del pannello tabulare per il registro delle prestazioni
        inizializzaTabGestionePrestazioni();

        // RISOLTO CODE SMELL: Sostituito Anonymous ActionListener con Lambda Expression
        btnMostraAgenda.addActionListener(e -> {
            String turni = this.controller.ottieniAgendaMedico();
            txtAgenda.setText(turni);
        });

        // RISOLTO CODE SMELL: Sostituito Anonymous ActionListener con Lambda Expression
        btnSalvaPrestazione.addActionListener(e -> {
            String data = txtData.getText().trim();
            // MODIFICATO: Normalizzazione automatica in maiuscolo per facilitare la validazione e il matching nel DB
            String tipo = txtTipoVisita.getText().trim().toUpperCase();

            if (data.isEmpty() || tipo.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Data e Tipo Prestazione sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // NUOVO VINCOLO DI DOMINIO: Intercettazione preventiva dell'input sul client GUI prima dell'invio al Controller
            if (!tipo.equals("VISITA") && !tipo.equals("INTERVENTO")) {
                JOptionPane.showMessageDialog(mainPanel,
                        "⚠️ Il Tipo Prestazione non è valido!\nPuoi inserire esclusivamente: 'VISITA' o 'INTERVENTO'.",
                        "Errore Input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String orario = JOptionPane.showInputDialog(mainPanel, "Inserisci l'orario della prestazione (es. 11:30):");
            if (orario == null || orario.trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Orario obbligatorio!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            orario = orario.trim();

            String idRicoveroStr = JOptionPane.showInputDialog(mainPanel, "Inserisci l'ID del Ricovero del paziente:");
            if (idRicoveroStr == null || idRicoveroStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "ID Ricovero obbligatorio!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String esito = JOptionPane.showInputDialog(mainPanel, "Inserisci l'esito della prestazione:");
            if (esito == null) esito = "";

            try {
                int idRicovero = Integer.parseInt(idRicoveroStr.trim());

                String esitoAggiunta = this.controller.aggiungiPrestazione(data, tipo, orario, esito, idRicovero);

                // RISOLTO CODE SMELL: Sostituzione della catena complessa di 'if-else' con un costrutto 'switch'
                switch (esitoAggiunta) {
                    case "OK":
                        JOptionPane.showMessageDialog(mainPanel, "✅ Prestazione registrata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        txtData.setText("");
                        txtTipoVisita.setText("");
                        aggiornaTabellaPrestazioni();
                        break;
                    case "FORMATO_DATA_KO":
                        JOptionPane.showMessageDialog(mainPanel, "⚠️ Formato data non valido. Usa GG-MM-AAAA.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "FORMATO_ORARIO_KO":
                        JOptionPane.showMessageDialog(mainPanel, "⚠️ Formato orario non valido. Usa HH:MM.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "RICOVERO_INESISTENTE":
                        JOptionPane.showMessageDialog(mainPanel, "⚠️ L'ID Ricovero inserito non esiste nel database.", "Errore Validazione", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "DATA_FUORI_RICOVERO":
                        JOptionPane.showMessageDialog(mainPanel, "❌ ERRORE CLINICO: Il paziente non risulta ricoverato in questa data!", "Errore Cronologia", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "FUORI_TURNO":
                        JOptionPane.showMessageDialog(mainPanel, "❌ ERRORE: Non hai turni lavorativi programmati per questo giorno/orario.", "Fuori Turno", JOptionPane.ERROR_MESSAGE);
                        break;
                    case "SOVRAPPOSIZIONE_MEDICO":
                        JOptionPane.showMessageDialog(mainPanel, "❌ ERRORE: Hai già una prestazione registrata in questo esatto momento!", "Conflitto Appuntamenti", JOptionPane.WARNING_MESSAGE);
                        break;
                    default:
                        JOptionPane.showMessageDialog(mainPanel, "❌ Errore nel salvataggio sul database.", "Errore DB", JOptionPane.ERROR_MESSAGE);
                        break;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "L'ID del ricovero deve essere un numero intero valido!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Configura la struttura tabulare del modulo di gestione e storicizzazione delle prestazioni.
     */
    private void inizializzaTabGestionePrestazioni() {
        JPanel pannelloGestione = new JPanel(new BorderLayout());

        String[] colonne = {"ID Ricovero", "Data", "Orario", "Tipo", "Esito"};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabellaPrestazioni = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabellaPrestazioni);
        pannelloGestione.add(scrollPane, BorderLayout.CENTER);

        JPanel pannelloBottoni = new JPanel(new FlowLayout());
        btnRinfrescaPrestazioni = new JButton("🔄 Aggiorna Elenco");
        btnModificaEsito = new JButton("📝 Modifica Esito Selezionato");

        pannelloBottoni.add(btnRinfrescaPrestazioni);
        pannelloBottoni.add(btnModificaEsito);
        pannelloGestione.add(pannelloBottoni, BorderLayout.SOUTH);

        tabbedPane1.addTab("Registro Prestazioni Erogate", pannelloGestione);

        btnRinfrescaPrestazioni.addActionListener(e -> aggiornaTabellaPrestazioni());

        btnModificaEsito.addActionListener(e -> {
            int rigaSelezionata = tabellaPrestazioni.getSelectedRow();
            if (rigaSelezionata == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona prima una riga dalla tabella!", "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idRicovero = (int) tableModel.getValueAt(rigaSelezionata, 0);
            String data = tableModel.getValueAt(rigaSelezionata, 1).toString();
            String orario = tableModel.getValueAt(rigaSelezionata, 2).toString();
            String vecchioEsito = tableModel.getValueAt(rigaSelezionata, 4).toString();

            String nuovoEsito = JOptionPane.showInputDialog(this, "Modifica l'esito della prestazione:", vecchioEsito);

            if (nuovoEsito != null) {
                boolean aggiornato = this.controller.modificaEsitoPrestazione(idRicovero, data, orario, nuovoEsito.trim());
                if (aggiornato) {
                    JOptionPane.showMessageDialog(this, "Esito aggiornato con successiva!");
                    aggiornaTabellaPrestazioni();
                } else {
                    JOptionPane.showMessageDialog(this, "Impossibile aggiornare l'esito.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Interroga il Controller per ripopolare dinamicamente i dati all'interno della griglia di visualizzazione.
     */
    private void aggiornaTabellaPrestazioni() {
        tableModel.setRowCount(0);
        List<Prestazione> prestazioni = this.controller.ottieniPrestazioniMedicoLoggato();

        for (Prestazione p : prestazioni) {
            Object[] riga = {
                    p.getIdRicovero(),
                    p.getData(),
                    p.getOrario(),
                    p.getTipoPrestazione().toUpperCase(),
                    p.getEsito()
            };
            tableModel.addRow(riga);
        }
    }
}