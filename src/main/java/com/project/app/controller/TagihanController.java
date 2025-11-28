package main.java.com.project.app.controller;


import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.com.project.app.dao.TagihanDAO;
import main.java.com.project.app.model.TagihanModel;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class TagihanController implements Initializable {

    @FXML private TableView<TagihanModel> tableTagihan;
    @FXML private TableColumn<TagihanModel, String> colNoTagihan;
    @FXML private TableColumn<TagihanModel, Integer> colSpId;
    @FXML private TableColumn<TagihanModel, Date> colTanggal;
    @FXML private TableColumn<TagihanModel, String> colTotal;
    @FXML private TableColumn<TagihanModel, String> colStatus;
    @FXML private Label labelGrandTotal;

    private final TagihanDAO tagihanDAO = new TagihanDAO();

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
    }
}

