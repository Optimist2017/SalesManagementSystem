package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity

public class SaleProduct {
    @Id
    @GeneratedValue

    private Long id;
    private String name;
    private  Double costPrice;
    private Double sellingPrice;

    @ManyToOne
    private SaleCategory category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public SaleCategory getCategory() {
        return category;
    }

    public void setCategory(SaleCategory category) {
        this.category = category;
    }
}
