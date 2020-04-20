package model;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CartSold")
public class CartSold {


    @Id
    @GeneratedValue

    @Column(name = "id")
    private Long id;


    @OneToMany(mappedBy = "sale")
    private List<SaleCart> items = new ArrayList<>();

    @Column(name = "time")
     private Timestamp time ;

    @Column(name = "amount")
     private Double amount ;

    @ManyToOne
    @JoinColumn (name = "Salesperson")
    private SaleEmployees salesperson;

    @Column (name = "customername")
    private String customername;

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SaleCart> getItems() {
        return items;
    }

    public void setItems(List<SaleCart> items) {
        this.items = items;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SaleEmployees getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(SaleEmployees salesperson) {
        this.salesperson = salesperson;
    }
}
