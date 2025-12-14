package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class MonthRangePickerController {

    @FXML private ComboBox<String> startMonth;
    @FXML private ComboBox<String> startYear;
    @FXML private ComboBox<String> endMonth;
    @FXML private ComboBox<String> endYear;
    @FXML private Button btnApply;
    @FXML private Button btnCancel;

    private Pair<String,String> selectedRange = null;
    private static final DateTimeFormatter YM_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

    @FXML
    public void initialize() {
        // fill months
        String[] months = {"01","02","03","04","05","06","07","08","09","10","11","12"};
        startMonth.getItems().addAll(months);
        endMonth.getItems().addAll(months);

        // fill years (last 5 years up to current)
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear; y >= currentYear - 5; y--) {
            startYear.getItems().add(String.valueOf(y));
            endYear.getItems().add(String.valueOf(y));
        }

        // defaults: start = this month, end = this month
        String ym = LocalDate.now().format(YM_FMT);
        String[] parts = ym.split("-");
        startYear.setValue(parts[0]);
        startMonth.setValue(parts[1]);
        endYear.setValue(parts[0]);
        endMonth.setValue(parts[1]);

        btnApply.setOnAction(e -> onApply());
        btnCancel.setOnAction(e -> onCancel());
    }

    private void onApply() {
        String sY = startYear.getValue();
        String sM = startMonth.getValue();
        String eY = endYear.getValue();
        String eM = endMonth.getValue();
        if (sY == null || sM == null || eY == null || eM == null) {
            // simple validation: ignore if invalid
            selectedRange = null;
        } else {
            String startYm = sY + "-" + sM;
            String endYm = eY + "-" + eM;

            // validate that start <= end (by year-month)
            int startInt = Integer.parseInt(sY) * 100 + Integer.parseInt(sM);
            int endInt = Integer.parseInt(eY) * 100 + Integer.parseInt(eM);
            if (startInt <= endInt) {
                selectedRange = new Pair<>(startYm, endYm);
            } else {
                // swap if user reversed
                selectedRange = new Pair<>(endYm, startYm);
            }
        }
        // close dialog
        Stage st = (Stage) btnApply.getScene().getWindow();
        st.close();
    }

    private void onCancel() {
        selectedRange = null;
        Stage st = (Stage) btnCancel.getScene().getWindow();
        st.close();
    }

    // called by HomeController after dialog closes
    public Pair<String,String> getSelectedRange() {
        return selectedRange;
    }
}
