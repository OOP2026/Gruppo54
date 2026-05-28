package model;

import java.util.List;
import java.util.ArrayList;

public class Reparto {
    private String codice;
    private List<Stanza> stanze; // Il reparto contiene una lista di stanze

    public Reparto(String codice) {
        this.codice = codice;
        this.stanze = new ArrayList<>();
    }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public List<Stanza> getStanze() { return stanze; }

    // Metodo per aggiungere una stanza al reparto
    public void addStanza(Stanza stanza) {
        this.stanze.add(stanza);
    }
}