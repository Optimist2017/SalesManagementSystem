package controller;

import Utility.Utility;
import business.DBManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class addsalesController {

    @FXML private TextField customername;
    @FXML private ChoiceBox<SaleProduct> product;
    @FXML private TextField price;
    @FXML private TextField quantity;
    @FXML private TextField amount;
    @FXML private TableView<SaleCart> table;
    @FXML private TableColumn<SaleCart,SaleProduct> productColumn;
    @FXML private TableColumn<SaleCart,Double> priceColumn;
    @FXML private TableColumn<SaleCart,Integer> quantityColumn;
    @FXML private TableColumn<SaleCart,Double> amountColumn;
    @FXML private TextField total;

    private double amountValue;

    private TableView<CartSold> saleTable;
    private TableView<SaleCart> cartTable;


    @FXML private void initialize(){

        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        product.setConverter(new StringConverter<SaleProduct>() {
            @Override
            public String toString(SaleProduct object) {
                if (object==null)return null;
                return object.getName();
            }

            @Override
            public SaleProduct fromString(String string) {
                return null;
            }
        });

        product.getSelectionModel().selectFirst();

        product.setItems(FXCollections.observableArrayList(DBManager.listAll(SaleProduct.class)));
        product.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue==null) return;
            price.setText(String.valueOf(newValue.getSellingPrice()));
            calculateAmount();

        }));

        quantity.textProperty().addListener(((observable, oldValue, newValue) -> {
            calculateAmount();
        }));

        productColumn.setCellFactory(Cell-> new TableCell<SaleCart,SaleProduct>(){
            @Override
            protected void updateItem(SaleProduct item, boolean empty) {
                super.updateItem(item, empty);
                if (item==null|| empty) setText(null);
                else setText(item.getName());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue==null) return;
            product.setValue(newValue.getProduct());
            price.setText(String.valueOf(newValue.getPrice()));
            quantity.setText(String.valueOf(newValue.getQuantity()));
            amount.setText(String.valueOf(newValue.getAmount()));
        }));



    }



    private void calculateAmount(){

        SaleProduct product = this.product.getValue();
        String quantity = this.quantity.getText().trim();
        quantity= quantity.isEmpty()? "0" : quantity;

        if (product==null ){
            this.amount.setText("0.0");
            return;
        }

        double amount = product.getSellingPrice() * Integer.parseInt(quantity);
        this.amount.setText(String.valueOf(amount));


    }



    private void calculatetotal(){
        amountValue = 0;
        for(SaleCart cartItem: table.getItems()){

            amountValue += cartItem.getAmount();


        }

        total.setText(String.valueOf(amountValue));

    }

    private void reset(){

        this.product.getSelectionModel().selectFirst();
        //this.price.clear();
        this.quantity.clear();
        this.amount.clear();
        table.getSelectionModel().select(null);
    }


    @FXML private void add(){

        SaleProduct product = this.product.getValue();
        String price = this.price.getText();
        String quantity = this.quantity.getText().trim();
        String amount  = this.amount.getText();

        SaleCart item = new SaleCart();
        item.setProduct(product);
        item.setPrice(product.getSellingPrice());
        item.setQuantity(Integer.parseInt(quantity));
        item.setAmount(Double.valueOf(amount));
        item.setPrice(Double.valueOf(price));

        Inventory inventory = DBManager.queryForSingleResult(Inventory.class, "select i from Inventory i where i.product =?1", product);

        if (inventory.getQuantity()< Integer.parseInt(quantity)){

            Utility.alert(Alert.AlertType.ERROR,"Quantity entered less than product in inventory ");
            return;
        }


        table.getItems().add(item);
        calculatetotal();
        reset();

    }

    @FXML private void save(){

        if (table.getItems().isEmpty()|| customername.getText().isEmpty()){
            Utility.alert(Alert.AlertType.INFORMATION,"Please enter products first or customer name ");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Confirm Items");
        alert.setContentText("Please confirm the items entered are correct and click enter");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType != null && buttonType.isPresent() && buttonType.get()== ButtonType.OK){

            CartSold sale = new CartSold();
            sale.setAmount(amountValue);
            sale.setTime(Timestamp.valueOf(LocalDateTime.now()));
            sale.setSalesperson(loginController.getUser());
            sale.setCustomername(customername.getText());

            DBManager.save(sale);

            for (SaleCart item : table.getItems()) {

                item.setSale(sale);
                DBManager.save(item);
                Inventory inventory = DBManager.queryForSingleResult(Inventory.class, "select i from Inventory i where i.product =?1", item.getProduct());

                inventory.setQuantity(inventory.getQuantity()-item.getQuantity());
                DBManager.merge(inventory);
            }

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Sale Completed");
            alert.setContentText("The sale have been saved successfully");
            alert.show();
            saleTable.getItems().addAll(sale);
            cartTable.refresh();

            table.getItems().clear();
        }

        Saleslog log = new Saleslog();
        log.setUser(loginController.getUser());
        log.setAction("Competed a sale ");
        log.setTime(Timestamp.valueOf(LocalDateTime.now()));
        DBManager.save(log);



    }

    @FXML private void remove(){

        if (table.getSelectionModel().isEmpty()){
            Utility.alert(Alert.AlertType.ERROR,"Please select product to be removed ");
            return;
        }

        Alert alert =new  Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Remove Cart Item");
        alert.setContentText("Do you really want to remove the item from the cart ?");

        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType!=null && buttonType.isPresent() && buttonType.get()==ButtonType.OK){
            SaleCart item = table.getSelectionModel().getSelectedItem();

            table.getItems().remove(item);
            reset();

        }

    }

    public void tables(TableView<CartSold> saleTable,TableView<SaleCart> cartTable){
        this.saleTable=saleTable;
        this.cartTable=cartTable;

    }




}
