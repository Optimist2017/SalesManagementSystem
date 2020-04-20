package model;

import javax.persistence.*;

@Entity
@Table(name = "salecart")
public class SaleCart {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "product")
    @ManyToOne
    private SaleProduct product;

    @Column(name = "price")
    private Double price;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "amount")
    private Double amount;


    @ManyToOne
    @JoinColumn(name = "CartSold")
    private CartSold sale ;

    public CartSold getSale() { return sale; }

    public void setSale(CartSold sale) { this.sale = sale; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public SaleProduct getProduct() {
        return product;
    }

    public void setProduct(SaleProduct product) {
        this.product = product;
    }
}

