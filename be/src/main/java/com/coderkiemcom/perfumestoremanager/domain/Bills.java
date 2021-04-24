package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Bills.
 */
@Entity
@Table(name = "bills")
public class Bills implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_id")
    private String billID;

    @Column(name = "sales_id")
    private String salesID;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "vat")
    private Float vat;

    @Column(name = "payment")
    private Float payment;

    @Column(name = "total")
    private Float total;

    @Column(name = "customer_id")
    private String customerID;

    @Column(name = "status")
    private Integer status;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "bills")
    @JsonIgnoreProperties(value = { "bills", "products" }, allowSetters = true)
    private Set<OrderDetails> orderDetails = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "bills" }, allowSetters = true)
    private Customers customers;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bills", "dayWorks", "stores", "roles" }, allowSetters = true)
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bills id(Long id) {
        this.id = id;
        return this;
    }

    public String getBillID() {
        return this.billID;
    }

    public Bills billID(String billID) {
        this.billID = billID;
        return this;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getSalesID() {
        return this.salesID;
    }

    public Bills salesID(String salesID) {
        this.salesID = salesID;
        return this;
    }

    public void setSalesID(String salesID) {
        this.salesID = salesID;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Bills date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getDiscount() {
        return this.discount;
    }

    public Bills discount(Float discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getVat() {
        return this.vat;
    }

    public Bills vat(Float vat) {
        this.vat = vat;
        return this;
    }

    public void setVat(Float vat) {
        this.vat = vat;
    }

    public Float getPayment() {
        return this.payment;
    }

    public Bills payment(Float payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Float getTotal() {
        return this.total;
    }

    public Bills total(Float total) {
        this.total = total;
        return this;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getCustomerID() {
        return this.customerID;
    }

    public Bills customerID(String customerID) {
        this.customerID = customerID;
        return this;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Bills status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public Bills description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OrderDetails> getOrderDetails() {
        return this.orderDetails;
    }

    public Bills orderDetails(Set<OrderDetails> orderDetails) {
        this.setOrderDetails(orderDetails);
        return this;
    }

    public Bills addOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.add(orderDetails);
        orderDetails.setBills(this);
        return this;
    }

    public Bills removeOrderDetails(OrderDetails orderDetails) {
        this.orderDetails.remove(orderDetails);
        orderDetails.setBills(null);
        return this;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        if (this.orderDetails != null) {
            this.orderDetails.forEach(i -> i.setBills(null));
        }
        if (orderDetails != null) {
            orderDetails.forEach(i -> i.setBills(this));
        }
        this.orderDetails = orderDetails;
    }

    public Customers getCustomers() {
        return this.customers;
    }

    public Bills customers(Customers customers) {
        this.setCustomers(customers);
        return this;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Member getMember() {
        return this.member;
    }

    public Bills member(Member member) {
        this.setMember(member);
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bills)) {
            return false;
        }
        return id != null && id.equals(((Bills) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bills{" +
            "id=" + getId() +
            ", billID='" + getBillID() + "'" +
            ", salesID='" + getSalesID() + "'" +
            ", date='" + getDate() + "'" +
            ", discount=" + getDiscount() +
            ", vat=" + getVat() +
            ", payment=" + getPayment() +
            ", total=" + getTotal() +
            ", customerID='" + getCustomerID() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
