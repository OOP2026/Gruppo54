package model;

import java.util.ArrayList;
import java.util.List;

public class Medico extends Utente {

    // Relazione N:M con Prestazione
    private List<Prestazione> prestazioniEseguite;

    public Medico(String login, String password) {
        super(login, password);
        // Inizializziamo la lista vuota nel costruttore
        this.prestazioniEseguite = new ArrayList<>();
    }

    // Getter e Setter per la lista
    public List<Prestazione> getPrestazioniEseguite() {
        return prestazioniEseguite;
    }

    public void setPrestazioniEseguite(List<Prestazione> prestazioniEseguite) {
        this.prestazioniEseguite = prestazioniEseguite;
    }

    // Metodo comodo per aggiungere una singola prestazione al medico
    public void addPrestazione(Prestazione p) {
        this.prestazioniEseguite.add(p);
    }

    public void visualizzaAgenda() {
        // Implementazione futura
    }
}