package tn.esprit.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Collaboration {

    private int id;
    private String nomC;
    private String type;
    private LocalDate dateSig;
    private LocalDate dateEx;
    private String status;

    private List<Partenaire> partenaires;

    public Collaboration() {
        this.partenaires = new ArrayList<>();
    }

    // Corrected constructor
    public Collaboration(int id, String nomC, String type, LocalDate dateSig, LocalDate dateEx, String status) {
        this.id = id;
        this.nomC = nomC;
        this.type = type;
        this.dateSig = dateSig;
        this.dateEx = dateEx;
        this.status = status;
        this.partenaires = new ArrayList<>();
    }

    // Corrected constructor to properly initialize fields
    public Collaboration(String nomC, String type, LocalDate dateSig, LocalDate dateEx, String status) {
        this.nomC = nomC;
        this.type = type;
        this.dateSig = dateSig;
        this.dateEx = dateEx;
        this.status = status;
        this.partenaires = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateSig() {
        return dateSig;
    }

    public void setDateSig(LocalDate dateSig) {
        this.dateSig = dateSig;
    }

    public LocalDate getDateEx() {
        return dateEx;
    }

    public void setDateEx(LocalDate dateEx) {
        this.dateEx = dateEx;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Partenaire> getPartenaires() {
        return partenaires;
    }

    public void setPartenaires(List<Partenaire> partenaires) {
        this.partenaires = partenaires;
    }


    

    @Override
    public String toString() {
        return "Collaboration{" +
                "id=" + id +
                ", nomC='" + nomC + '\'' +
                ", type='" + type + '\'' +
                ", dateSig=" + dateSig +
                ", dateEx=" + dateEx +
                ", status='" + status + '\'' +
                ", partenaires=" + partenaires +
                '}';
    }
}
