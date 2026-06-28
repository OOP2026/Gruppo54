package gui;

import controller.Controller;
import javax.swing.*;

/**
 * Finestra di autenticazione iniziale dell'applicazione (Schermata di Login).
 * Gestisce l'acquisizione delle credenziali utente e ne delega la convalida al Controller,
 * instradando l'accesso verso la corretta interfaccia grafica (Admin o Medico)
 * in base al ruolo restituito dal sistema.
 */
public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnAccedi;

    // RISOLTO CODE SMELL: Aggiunto il modificatore 'final'
    private final Controller controller;

    /**
     * Costruttore della schermata di login.
     * Configura le proprietà della finestra e registra il listener per l'evento
     * di autenticazione sul pulsante di accesso.
     *
     * @param controller Il Controller centrale per il coordinamento dei flussi di business logic.
     */
    public LoginFrame(Controller controller){
        this.controller = controller;
        setContentPane(mainPanel);
        setTitle("Accesso al sistema ospedaliero");
        setSize(400,300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // RISOLTO CODE SMELL: Sostituito l'Anonymous ActionListener con una Lambda Expression
        btnAccedi.addActionListener(e -> {
            String utenteInserito = txtUsername.getText();
            String passwordInserita = new String(txtPassword.getPassword());

            String risposta = this.controller.verificaLogin(utenteInserito, passwordInserita);

            if (risposta.equals("AMMINISTRATORE")) {
                JOptionPane.showMessageDialog(mainPanel, "Benvenuto Amministratore, apro la dashboard...");
                AdminFrame adminDashboard = new AdminFrame(this.controller);
                adminDashboard.setVisible(true);
                dispose();
            }
            else if (risposta.equals("MEDICO")) {
                JOptionPane.showMessageDialog(mainPanel, "Accesso riuscito. Avvio Dashboard Medico...");

                // Crea la finestra del medico e le passa il controller
                MedicoFrame medicoDashboard = new MedicoFrame(this.controller);
                medicoDashboard.setVisible(true);

                dispose(); // Chiude la finestra di login corrente
            }
            else {
                JOptionPane.showMessageDialog(mainPanel, "CREDENZIALI ERRATE", "ERRORE", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}