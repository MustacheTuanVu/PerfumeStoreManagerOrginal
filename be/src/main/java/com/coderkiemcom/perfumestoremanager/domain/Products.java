package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Products.
 */
@Entity
@Table(name = "products")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private String productID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "price")
    private Float price;

    @Column(name = "date_import")
    private LocalDate dateImport;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "description")
    private String description;

    @Column(name = "category_id")
    private String categoryID;

    @Column(name = "volume")
    private Float volume;

    @OneToMany(mappedBy = "products")
    @JsonIgnoreProperties(value = { "bills", "products" }, allowSetters = true)
    private Set<OrderDetails> orderDetails = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Categories categories;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Products id(Long id) {
        this.id = id;
        return this;
    }

    public String getProductID() {
        return this.productID;
    }

    public Products productID(String productID) {
        this.productID = productID;
        return this;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return this.productName;
    }

    public Products productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantityAvailable() {
        return this.quantityAvailable;
    }

    public Products quantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
        return this;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Float getPrice() {
        return this.price;
    }

    public Products price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public LocalDate getDateImport() {
        return this.dateImport;
    }

    public Products dateImport(LocalDate dateImport) {
        this.dateImport = dateImport;
        return this;
    }

    public void setDateImport(LocalDate dateImport) {
        this.dateImport = dateImport;
    }

    public LocalDate getExpireDate() {
        return this.expireDate;
    }

    public Products expireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getDescription() {
        return this.description;
    }

    public Products description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryID() {
        return this.categoryID;
    }

    public Products categoryID(String categoryID) {
        this.categoryID = categoryID;
        return this;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public Float getVolume() {
        return this.volume;
    }

    public Products volume(Float volume) {
        this.volume = volume;
        return this;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }

    public Set<OrderDetails> getOrderDetails() {
        return this.orderDetails;
    }

    public Products orderDetails(Set<OrderDetails> orderDetails) {
        this.setOrderDetails(orderDetails);
        return this;
    }

    public Products addOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
        orderDetails.setProducts(this);
        return this;
    }

    public Products removeOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.remove(orderDetails);
        orderDetails.setProducts(null);
        return this;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        if (this.orderDetails != null) {
            this.orderDetails.forEach(i -> i.setProducts(null));
        }
        if (orderDetails != null) {
            orderDetails.forEach(i -> i.setProducts(this));
        }
        this.orderDetails = orderDetails;
    }

    public Categories getCategories() {
        return this.categories;
    }

    public Products categories(Categories categories) {
        this.setCategories(categories);
        return this;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Products)) {
            return false;
        }
        return id != null && id.equals(((Products) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Products{" +
            "id=" + getId() +
            ", productID='" + getProductID() + "'" +
            ", productName='" + getProductName() + "'" +
            ", quantityAvailable=" + getQuantityAvailable() +
            ", price=" + getPrice() +
            ", dateImport='" + getDateImport() + "'" +
            ", expireDate='" + getExpireDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", categoryID='" + getCategoryID() + "'" +
            ", volume=" + getVolume() +
            "}";
    }
}
