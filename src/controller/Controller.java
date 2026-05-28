package controller;

public class Controller {

    public Controller() {
        // Costruttore vuoto: non dobbiamo più inizializzare liste complesse
    }

    // --- METODI PER IL LOGIN ---

    public String verificaLogin(String username, String password) {
        // Dati fittizi (hardcoded) per far funzionare la GUI
        if (username.equals("admin") && password.equals("123")) {
            return "AMMINISTRATORE";
        } else if (username.equals("medico") && password.equals("123")) {
            return "MEDICO";
        }
        return "ERRORE"; // Se si sbaglia password
    }


    // --- METODI PER IL MEDICO ---

    public String getAgendaFittizia() {
        // Restituisce un testo finto da mostrare nella schermata del Medico
        return "Lunedì: 08:00 - 14:00 (Turno in Reparto)\n" +
                "Martedì: 10:00 - 12:00 (Intervento Chirurgico)\n" +
                "Giovedì: 14:00 - 20:00 (Visite di controllo)";
    }

    public boolean aggiungiPrestazioneFittizia(String data, String tipo) {
        // Fa finta che l'inserimento della prestazione sia andato a buon fine
        return true;
    }


    // --- METODI PER L'AMMINISTRATORE ---

    public String getLettiFittizi(String reparto) {
        // Restituisce una lista finta di letti da mostrare all'Amministratore
        if (reparto.equalsIgnoreCase("Cardiologia")) {
            return "Letto 1A: Libero\nLetto 1B: Occupato (Paziente: Mario Rossi)\nLetto 2A: Libero";
        } else if (reparto.equalsIgnoreCase("Chirurgia")) {
            return "Letto 3A: Occupato (Paziente: Luigi Verdi)\nLetto 3B: Libero";
        }
        return "Nessun dato disponibile per questo reparto.";
    }

    public boolean registraRicoveroFittizio(String nomePaziente, String letto) {
        // Fa finta che il ricovero sia andato a buon fine
        return true;
    }
}