package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminFrame extends JFrame{
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTextField txtNomePaziente;
    private JTextField txtLetto;
    private JButton btnSalvaRicovero;
    private JTextField txtReparto;
    private JButton btnCercaLetti;
    private JTextArea txtRisultatiLetti;
    private Controller controller;

    public AdminFrame(Controller controller){
        this.controller=controller;
        setContentPane(mainPanel);
        setTitle("Dashboard Amministratore");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        btnCercaLetti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String repartoScelto= txtReparto.getText();
                String listaLetti= controller.getLettiFittizi(repartoScelto);
                txtRisultatiLetti.setText(listaLetti);

            }
        });
        btnSalvaRicovero.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String paziente = txtNomePaziente.getText();
                String letto = txtLetto.getText();
                boolean salvato= controller.registraRicoveroFittizio(paziente,letto);
                if(salvato){
                    JOptionPane.showMessageDialog(mainPanel,"Ricovero Registrato con successo per "+ paziente);
                    txtNomePaziente.setText("");
                    txtLetto.setText("");
                } else {
                    JOptionPane.showMessageDialog(mainPanel,"ERRORE: impossibile registrare ricovero","Errore di Salvataggio",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
