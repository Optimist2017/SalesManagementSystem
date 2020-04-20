package controller;

import business.DBManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.SaleEmployees;
import model.Saleslog;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class profileController {

    @FXML private TextField username;
    @FXML private TextField fullname;
    @FXML private TextField phonenumber;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField salary;
    @FXML private TextField dateregistered;
    @FXML private TextField registeredby;
    public ImageView picture;
    @FXML private ChoiceBox<String> status;
    @FXML private TextArea address;

    private String image;

    @FXML private void initialize(){

        this.dateregistered.setText(String.valueOf(LocalDate.now()));

        status.getItems().addAll("Active","Inactive");



        username.setText(loginController.getUser().getUsername());
        fullname.setText(loginController.getUser().getFullname());
        phonenumber.setText(loginController.getUser().getPhonenumber());
        email.setText(loginController.getUser().getEmail());
        password.setText(loginController.getUser().getPassword());
        salary.setText(loginController.getUser().getSalary());
        address.setText(loginController.getUser().getAddress());
        status.setValue(loginController.getUser().getStatus());
        dateregistered.setText(String.valueOf(loginController.getUser().getDateregistered()));
        registeredby.setText(loginController.getUser().getRegisteredby());

        String pictue = loginController.getUser().getImage();
        if (pictue==null||pictue.isEmpty()){
            return;
        }
        byte[] bytes = Base64.decodeBase64(pictue);
        picture.setImage(new Image(new ByteArrayInputStream(bytes)));

    }

    @FXML public void selectimage() throws IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Please Select  Image ");
        File file = chooser.showOpenDialog(username.getScene().getWindow());

        if (file!=null){
            byte[] bytes = FileUtils.readFileToByteArray(file);

            image = Base64.encodeBase64String(bytes);
            picture.setImage(new Image(new ByteArrayInputStream(bytes)));


        }


    }

    @FXML private void update(){


        String username = this.username.getText();
        String fullname = this.fullname.getText();
        String phonenumber = this.phonenumber.getText();
        String email = this.email.getText();
        String password = this.password.getText();
        String salary = this.salary.getText();
        String dateregistered = this.dateregistered.getText();
        String registeredby = this.registeredby.getText();
        String status = this.status.getValue();
        String address = this.address.getText();


        SaleEmployees employees = loginController.getUser();
        employees.setUsername(username);
        employees.setFullname(fullname);
        employees.setPhonenumber(phonenumber);
        employees.setEmail(email);
        employees.setPassword(password);
        employees.setSalary(salary);
        employees.setDateregistered(LocalDate.parse(dateregistered));
        employees.setRegisteredby(registeredby);
        employees.setImage(image);
        employees.setStatus(status);
        employees.setAddress(address);

        Saleslog log = new Saleslog();
        log.setUser(loginController.getUser());
        log.setAction("Employee name "+employees.getFullname()+" record changed ");
        log.setTime(Timestamp.valueOf(LocalDateTime.now()));
        DBManager.save(log);



        DBManager.merge(employees);



    }



}
