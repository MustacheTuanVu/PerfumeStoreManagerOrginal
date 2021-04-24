package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A OrderDetails.
 */
@Entity
@Table(name = "order_details")
public class OrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private String orderID;

    @Column(name = "bill_id")
    private String billID;

    @Column(name = "product_id")
    private String productID;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Float price;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderDetails", "customers", "member" }, allowSetters = true)
    private Bills bills;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderDetails", "categories" }, allowSetters = true)
    private Products products;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getOrderID() {
        return this.orderID;
    }

    public OrderDetails orderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getBillID() {
        return this.billID;
    }

    public OrderDetails billID(String billID) {
        this.billID = billID;
        return this;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getProductID() {
        return this.productID;
    }

    public OrderDetails productID(String productID) {
        this.productID = productID;
        return this;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderDetails quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return this.price;
    }

    public OrderDetails price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Bills getBills() {
        return this.bills;
    }

    public OrderDetails bills(Bills bills) {
        this.setBills(bills);
        return this;
    }

    public void setBills(Bills bills) {
        this.bills = bills;
    }

    public Products getProducts() {
        return this.products;
    }

    public OrderDetails products(Products products) {
        this.setProducts(products);
        return this;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDetails)) {
            return false;
        }
        return id != null && id.equals(((OrderDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDetails{" +
            "id=" + getId() +
            ", orderID='" + getOrderID() + "'" +
            ", billID='" + getBillID() + "'" +
            ", productID='" + getProductID() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            "}";
    }
}
