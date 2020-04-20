package model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class SaleEmployees {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username ;

    @Column(name = "fullname")
    private String fullname ;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "email")
    private String email;

    @Column(name = "salary")
    private String salary;

    @Column(name= "date_registered")
    private LocalDate dateregistered;

    @Column(name = "registered_by")
    private String registeredby;

    @Column(name = "status")
    private String status;

    @Column(name = "address")
    private String address;

    @Column(name = "image ")
    private String image;

    @Column(name = "password")
    private String password;

//    @OneToMany
//    private List<Product> productList = new ArrayList<>();
//
//    @OneToMany
//    private List<Sale> sales = new ArrayList<>();
//
//    public List<Sale> getSales() {
//        return sales;
//    }
//
//    public void setSales(List<Sale> sales) {
//        this.sales = sales;
//    }
//
//    public List<Product> getProductList() {
//        return productList;
//    }
//
//    public void setProductList(List<Product> productList) {
//        this.productList = productList;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public LocalDate getDateregistered() {
        return dateregistered;
    }

    public void setDateregistered(LocalDate dateregistered) {
        this.dateregistered = dateregistered;
    }

    public String getRegisteredby() {
        return registeredby;
    }

    public void setRegisteredby(String registeredby) {
        this.registeredby = registeredby;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
