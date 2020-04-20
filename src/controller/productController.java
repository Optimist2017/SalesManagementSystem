package controller;

import Utility.Utility;
import business.DBManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Inventory;
import model.SaleCategory;
import model.SaleProduct;
import model.Saleslog;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class productController {

    @FXML private TableView<SaleProduct> table;
    @FXML private TableColumn<SaleProduct,String> nameColumn;
    @FXML private TableColumn<SaleProduct,Double> costPriceColumn;
    @FXML private TableColumn<SaleProduct,Double> sellingPriceColumn;
    @FXML private TableColumn<SaleProduct, SaleCategory> categoryColumn;


    @FXML
    private void initialize(){

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        costPriceColumn.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        categoryColumn.setCellFactory(new Callback<TableColumn<SaleProduct, SaleCategory>, TableCell<SaleProduct, SaleCategory>>() {
            @Override
            public TableCell<SaleProduct, SaleCategory> call(TableColumn<SaleProduct, SaleCategory> param) {
                return new TableCell<SaleProduct,SaleCategory>(){
                    @Override
                    protected void updateItem(SaleCategory item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item==null|| empty) setText(null);
                        else setText(item.getName());
                    }
                };
            }
        });

        List<SaleProduct> productList= DBManager.listAll(SaleProduct.class);
        table.setItems(FXCollections.observableArrayList(productList));
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SaleProduct>() {
            @Override
            public void changed(ObservableValue<? extends SaleProduct> observable, SaleProduct oldValue, SaleProduct newValue) {

                if(newValue ==null){
                    return;


                }
                else {

                };


            }
        });

    }



    @FXML
    private void save() throws IOException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add_product.fxml"));
        Parent root = loader.load();
        addProductController controller = loader.getController();
        controller.setTable(table);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add a new product");
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML private  void edit() throws IOException {

        if (table.getSelectionModel().isEmpty()){
            Utility.alert(Alert.AlertType.ERROR,"Please select the prooduct to be editted ");
            return;
        }

        SaleProduct product = table.getSelectionModel().getSelectedItem();
        String name = product.getName();
        Double costprice = product.getCostPrice();
        Double sellingprice = product.getSellingPrice();
        SaleCategory category = product.getCategory();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit_product.fxml"));
        Parent root = loader.load();
        editProductController controller = loader.getController();
        controller.setTable(table);
        controller.setItems(name,costprice,sellingprice,category);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Product");
        stage.setScene(new Scene(root));
        stage.show();


    }


    @FXML
    private void delete(){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deletion");
        alert.setHeaderText("Delete");
        alert.setContentText("Are sure you want to delete the item from the list");
        Optional<ButtonType> response = alert.showAndWait();

        if(response != null && response.isPresent() && response.get()==ButtonType.OK){

            SaleProduct product = table.getSelectionModel().getSelectedItem();
            Inventory inventory= DBManager.queryForSingleResult(Inventory.class,"select e from Inventory e where e.product=?1",product);
            DBManager.delete(inventory);
            DBManager.delete(product);
            table.getItems().remove(product);

            Saleslog log = new Saleslog();
            log.setUser(loginController.getUser());
            log.setAction("Product name "+product.getName()+" removed");
            log.setTime(Timestamp.valueOf(LocalDateTime.now()));
            DBManager.save(log);

        }


    }
}
