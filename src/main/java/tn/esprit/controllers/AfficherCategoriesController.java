package tn.esprit.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.esprit.entities.Categorie;
import tn.esprit.services.ServiceCategorie;
import tn.esprit.utils.TranslationUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class AfficherCategoriesController implements Initializable {

    @FXML private VBox vboxCategories;
    @FXML private TextField tfFilter;
    @FXML private ComboBox<String> cbSort;
    @FXML private Button btnTranslate;
    private boolean isFrench = true;
    private final Map<Label, String> translationCache = new HashMap<>();

    private final ServiceCategorie serviceCategorie;
    private final ObservableList<Categorie> masterData = FXCollections.observableArrayList();

    public AfficherCategoriesController() {
        try {
            serviceCategorie = new ServiceCategorie();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) Initialiser le combo de tri AVANT d'appeler updateView()
        cbSort.setItems(FXCollections.observableArrayList(
                "Nom ↑", "Nom ↓", "Statut ↑", "Statut ↓"
        ));
        cbSort.getSelectionModel().selectFirst();

        // 2) Installer les écouteurs pour filtrer/tri dynamique
        tfFilter.textProperty().addListener((obs, old, niu) -> updateView());
        cbSort.valueProperty().addListener((obs, old, niu) -> updateView());

        // 3) Charger les données depuis la base

        List<Categorie> categories = serviceCategorie.afficherCategories();
        masterData.setAll(categories);


        // 4) Affichage initial
        updateView();
    }


    private void updateView() {
        String filtre = tfFilter.getText().toLowerCase().trim();
        // Filtrer
        List<Categorie> temp = masterData.stream()
                .filter(c -> c.getNom().toLowerCase().contains(filtre)
                        || c.getDescription().toLowerCase().contains(filtre)
                        || c.getStatut().toLowerCase().contains(filtre))
                .collect(Collectors.toList());

        // Trier (inchangé)
        String criterium = cbSort.getValue();
        Comparator<Categorie> cmp = switch (criterium) {
            case "Nom ↓"    -> Comparator.comparing(Categorie::getNom, String.CASE_INSENSITIVE_ORDER).reversed();
            case "Statut ↑" -> Comparator.comparing(Categorie::getStatut, String.CASE_INSENSITIVE_ORDER);
            case "Statut ↓" -> Comparator.comparing(Categorie::getStatut, String.CASE_INSENSITIVE_ORDER).reversed();
            default         -> Comparator.comparing(Categorie::getNom, String.CASE_INSENSITIVE_ORDER);
        };
        temp.sort(cmp);

        // Clear & rebuild
        vboxCategories.getChildren().clear();
        for (Categorie c : temp) {
            // 1) HBox racine
            HBox ligne = new HBox(15);
            ligne.setAlignment(Pos.CENTER_LEFT);
            ligne.setStyle(
                    "-fx-padding: 10; " +
                            "-fx-background-color: #fffbea; " +
                            "-fx-border-color: orange; " +
                            "-fx-border-width: 1.5; " +
                            "-fx-border-radius: 6; " +
                            "-fx-background-radius: 6;"
            );

            // 2) VBox infos (champs empilés)
            VBox vboxInfos = new VBox(8);
            vboxInfos.setAlignment(Pos.TOP_LEFT);
            // Label cliquable pour le nom
            Label lblNom = new Label(c.getNom());
            lblNom.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;");
            lblNom.setOnMouseEntered(evt -> lblNom.setStyle("-fx-text-fill: red; -fx-underline: true; -fx-cursor: hand;"));
            lblNom.setOnMouseExited(evt -> lblNom.setStyle("-fx-text-fill: blue; -fx-underline: true; -fx-cursor: hand;"));
            lblNom.setOnMouseClicked(evt -> {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/categorie/DetailsCategorie.fxml")
                    );
                    // On utilise Parent au lieu d'AnchorPane
                    Parent root = loader.load();

                    DetailsCategorieController ctrl = loader.getController();
                    ctrl.initData(c);
                    ctrl.setAfficherController(this);

                    Stage st = new Stage();
                    st.setScene(new Scene(root));
                    st.setTitle("Détails de la catégorie");
                    st.show();

                } catch (IOException ex) {
                    new Alert(Alert.AlertType.ERROR,
                            "Impossible d'ouvrir le détail : " + ex.getMessage()
                    ).showAndWait();
                }
            });


            Label lblDesc      = new Label("Description : "+ c.getDescription());
            Label lblStatut    = new Label("Statut : "     + c.getStatut());
            ImageView iv       = new ImageView();
            try {
                Image img = new Image(
                        new File(c.getUrl_image()).toURI().toString(),
                        120,  90, true, true
                );
                iv.setImage(img);
            } catch (Exception ex) {
                System.err.println("Image non chargée : " + ex.getMessage());
            }
            vboxInfos.getChildren().addAll(lblNom, lblDesc, lblStatut, iv);

            // 3) Spacer pour pousser les boutons à droite
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // 4) VBox boutons (alignés à droite)
            VBox vboxBtns = new VBox(8);
            vboxBtns.setAlignment(Pos.TOP_RIGHT);

            Button btnModifier = new Button("Modifier");
            btnModifier.setStyle(
                    "-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight:bold; " +
                            "-fx-pref-width:100px; -fx-pref-height:30px;"
            );
            btnModifier.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/categorie/ModifierCategorie.fxml")
                    );
                    Stage st = new Stage();
                    st.setScene(new Scene(loader.load()));
                    ModifierCategorieController ctl =
                            loader.getController();
                    ctl.initData(c);
                    ctl.setAfficherController(this);
                    st.setTitle("Modifier Catégorie");
                    st.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            Button btnSupprimer = new Button("Supprimer");
            btnSupprimer.setStyle(
                    "-fx-background-color: #FFD54F; -fx-text-fill: white; -fx-font-weight:bold; " +
                            "-fx-pref-width:100px; -fx-pref-height:30px;"
            );
            btnSupprimer.setOnAction(e -> {
                serviceCategorie.supprimerCategorie(c.getId());
                refresh();
            });

            vboxBtns.getChildren().addAll(btnModifier, btnSupprimer);

            // 5) Assemblage
            ligne.getChildren().addAll(vboxInfos, spacer, vboxBtns);
            vboxCategories.getChildren().add(ligne);
        }
    }

    @FXML
    private void ajouterCategorie() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/categorie/AjouterCategorie.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            AjouterCategorieController ctrl = loader.getController();
            ctrl.setAfficherController(this);
            stage.setTitle("Ajouter une Catégorie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerVersTypesEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/type_evenement/AfficherTypeEvenement.fxml")
            );
            Stage stage = new Stage();
            stage.setTitle("Liste des Types d'Événements");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur ouverture AfficherTypeEvenement.fxml : " + e.getMessage());
        }
    }

    @FXML
    public void refresh() {

        // 1) On relit toutes les catégories en base
        List<Categorie> categories = serviceCategorie.afficherCategories();
        masterData.setAll(categories);

        // 2) On met à jour la vue (filtre + tri + affichage)
        updateView();

    }


    @FXML
    private void showStatsChart() {
        try {
            // 1) Récupérer les stats
            Map<String,Integer> stats = serviceCategorie.getStatsEvenementsParCategorie();

            // 2) Créer les axes
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Catégorie");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Nombre d'événements");

            // 3) Créer le BarChart
            BarChart<String,Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Événements par catégorie");

            // 4) Construire la série de données
            XYChart.Series<String,Number> series = new XYChart.Series<>();
            series.setName("Événements");
            stats.forEach((cat, cnt) -> {
                series.getData().add(new XYChart.Data<>(cat, cnt));
            });
            barChart.getData().add(series);

            // 5) Afficher dans une nouvelle fenêtre
            BorderPane root = new BorderPane(barChart);
            Stage stage = new Stage();
            stage.setTitle("Statistiques – Graphique");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Impossible de charger le graphique : " + e.getMessage())
                    .showAndWait();
        }
    }

    @FXML
    private void toggleTranslation() {
        if (isFrench) {
            translateCategoriesToEnglish();
        } else {
            revertToFrench();
        }
        isFrench = !isFrench;
    }

    private void translateCategoriesToEnglish() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Node node : vboxCategories.getChildren()) {
                    if (node instanceof HBox) {
                        for (Node child : ((HBox) node).getChildren()) {
                            if (child instanceof VBox) {
                                for (Node field : ((VBox) child).getChildren()) {
                                    if (field instanceof Label) {
                                        Label label = (Label) field;
                                        if (label.getText().contains("Nom :") ||
                                                label.getText().contains("Description :") ||
                                                label.getText().contains("Statut :")) {

                                            String original = label.getText();
                                            String translated = TranslationUtil.translateToEnglish(original);
                                            Platform.runLater(() -> {
                                                translationCache.put(label, original);
                                                label.setText(translated);
                                            });
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private void revertToFrench() {
        for (Map.Entry<Label, String> entry : translationCache.entrySet()) {
            entry.getKey().setText(entry.getValue());
        }
        translationCache.clear();
    }

}
