package model;

import java.util.List;
import java.util.ArrayList;

public class Stanza {
    private String numeroStanza;
    private List<Letto> letti; // La stanza contiene una lista di letti

    public Stanza(String numeroStanza) {
        this.numeroStanza = numeroStanza;
        this.letti = new ArrayList<>(); // Inizializza la lista vuota
    }

    public String getNumeroStanza() { return numeroStanza; }
    public void setNumeroStanza(String numeroStanza) { this.numeroStanza = numeroStanza; }

    public List<Letto> getLetti() { return letti; }

    // Metodo per aggiungere un letto alla stanza
    public void addLetto(Letto letto) {
        this.letti.add(letto);
    }
}