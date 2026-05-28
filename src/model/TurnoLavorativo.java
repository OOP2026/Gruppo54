package model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TurnoLavorativo {
    private DayOfWeek giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Medico medico; // Il medico a cui è assegnato il turno

    public TurnoLavorativo(DayOfWeek giorno, LocalTime oraInizio, LocalTime oraFine, Medico medico) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.medico = medico;
    }

    public DayOfWeek getGiorno() { return giorno; }
    public void setGiorno(DayOfWeek giorno) { this.giorno = giorno; }

    public LocalTime getOraInizio() { return oraInizio; }
    public void setOraInizio(LocalTime oraInizio) { this.oraInizio = oraInizio; }

    public LocalTime getOraFine() { return oraFine; }
    public void setOraFine(LocalTime oraFine) { this.oraFine = oraFine; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
}