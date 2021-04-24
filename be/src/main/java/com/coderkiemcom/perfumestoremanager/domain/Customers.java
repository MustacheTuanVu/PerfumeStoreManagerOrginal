package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Customers.
 */
@Entity
@Table(name = "customers")
public class Customers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private String customerID;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private Integer gender;

    @OneToMany(mappedBy = "customers")
    @JsonIgnoreProperties(value = { "orderDetails", "customers", "member" }, allowSetters = true)
    private Set<Bills> bills = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customers id(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public Customers customerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Customers customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return this.address;
    }

    public Customers address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Customers phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Customers email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return this.gender;
    }

    public Customers gender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Set<Bills> getBills() {
        return this.bills;
    }

    public Customers bills(Set<Bills> bills) {
        this.setBills(bills);
        return this;
    }

    public Customers addBills(Bills bills) {
        this.bills.add(bills);
        bills.setCustomers(this);
        return this;
    }

    public Customers removeBills(Bills bills) {
        this.bills.remove(bills);
        bills.setCustomers(null);
        return this;
    }

    public void setBills(Set<Bills> bills) {
        if (this.bills != null) {
            this.bills.forEach(i -> i.setCustomers(null));
        }
        if (bills != null) {
            bills.forEach(i -> i.setCustomers(this));
        }
        this.bills = bills;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customers)) {
            return false;
        }
        return id != null && id.equals(((Customers) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customers{" +
            "id=" + getId() +
            ", customerID='" + getCustomerID() + "'" +
            ", customerName='" + getCustomerName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", gender=" + getGender() +
            "}";
    }
}
