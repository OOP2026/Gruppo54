package model;

import java.util.ArrayList;
import java.util.List;

public class Prestazione {
    private String tipoPrestazione; // Es. "Visita" o "Intervento"
    private String data;
    private String orario;
    private String esito;

    // Relazione N:M con Medico (l'équipe medica)
    private List<Medico> mediciCoinvolti;

    public Prestazione(String tipoPrestazione, String data, String orario) {
        this.tipoPrestazione = tipoPrestazione;
        this.data = data;
        this.orario = orario;
        // Inizializziamo la lista vuota nel costruttore
        this.mediciCoinvolti = new ArrayList<>();
    }

    // Getter e Setter classici
    public String getTipoPrestazione() { return tipoPrestazione; }
    public void setTipoPrestazione(String tipoPrestazione) { this.tipoPrestazione = tipoPrestazione; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getOrario() { return orario; }
    public void setOrario(String orario) { this.orario = orario; }

    public String getEsito() { return esito; }
    public void setEsito(String esito) { this.esito = esito; }

    // Getter e Setter per la lista dei medici
    public List<Medico> getMediciCoinvolti() { return mediciCoinvolti; }
    public void setMediciCoinvolti(List<Medico> mediciCoinvolti) { this.mediciCoinvolti = mediciCoinvolti; }

    // Metodo comodo per aggiungere un medico all'équipe
    public void addMedico(Medico m) {
        this.mediciCoinvolti.add(m);
    }
}