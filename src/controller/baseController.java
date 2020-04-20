package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class baseController {

    @FXML private VBox container;

    public void initialize() throws IOException {
        loadsales();


    }


    public void loadcontent(String fxml) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/"+fxml+".fxml"));
        container.getChildren().clear();
        container.getChildren().add(root);
        VBox.setVgrow(root, Priority.ALWAYS);


    }

    @FXML private void loadsales() throws IOException {
        loadcontent("sales");
    }

    @FXML private void loadcategories() throws IOException {
        loadcontent("category");
    }

    @FXML private void loademployees() throws IOException {
        loadcontent("employee");
    }

    @FXML private void loadprofile() throws IOException {
        loadcontent("profile");
    }
    @FXML private void loadproduct() throws IOException {
        loadcontent("product");
    }

    @FXML private void loadinventory() throws IOException {
        loadcontent("inventory");
    }
}
