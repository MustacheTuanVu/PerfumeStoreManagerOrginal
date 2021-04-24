package com.coderkiemcom.perfumestoremanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Customers} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.CustomersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerID;

    private StringFilter customerName;

    private StringFilter address;

    private StringFilter phone;

    private StringFilter email;

    private IntegerFilter gender;

    private LongFilter billsId;

    public CustomersCriteria() {}

    public CustomersCriteria(CustomersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerID = other.customerID == null ? null : other.customerID.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.billsId = other.billsId == null ? null : other.billsId.copy();
    }

    @Override
    public CustomersCriteria copy() {
        return new CustomersCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCustomerID() {
        return customerID;
    }

    public StringFilter customerID() {
        if (customerID == null) {
            customerID = new StringFilter();
        }
        return customerID;
    }

    public void setCustomerID(StringFilter customerID) {
        this.customerID = customerID;
    }

    public StringFilter getCustomerName() {
        return customerName;
    }

    public StringFilter customerName() {
        if (customerName == null) {
            customerName = new StringFilter();
        }
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public IntegerFilter getGender() {
        return gender;
    }

    public IntegerFilter gender() {
        if (gender == null) {
            gender = new IntegerFilter();
        }
        return gender;
    }

    public void setGender(IntegerFilter gender) {
        this.gender = gender;
    }

    public LongFilter getBillsId() {
        return billsId;
    }

    public LongFilter billsId() {
        if (billsId == null) {
            billsId = new LongFilter();
        }
        return billsId;
    }

    public void setBillsId(LongFilter billsId) {
        this.billsId = billsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomersCriteria that = (CustomersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerID, that.customerID) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(billsId, that.billsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerID, customerName, address, phone, email, gender, billsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerID != null ? "customerID=" + customerID + ", " : "") +
            (customerName != null ? "customerName=" + customerName + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (billsId != null ? "billsId=" + billsId + ", " : "") +
            "}";
    }
}
