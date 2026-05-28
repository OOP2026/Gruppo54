package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnAccedi;
    private Controller controller;
    public LoginFrame(Controller controller){
        this.controller=controller;
        setContentPane(mainPanel);
        setTitle("Accesso al sistema ospedaliero");
        setSize(400,300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        btnAccedi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String utenteInserito = txtUsername.getText();
                String passwordInserita = new String(txtPassword.getPassword());
                String risposta = controller.verificaLogin(utenteInserito,passwordInserita);
                if (risposta.equals("AMMINISTRATORE")){
                    JOptionPane.showMessageDialog(mainPanel,"Benvenuto Amministratore, apro la dashboard...");
                    AdminFrame adminDashboard=new AdminFrame(controller);
                    adminDashboard.setVisible(true);
                    dispose();
                }
                else if (risposta.equals("MEDICO")) {
                    JOptionPane.showMessageDialog(mainPanel, "Accesso riuscito. Avvio Dashboard Medico...");

                    // Crea la finestra di Antonio e le passa il controller
                    MedicoFrame medicoDashboard = new MedicoFrame(controller);
                    medicoDashboard.setVisible(true);

                    dispose(); // Chiude il login
                } else{
                    JOptionPane.showMessageDialog(mainPanel,"CREDENZIALI ERRATE","ERRORE",JOptionPane.ERROR_MESSAGE);
                }


            }
        });
    }
}
