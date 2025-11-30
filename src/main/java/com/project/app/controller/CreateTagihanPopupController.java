package main.java.com.project.app.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.java.com.project.app.dao.TagihanDAO;
import main.java.com.project.app.dao.TagihanSPViewDAO;
import main.java.com.project.app.model.TagihanModel;
import main.java.com.project.app.model.TagihanSPViewModel;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CreateTagihanPopupController implements MainInjectable {
    private String action;
    private TagihanModel selectedTagihan;

    @FXML private Text textAdd;
    @FXML private Text textUpdate;
    @FXML private Text smallTextAdd;
    @FXML private Text smallTextUpdate;

    @FXML private AnchorPane popupRoot;
    @FXML private TextField noTagihanField; // no_tag (PK)
    @FXML private TextField noSuratField; // sp_id (manual input)
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private DatePicker tanggalPick;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnBack;

    // Table (reference view_list_sp - read only)
    @FXML private TableView<TagihanSPViewModel> tableListSP;
    @FXML private TableColumn<TagihanSPViewModel, Integer> colIdSP;
    @FXML private TableColumn<TagihanSPViewModel, String> colNoSP;
    @FXML private TableColumn<TagihanSPViewModel, String> colPerusahaan;
    @FXML private TableColumn<TagihanSPViewModel, String> colTanggal;

    private MainController mainController;
    private TagihanController tagihanController;

    private TagihanSPViewDAO spViewDAO = new TagihanSPViewDAO();
    private TagihanDAO tagihanDAO = new TagihanDAO();

    public void setAction(String action) {
        this.action = action;
        updateUIBasedOnAction();
    }

    private void updateUIBasedOnAction() {
        if (action == null) return;

        boolean isAdd = action.equalsIgnoreCase("Add");

        textAdd.setVisible(isAdd);
        smallTextAdd.setVisible(isAdd);
        btnAdd.setVisible(isAdd);

        textUpdate.setVisible(!isAdd);
        smallTextUpdate.setVisible(!isAdd);
        btnUpdate.setVisible(!isAdd);
    }

    @Override
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setTagihanController(TagihanController controller) {
        this.tagihanController = controller;
    }

    @FXML
    private void initialize() {
        // setup status choice values
        statusChoice.getItems().addAll("Pending", "Batal", "Dibayar", "Telat");
        statusChoice.getSelectionModel().selectFirst();

        // setup table columns
        if (colIdSP != null) {
            colIdSP.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getSpId()).asObject());
        }
        if (colNoSP != null) {
            colNoSP.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNoSp()));
        }
        if (colPerusahaan != null) {
            colPerusahaan.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNamaPerusahaan()));
        }
        if (colTanggal != null) {
            colTanggal.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getTanggal()));
        }

        // load reference data into table (read-only)
        if (tableListSP != null) {
            ObservableList<TagihanSPViewModel> items = spViewDAO.getAllSPView();
            tableListSP.setItems(items);
            tableListSP.setPlaceholder(new Label("Tidak ada data SP."));
        }

        // button handlers
        if (btnAdd != null) btnAdd.setOnAction(e -> addTagihan());
        if (btnUpdate != null) btnUpdate.setOnAction(e -> updateTagihan());
        if (btnBack != null) btnBack.setOnAction(e -> closePopup());
    }

    // dipanggil dari MainController saat mode Update
    public void setTagihanData(TagihanModel tagihan) {
        this.selectedTagihan = tagihan;
        if (tagihan == null) return;

        noTagihanField.setText(tagihan.getNoTag());
        noSuratField.setText(tagihan.getSpId() + "");
        tanggalPick.setValue(tagihan.getTanggal());
        statusChoice.setValue(tagihan.getStatus() != null ? tagihan.getStatus() : statusChoice.getItems().get(0));
    }

    private void addTagihan() {
        String noTag = noTagihanField.getText().trim();
        String spText = noSuratField.getText().trim();
        if (spText.isEmpty() && tableListSP.getSelectionModel().getSelectedItem() != null) {
            spText = String.valueOf(tableListSP.getSelectionModel().getSelectedItem().getSpId());
        }
        String status = statusChoice.getValue();

        if (noTag.isEmpty()) {
            showAlert("No Tag wajib diisi.");
            return;
        }
        if (spText.isEmpty()) {
            showAlert("SP ID (noSuratField) wajib diisi.");
            return;
        }
        int spId;
        try {
            spId = Integer.parseInt(spText);
        } catch (NumberFormatException ex) {
            showAlert("SP ID harus berupa angka (integer).");
            return;
        }

        LocalDate tenggat = tanggalPick.getValue();

        boolean ok = tagihanDAO.insertTagihan(noTag, spId, status, tenggat);
        if (ok) {
            if (tagihanController != null) {
                tagihanController.loadData();
            }
            closePopup();
        } else {
            showAlert("Gagal menyimpan tagihan. Cek konsol untuk detail error.");
        }
    }

    private void updateTagihan() {
        if (selectedTagihan == null) {
            showAlert("Tidak ada data tagihan yang dipilih untuk diupdate.");
            return;
        }

        String noTag = noTagihanField.getText().trim();
        String spText = noSuratField.getText().trim();
        String status = statusChoice.getValue();

        if (noTag.isEmpty()) {
            showAlert("No Tag wajib diisi.");
            return;
        }
        if (spText.isEmpty()) {
            showAlert("SP ID (noSuratField) wajib diisi.");
            return;
        }
        int spId;
        try {
            spId = Integer.parseInt(spText);
        } catch (NumberFormatException ex) {
            showAlert("SP ID harus berupa angka (integer).");
            return;
        }

        LocalDate tenggat = tanggalPick.getValue();

        boolean ok = tagihanDAO.updateTagihan(noTag, spId, status, tenggat);
        if (ok) {
            if (tagihanController != null) {
                tagihanController.loadData();
            }
            closePopup();
        } else {
            showAlert("Gagal meng-update tagihan. Cek konsol untuk detail error.");
        }
    }

    public void closePopup() {
        Parent parent = popupRoot.getParent();
        if (parent != null) {
            ((Pane) parent).getChildren().remove(popupRoot);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
