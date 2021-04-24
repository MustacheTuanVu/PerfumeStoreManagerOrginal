package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Categories.
 */
@Entity
@Table(name = "categories")
public class Categories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private String categoryID;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "categories")
    @JsonIgnoreProperties(value = { "orderDetails", "categories" }, allowSetters = true)
    private Set<Products> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categories id(Long id) {
        this.id = id;
        return this;
    }

    public String getCategoryID() {
        return this.categoryID;
    }

    public Categories categoryID(String categoryID) {
        this.categoryID = categoryID;
        return this;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public Categories categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Products> getProducts() {
        return this.products;
    }

    public Categories products(Set<Products> products) {
        this.setProducts(products);
        return this;
    }

    public Categories addProducts(Products products) {
        this.products.add(products);
        products.setCategories(this);
        return this;
    }

    public Categories removeProducts(Products products) {
        this.products.remove(products);
        products.setCategories(null);
        return this;
    }

    public void setProducts(Set<Products> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setCategories(null));
        }
        if (products != null) {
            products.forEach(i -> i.setCategories(this));
        }
        this.products = products;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categories)) {
            return false;
        }
        return id != null && id.equals(((Categories) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categories{" +
            "id=" + getId() +
            ", categoryID='" + getCategoryID() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            "}";
    }
}
