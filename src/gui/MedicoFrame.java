package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MedicoFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton btnMostraAgenda;
    private JTextArea txtAgenda;
    private JTextField txtData;
    private JTextField txtTipoVisita;
    private JButton btnSalvaPrestazione;
    private Controller controller;

    public MedicoFrame(Controller controller) {
        this.controller = controller;

        setContentPane(mainPanel);
        setTitle("Dashboard Medico");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        btnMostraAgenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String turni = controller.getAgendaFittizia();
                txtAgenda.setText(turni);
            }
        });
        btnSalvaPrestazione.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = txtData.getText();
                String tipo = txtTipoVisita.getText();

                // Chiama il metodo finto per salvare
                boolean salvato = controller.aggiungiPrestazioneFittizia(data, tipo);

                if (salvato) {
                    JOptionPane.showMessageDialog(mainPanel, "Prestazione registrata con successo!");
                    txtData.setText("");
                    txtTipoVisita.setText("");
                }
            }
        });
    }
}