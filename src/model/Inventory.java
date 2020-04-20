package model;

import javax.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @OneToOne

    @JoinColumn(name = "product")
    private SaleProduct product;

    @Column(name = "quantity")
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SaleProduct getProduct() {
        return product;
    }

    public void setProduct(SaleProduct product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }




}
