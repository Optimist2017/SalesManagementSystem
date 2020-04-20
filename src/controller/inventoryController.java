package controller;

import business.DBManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Inventory;
import model.SaleProduct;
import model.Saleslog;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class inventoryController {
    @FXML private TableView<Inventory> table;
    @FXML private TableColumn<Inventory, SaleProduct> productColumn;
    @FXML private TableColumn<Inventory, SaleProduct> categoryColumn;
    @FXML private TableColumn<Inventory, Integer> quantityColumn;
    @FXML private ChoiceBox<SaleProduct> product;
    @FXML private TextField quantity;
    @FXML private Button editButton;


    public void initialize(){

        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productColumn.setCellFactory(Cell->new TableCell<Inventory,SaleProduct>(){
            @Override
            protected void updateItem(SaleProduct item, boolean empty) {
                super.updateItem(item, empty);
                setText(item==null|| empty? null : item.getName());
            }
        });


        categoryColumn.setCellFactory(Cell-> new TableCell<Inventory,SaleProduct>(){
            @Override
            protected void updateItem(SaleProduct item, boolean empty) {
                super.updateItem(item, empty);
                setText(item==null|| empty? null : item.getCategory().getName());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==null){
                editButton.setDisable(true);
                return;
            }
            product.setValue(newValue.getProduct());
            quantity.setText(String.valueOf(newValue.getQuantity()));
            editButton.setDisable(false);

        });

        List<Inventory> inventories = DBManager.listAll(Inventory.class);
        table.setItems(FXCollections.observableArrayList(inventories));

        List<SaleProduct> products = DBManager.listAll(SaleProduct.class);
        product.setItems(FXCollections.observableArrayList(products));


        product.setConverter(new StringConverter<SaleProduct>() {
            @Override
            public String toString(SaleProduct object) {
                if (object==null) return null;
                return object.getName();
            }

            @Override
            public SaleProduct fromString(String string) {
                return null;
            }
        });


    }


    @FXML private void edit(){

        Inventory inventory= table.getSelectionModel().getSelectedItem();
        String quantity = this.quantity.getText();

        inventory.setQuantity(Integer.parseInt(quantity));
        DBManager.merge(inventory);
        table.refresh();

        Saleslog log = new Saleslog();
        log.setUser(loginController.getUser());
        log.setAction("Product name "+inventory.getProduct().getName()+ " quantity changed to "+quantity);
        log.setTime(Timestamp.valueOf(LocalDateTime.now()));
        DBManager.save(log);


    }


}
