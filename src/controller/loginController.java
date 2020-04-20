package controller;

import Utility.Utility;
import business.DBManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SaleEmployees;
import model.Saleslog;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;


public class loginController {

    @FXML private TextField username ;
    @FXML private TextField password ;

    private static SaleEmployees user;

    public static SaleEmployees getUser() {
        return user;
    }

    @FXML private void login(){
        String name = username.getText().trim();
        String pass = password.getText().trim();

        Long count = DBManager.queryForSingleResult(Long.class,"select count (p) from SaleEmployees p where p.username=?1 and p.password=?2",name,pass);


        if(count > 0){

            try {
                SaleEmployees employees = DBManager.queryForSingleResult(SaleEmployees.class, "select e from SaleEmployees e where e.username = ?1", name);
                user = employees;
                Utility.alert(Alert.AlertType.INFORMATION,"Welcome " + name);
                Parent root = FXMLLoader.load(getClass().getResource("/view/base.fxml"));
                Stage stage = (Stage) username.getScene().getWindow();
                stage.hide();
                stage.setScene(new Scene(root));
                stage.show();


                Saleslog log = new Saleslog();
                log.setUser(user);
                log.setTime(Timestamp.valueOf(LocalDateTime.now()));
                log.setAction("Logged In");
                DBManager.save(log);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Utility.alert(Alert.AlertType.ERROR,"Wrong Credentials , Please retry again ");


        }










    }

    @FXML private void cancel(){

        username.clear();
        password.clear();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit ?");
        alert.setHeaderText("Exit Application");
        alert.setContentText("Are you sure you want to exit this application");
         Optional<ButtonType> response = alert.showAndWait();

        if(response != null && response.isPresent() && response.get()== ButtonType.OK  ){
            ((Stage) username.getScene().getWindow()).close();
        }



    }
}
