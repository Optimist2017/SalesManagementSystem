package controller;

import business.DBManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.SaleCategory;
import model.Saleslog;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class categoryController {
    public TableView<SaleCategory> table;
    public TableColumn<SaleCategory , String> nameColumn;

    @FXML private Button editbtn;
    @FXML private Button deletebtn;
    public TextField name;
    @FXML
    private void initialize(){

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        List<SaleCategory> categoryList= DBManager.listAll(SaleCategory.class);
        table.setItems(FXCollections.observableArrayList(categoryList));

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SaleCategory>() {
            @Override
            public void changed(ObservableValue<? extends SaleCategory> observable, SaleCategory oldValue, SaleCategory newValue) {

                if(newValue ==null){
                   editbtn.setDisable(true);
                   deletebtn.setDisable(true);
                   return;


               }  ;

                 editbtn.setDisable(false);
                 deletebtn.setDisable(false);
                 name.setText(newValue.getName());


            }
        });


    }


    @FXML
    private void save(){
        String name = this.name.getText();
        SaleCategory category = new SaleCategory();
        category.setName(name);
        DBManager.save(category);
        table.getItems().add(category);
        this.name.clear();

        Saleslog log = new Saleslog();
        log.setUser(loginController.getUser());
        log.setAction("Saved a new category "+name);
        log.setTime(Timestamp.valueOf(LocalDateTime.now()));
        DBManager.save(log);


    }
    @FXML
    private void edit(){

        String name = this.name.getText();
        SaleCategory category = table.getSelectionModel().getSelectedItem();
        category.setName(name);
        DBManager.merge(category);
        table.refresh();
        this.name.clear();

        Saleslog log = new Saleslog();
        log.setUser(loginController.getUser());
        log.setAction("Editted Category "+name);
        log.setTime(Timestamp.valueOf(LocalDateTime.now()));
        DBManager.save(log);


    }
    @FXML
    private void delete(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText("Delete");
        alert.setContentText("Are sure you want to delete the item from the list");
        Optional<ButtonType> response = alert.showAndWait();

        if(response != null && response.isPresent() && response.get()==ButtonType.OK){

            SaleCategory category = table.getSelectionModel().getSelectedItem();
            DBManager.delete(category);
            table.getItems().remove(category);
            this.name.clear();

            Saleslog log = new Saleslog();
            log.setUser(loginController.getUser());
            log.setAction("Removed category "+this.name.getText());
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            DBManager.save(log);

        }




    }
}
