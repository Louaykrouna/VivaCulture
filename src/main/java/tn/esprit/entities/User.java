package tn.esprit.entities;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty prenom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final IntegerProperty numTel = new SimpleIntegerProperty();
    private final StringProperty sexe = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty companyName = new SimpleStringProperty();
    private final StringProperty matricule = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty image = new SimpleStringProperty();
    private final StringProperty organizerRequestStatus = new SimpleStringProperty();
    private final BooleanProperty isBanned = new SimpleBooleanProperty();

    public User() {
        this.isBanned.set(false); // Default value
    }

    public User(int id, String nom, String prenom, String email, String password, int numTel,
                String sexe, String address, String companyName, String matricule,
                String role, String status, String image, String organizerRequestStatus, boolean isBanned) {
        setId(id);
        setNom(nom);
        setPrenom(prenom);
        setEmail(email);
        setPassword(password);
        setNumTel(numTel);
        setSexe(sexe);
        setAddress(address);
        setCompanyName(companyName);
        setMatricule(matricule);
        setRole(role);
        setStatus(status);
        setImage(image);
        setOrganizerRequestStatus(organizerRequestStatus);
        setBanned(isBanned);
    }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty prenomProperty() { return prenom; }
    public StringProperty emailProperty() { return email; }
    public StringProperty passwordProperty() { return password; }
    public IntegerProperty numTelProperty() { return numTel; }
    public StringProperty sexeProperty() { return sexe; }
    public StringProperty addressProperty() { return address; }
    public StringProperty companyNameProperty() { return companyName; }
    public StringProperty matriculeProperty() { return matricule; }
    public StringProperty roleProperty() { return role; }
    public StringProperty statusProperty() { return status; }
    public StringProperty imageProperty() { return image; }
    public StringProperty organizerRequestStatusProperty() { return organizerRequestStatus; }
    public BooleanProperty isBannedProperty() { return isBanned; }

    // Getters and setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }

    public String getPrenom() { return prenom.get(); }
    public void setPrenom(String prenom) { this.prenom.set(prenom); }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }

    public String getPassword() { return password.get(); }
    public void setPassword(String password) { this.password.set(password); }

    public int getNumTel() { return numTel.get(); }
    public void setNumTel(int numTel) { this.numTel.set(numTel); }

    public String getSexe() { return sexe.get(); }
    public void setSexe(String sexe) { this.sexe.set(sexe); }

    public String getAddress() { return address.get(); }
    public void setAddress(String address) { this.address.set(address); }

    public String getCompanyName() { return companyName.get(); }
    public void setCompanyName(String companyName) { this.companyName.set(companyName); }

    public String getMatricule() { return matricule.get(); }
    public void setMatricule(String matricule) { this.matricule.set(matricule); }

    public String getRole() { return role.get(); }
    public void setRole(String role) { this.role.set(role); }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }

    public String getImage() { return image.get(); }
    public void setImage(String image) { this.image.set(image); }

    public String getOrganizerRequestStatus() { return organizerRequestStatus.get(); }
    public void setOrganizerRequestStatus(String organizerRequestStatus) { this.organizerRequestStatus.set(organizerRequestStatus); }

    public boolean isBanned() { return isBanned.get(); }
    public void setBanned(boolean banned) { this.isBanned.set(banned); }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", numTel=" + getNumTel() +
                ", sexe='" + getSexe() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", companyName='" + getCompanyName() + '\'' +
                ", matricule='" + getMatricule() + '\'' +
                ", role='" + getRole() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", image='" + getImage() + '\'' +
                ", organizerRequestStatus='" + getOrganizerRequestStatus() + '\'' +
                ", isBanned=" + isBanned() +
                '}';
    }
}