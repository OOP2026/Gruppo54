import controller.Controller;
import gui.LoginFrame;

/**
 * Classe di ingresso principale (Entry Point) dell'applicazione di gestione ospedaliera.
 * Si occupa di inizializzare il Controller centrale e di avviare la schermata di login iniziale.
 */
public class Main {

    /**
     * Metodo principale eseguito all'avvio del programma.
     *
     * @param args Argomenti passati da riga di comando (non utilizzati nell'applicazione).
     */
    public static void main(String[] args) {
        // Inizializzazione del core business logico (Controller)
        Controller controller = new Controller();

        // Inizializzazione e apertura dell'interfaccia grafica di autenticazione
        LoginFrame login = new LoginFrame(controller);
        login.setVisible(true);
    }
}