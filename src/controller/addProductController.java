package controller;

import Utility.Utility;
import business.DBManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Inventory;
import model.SaleCategory;
import model.SaleProduct;
import model.Saleslog;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class addProductController {


public TextField name;
public TextField costPrice;
public TextField sellingPrice;
public ChoiceBox<SaleCategory> category ;


     private TableView<SaleProduct> table;




      @FXML private void initialize(){

         category.setConverter(new StringConverter<SaleCategory>() {
             @Override
             public String toString(SaleCategory object) {
                 if (object==null)return null;
                 return object.getName();
             }

             @Override
             public SaleCategory fromString(String string) {
                 return null;
             }
         });
          List<SaleCategory> categories = DBManager.listAll(SaleCategory.class);
          category.setItems((FXCollections.observableArrayList(categories)));
          category.getSelectionModel().selectFirst();
      }




     public  void setTable(TableView<SaleProduct> table){

          this.table = table;
      }



public void save(){

    String name = this.name.getText();
    String costPrice = this.costPrice.getText();
    String sellingPrice = this.sellingPrice.getText();
    SaleCategory category = this.category.getValue();


    if (name.isEmpty() || costPrice.isEmpty() || sellingPrice.isEmpty()){

        Utility.alert(Alert.AlertType.ERROR,"Please check your inputs ");
        return;
    }

    SaleProduct product = new SaleProduct();
    product.setName(name);
    product.setCostPrice(Double.valueOf(costPrice));
    product.setSellingPrice(Double.valueOf(sellingPrice));
    product.setCategory(category);
    table.getItems().addAll(product);
    DBManager.save(product);
    ((Stage) this.name.getScene().getWindow()).close();

    Inventory inventory= new Inventory();
    inventory.setProduct(product);
    inventory.setQuantity(0);
    DBManager.save(inventory);

    Saleslog log = new Saleslog();
    log.setUser(loginController.getUser());
    log.setAction("Saved a new product called "+name);
    log.setTime(Timestamp.valueOf(LocalDateTime.now()));
    DBManager.save(log);


}

   @FXML private void clear(){
          this.name.clear();
          this.costPrice.clear();
          this.sellingPrice.clear();

   }





}
