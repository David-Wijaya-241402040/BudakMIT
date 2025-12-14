package main.java.com.project.app.controller;

public interface SharedControllerProvider {
    void setSparepartController(SparepartController controller);
    SparepartController getSparepartController();

    void setTagihanController(TagihanController controller);
    TagihanController getTagihanController();

<<<<<<< HEAD
    void setAddNewPenawaranController(AddNewPenawaranController controller);
    AddNewPenawaranController getAddNewPenawaranController();
=======
    void setActivePenawaranPopup(CreatePenawaranPopupController controller);
    CreatePenawaranPopupController getPenawaranPopupController();
>>>>>>> ba15d41d1a41cbc4adf69da486cc3a09d6012116
}
