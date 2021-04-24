package com.coderkiemcom.perfumestoremanager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.coderkiemcom.perfumestoremanager.domain.OrderDetails} entity.
 */
public class OrderDetailsDTO implements Serializable {

    private Long id;

    private String orderID;

    private String billID;

    private String productID;

    private Integer quantity;

    private Float price;

    private BillsDTO bills;

    private ProductsDTO products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public BillsDTO getBills() {
        return bills;
    }

    public void setBills(BillsDTO bills) {
        this.bills = bills;
    }

    public ProductsDTO getProducts() {
        return products;
    }

    public void setProducts(ProductsDTO products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDetailsDTO)) {
            return false;
        }

        OrderDetailsDTO orderDetailsDTO = (OrderDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
            "id=" + getId() +
            ", orderID='" + getOrderID() + "'" +
            ", billID='" + getBillID() + "'" +
            ", productID='" + getProductID() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", bills=" + getBills() +
            ", products=" + getProducts() +
            "}";
    }
}
