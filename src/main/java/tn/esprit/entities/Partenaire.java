package tn.esprit.entities;

public class Partenaire {

    private int id;
    private String idP;
    private String nomP;
    private String emailP;
    private String telephone;
    private String typeP;
    private int collaboration_id;

    public Partenaire() {
    }



    public Partenaire( String idP, String nomP, String emailP, String telephone, String typeP) {
        this.idP = idP;
        this.nomP = nomP;
        this.emailP = emailP;
        this.telephone = telephone;
        this.typeP = typeP;

    }

    public Partenaire(int id, int collaboration_id, String idP, String typeP, String nomP, String telephone, String emailP) {
        this.id = id;
        this.collaboration_id = collaboration_id;
        this.idP = idP;
        this.typeP = typeP;
        this.nomP = nomP;
        this.telephone = telephone;
        this.emailP = emailP;
    }

    public Partenaire(String nomP, String emailP, String telephone, String typeP) {
    }

    // Getters & Setters

    public int getcollaboration_id() {
        return collaboration_id;
    }

    public void setcollaboration_id(int collaboration_id) {
        this.collaboration_id = collaboration_id;
    }

    @Override
    public String toString() {
        return "Partenaire{" +
                "id=" + id +
                ", idP='" + idP + '\'' +
                ", nomP='" + nomP + '\'' +
                ", emailP='" + emailP + '\'' +
                ", telephone='" + telephone + '\'' +
                ", typeP='" + typeP + '\'' +
                ", id_c=" + collaboration_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }

    public String getNomP() {
        return nomP;
    }

    public void setNomP(String nomP) {
        this.nomP = nomP;
    }

    public String getEmailP() {
        return emailP;
    }

    public void setEmailP(String emailP) {
        this.emailP = emailP;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTypeP() {
        return typeP;
    }

    public void setTypeP(String typeP) {
        this.typeP = typeP;
    }


}
