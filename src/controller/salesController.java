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
import model.CartSold;
import model.SaleCart;
import model.SaleProduct;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class salesController {

    @FXML private TableView<CartSold> saleTable;
    @FXML private TableColumn<CartSold,Timestamp> dateColumn;
    @FXML private TableColumn<CartSold,String> customerColumn;

    @FXML private TableView<SaleCart> cartTable;
    @FXML private TableColumn<SaleCart, SaleProduct> productColumn;
    @FXML private TableColumn<SaleCart,Integer> quantityColumn;
    @FXML private TableColumn<SaleCart,Double> priceColumn;
    @FXML private TableColumn<SaleCart,Double> totalColumn;
    @FXML private TextField totaltxtbox;
    @FXML private DatePicker fromdate;
    @FXML private DatePicker todate;



    private double amountValue;

    @FXML private void initialize(){

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customername"));

        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        dateColumn.setCellFactory(new Callback<TableColumn<CartSold, Timestamp>, TableCell<CartSold, Timestamp>>() {
            @Override
            public TableCell<CartSold, Timestamp> call(TableColumn<CartSold, Timestamp> param) {
                return new TableCell<CartSold,Timestamp>(){
                    @Override
                    protected void updateItem(Timestamp item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item==null||empty){
                            setText(null);
                        }else setText(DateFormat.getDateTimeInstance().format(item));
                    }
                };
            }
        });

        productColumn.setCellFactory(new Callback<TableColumn<SaleCart, SaleProduct>, TableCell<SaleCart, SaleProduct>>() {
            @Override
            public TableCell<SaleCart, SaleProduct> call(TableColumn<SaleCart, SaleProduct> param) {
                return new TableCell<SaleCart,SaleProduct>(){
                    @Override
                    protected void updateItem(SaleProduct item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item==null|| empty) setText(null);
                        else setText(item.getName());
                    }
                };
            }
        });

        saleTable.getItems().addAll(DBManager.listAll(CartSold.class));
        cartTable.getItems().addAll(DBManager.listAll(SaleCart.class));
        calculatetotal();


        saleTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CartSold>() {
            @Override
            public void changed(ObservableValue<? extends CartSold> observable, CartSold oldValue, CartSold newValue) {
                if (newValue==null)return;
                cartTable.getItems().clear();
                cartTable.getItems().addAll(newValue.getItems());
                calculatetotal();
            }
        });



    }




    @FXML private void addsales() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addsales.fxml"));

        Parent root = loader.load();

        addsalesController controller= loader.getController();
        controller.tables(saleTable,cartTable);

        Stage stage = new Stage();
        stage.setTitle("Add New Sale ");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    @FXML  private void datesearch(){
        Timestamp from = Timestamp.valueOf(fromdate.getValue().atTime(LocalTime.now()));
        Timestamp to = Timestamp.valueOf(todate.getValue().atTime(LocalTime.now()));

        if (todate.getValue().isBefore(fromdate.getValue())|| from.toString().isEmpty()||to.toString().isEmpty()){
            Utility.alert(Alert.AlertType.ERROR,"Please check the dates ");
            return;
        }

        List<CartSold> product = DBManager.query(CartSold.class, "select c from CartSold c where c.time>=?1 and c.time<=?2", from,to);
        saleTable.getItems().clear();
        saleTable.setItems(FXCollections.observableArrayList(product));

    }

    private void calculatetotal(){
        amountValue = 0;
        for(SaleCart cartItem: cartTable.getItems()){

            amountValue += cartItem.getAmount();


        }

        totaltxtbox.setText(String.valueOf(amountValue));

    }


}


