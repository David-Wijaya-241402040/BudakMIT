package main.java.com.project.app.controller;

public interface SharedControllerProvider {
    void setSparepartController(SparepartController controller);
    SparepartController getSparepartController();

    void setTagihanController(TagihanController controller);
    TagihanController getTagihanController();

    void setActivePenawaranPopup(CreatePenawaranPopupController controller);
    CreatePenawaranPopupController getPenawaranPopupController();
}
