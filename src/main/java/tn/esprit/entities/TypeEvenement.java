    package tn.esprit.entities;

    public class TypeEvenement {
        private int id;
        private int categorieId;
        private String nom;
        private String urlImage;
        private String description;
        private String categorieNom;


        // Constructeurs
        public TypeEvenement() {}

        public TypeEvenement(int id, int categorieId, String nom, String urlImage, String description) {
            this.id = id;
            this.categorieId = categorieId;
            this.nom = nom;
            this.urlImage = urlImage;
            this.description = description;
        }

        public TypeEvenement(int categorieId, String nom, String urlImage, String description) {
            this.categorieId = categorieId;
            this.nom = nom;
            this.urlImage = urlImage;
            this.description = description;
        }

        // Getters & Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getCategorieId() { return categorieId; }
        public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getUrlImage() { return urlImage; }
        public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getCategorieNom() {
            return categorieNom;
        }
        public void setCategorieNom(String categorieNom) {
            this.categorieNom = categorieNom;
        }
        @Override
        public String toString() {
            return "TypeEvenement{" +
                    "id=" + id +
                    ", categorieId=" + categorieId +
                    ", categorieNom='" + categorieNom + '\'' +
                    ", nom='" + nom + '\'' +
                    ", urlImage='" + urlImage + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }

    }
