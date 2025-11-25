package main.java.com.project.app.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SalesOverviewPopupController {
    @FXML
    private ComboBox<String> comboBulan;

    public void initialize() {
        comboBulan.getItems().addAll("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember");

        // Pseudo-class untuk state 'open'
        PseudoClass open = PseudoClass.getPseudoClass("open");

        comboBulan.showingProperty().addListener((obs, oldValue, newValue) -> {
            comboBulan.pseudoClassStateChanged(open, newValue);
        });
    }
}

