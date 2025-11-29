package main.java.com.project.app.controller;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.com.project.app.dao.TagihanDAO;
import main.java.com.project.app.model.TagihanModel;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TagihanController implements Initializable {

    @FXML private TableView<TagihanModel> tableTagihan;
    @FXML private TableColumn<TagihanModel, String> colNoTagihan;
    @FXML private TableColumn<TagihanModel, Integer> colSpId;
    @FXML private TableColumn<TagihanModel, Date> colTanggal;
    @FXML private TableColumn<TagihanModel, String> colTotal;
    @FXML private TableColumn<TagihanModel, String> colStatus;
    @FXML private Label labelGrandTotal;
    @FXML private TextField fieldSearch;


    private final TagihanDAO tagihanDAO = new TagihanDAO();
    private ObservableList<TagihanModel> masterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNoTagihan.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNoTagihan()));
        colSpId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSpId()).asObject());
        colTanggal.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTanggal()));

        // pakai format rupiah ke String
        colTotal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTotalHargaRupiah()));

        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        tableTagihan.setItems(tagihanDAO.listTagihan());

        // isi grand total
        double totalAll = tagihanDAO.hitungGrandTotal();
        labelGrandTotal.setText("Total: Rp " + String.format("%,.0f", totalAll).replace(",", "."));

        // load data awal ke master list
        masterData = tagihanDAO.listTagihan();
        tableTagihan.setItems(masterData);

        // search listener
        fieldSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filterData(newVal);
        });

        // isi total awal
        updateGrandTotal(masterData);
    }

    private void filterData(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            tableTagihan.setItems(masterData);
            filterData(keyword);
            updateGrandTotal(masterData);
            return;
        }

        String lowerKey = keyword.toLowerCase();

        ObservableList<TagihanModel> filtered = masterData.stream()
                .filter(item ->
                        item.getNoTagihan().toLowerCase().contains(lowerKey) ||
                                item.getStatus().toLowerCase().contains(lowerKey) ||
                                String.valueOf(item.getSpId()).contains(lowerKey)
                )
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableTagihan.setItems(filtered);
        updateGrandTotal(filtered);
        System.out.println("🔍 Keyword search: " + keyword);
        System.out.println("📌 Jumlah data setelah filter: " + tableTagihan.getItems().size());
    }
    private void updateGrandTotal(ObservableList<TagihanModel> list) {
        double total = list.stream().mapToDouble(TagihanModel::getTotalHarga).sum();
        labelGrandTotal.setText("Total: Rp " + String.format("%,.0f", total).replace(",", "."));
    }
}