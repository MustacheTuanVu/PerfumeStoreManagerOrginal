package com.coderkiemcom.perfumestoremanager.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.coderkiemcom.perfumestoremanager.domain.Bills} entity.
 */
public class BillsDTO implements Serializable {

    private Long id;

    private String billID;

    private String salesID;

    private LocalDate date;

    private Float discount;

    private Float vat;

    private Float payment;

    private Float total;

    private String customerID;

    private Integer status;

    private String description;

    private CustomersDTO customers;

    private MemberDTO member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getSalesID() {
        return salesID;
    }

    public void setSalesID(String salesID) {
        this.salesID = salesID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getVat() {
        return vat;
    }

    public void setVat(Float vat) {
        this.vat = vat;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(Float payment) {
        this.payment = payment;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomersDTO getCustomers() {
        return customers;
    }

    public void setCustomers(CustomersDTO customers) {
        this.customers = customers;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillsDTO)) {
            return false;
        }

        BillsDTO billsDTO = (BillsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillsDTO{" +
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
            ", customers=" + getCustomers() +
            ", member=" + getMember() +
            "}";
    }
}
