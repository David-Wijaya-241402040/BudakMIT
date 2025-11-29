package main.java.com.project.app.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.java.com.project.app.config.DBConnection;
import main.java.com.project.app.dao.SparepartDAO;
import main.java.com.project.app.dao.UserDAO;
import main.java.com.project.app.model.SparepartModel;

public class CreateSparepartPopupController implements MainInjectable {
    private String action;
    private SparepartModel selectedSparepart;
    
    @FXML private Text textAdd;
    @FXML private Text textUpdate;
    @FXML private Text smallTextAdd;
    @FXML private Text smallTextUpdate;

    @FXML private AnchorPane popupRoot;
    @FXML private TextField namaKomponenField;
    @FXML private TextField satuanField;
    @FXML private TextField qtyField;
    @FXML private TextField hargaField;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnBack;

    private MainController mainController;
    private SparepartController sparepartController;

    public void setAction(String action) {
        this.action = action;
        
        updateUIBasedOnAction();
    }

    private void updateUIBasedOnAction() {
        if (action == null) return;

        if (action.equalsIgnoreCase("Add")) {
            textAdd.setVisible(true);
            smallTextAdd.setVisible(true);
            btnAdd.setVisible(true);
            textUpdate.setVisible(false);
            smallTextUpdate.setVisible(false);
            btnUpdate.setVisible(false);

        } else if (action.equalsIgnoreCase("Update")) {
            textAdd.setVisible(false);
            smallTextAdd.setVisible(false);
            btnAdd.setVisible(false);
            textUpdate.setVisible(true);
            smallTextUpdate.setVisible(true);
            btnUpdate.setVisible(true);
        }
    }

    public void setSparepartData(SparepartModel sparepart) {
        this.selectedSparepart = sparepart;

        namaKomponenField.setText(sparepart.getNamaComponent());
        satuanField.setText(sparepart.getNamaSatuan());
        qtyField.setText(String.valueOf(sparepart.getQty()));
        hargaField.setText(String.valueOf(sparepart.getHargaBaru()));
    }


    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setSparepartController(SparepartController controller) {
        this.sparepartController = controller;
    }

    @FXML
    private void initialize() {
        btnAdd.setOnAction(e -> addSparepart());
        
        btnUpdate.setOnAction(e -> updateSparepart());

        btnBack.setOnAction(e -> closePopup());
    }

    private void updateSparepart() {
        if (selectedSparepart == null) return;

        try {
            int id = selectedSparepart.getComponentId();
            String nama = namaKomponenField.getText();
            String satuan = satuanField.getText();
            int qty = Integer.parseInt(qtyField.getText());
            double harga = Double.parseDouble(hargaField.getText());

            SparepartDAO dao = new SparepartDAO(DBConnection.getConnection());

            boolean success = dao.updateSparepart(id, nama, satuan, qty, harga);

            if (success) {
                sparepartController.loadData();
                closePopup();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addSparepart() {
        try {
            String nama = namaKomponenField.getText();
            String satuan = satuanField.getText();
            int qty = Integer.parseInt(qtyField.getText());
            double harga = Double.parseDouble(hargaField.getText());

            SparepartDAO dao = new SparepartDAO(DBConnection.getConnection());

            boolean success = dao.addSparepart(nama, satuan, qty, harga);

            if (success) {
                sparepartController.loadData();
                closePopup();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closePopup() {
        Parent parent = popupRoot.getParent();
        if(parent != null) {
            ((Pane) parent).getChildren().remove(popupRoot);
        }
    }
}
