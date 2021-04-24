package com.coderkiemcom.perfumestoremanager.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Bills} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.BillsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bills?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BillsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter billID;

    private StringFilter salesID;

    private LocalDateFilter date;

    private FloatFilter discount;

    private FloatFilter vat;

    private FloatFilter payment;

    private FloatFilter total;

    private StringFilter customerID;

    private IntegerFilter status;

    private StringFilter description;

    private LongFilter orderDetailsId;

    private LongFilter customersId;

    private LongFilter memberId;

    public BillsCriteria() {}

    public BillsCriteria(BillsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.billID = other.billID == null ? null : other.billID.copy();
        this.salesID = other.salesID == null ? null : other.salesID.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.discount = other.discount == null ? null : other.discount.copy();
        this.vat = other.vat == null ? null : other.vat.copy();
        this.payment = other.payment == null ? null : other.payment.copy();
        this.total = other.total == null ? null : other.total.copy();
        this.customerID = other.customerID == null ? null : other.customerID.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.orderDetailsId = other.orderDetailsId == null ? null : other.orderDetailsId.copy();
        this.customersId = other.customersId == null ? null : other.customersId.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
    }

    @Override
    public BillsCriteria copy() {
        return new BillsCriteria(this);
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

    public StringFilter getBillID() {
        return billID;
    }

    public StringFilter billID() {
        if (billID == null) {
            billID = new StringFilter();
        }
        return billID;
    }

    public void setBillID(StringFilter billID) {
        this.billID = billID;
    }

    public StringFilter getSalesID() {
        return salesID;
    }

    public StringFilter salesID() {
        if (salesID == null) {
            salesID = new StringFilter();
        }
        return salesID;
    }

    public void setSalesID(StringFilter salesID) {
        this.salesID = salesID;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public FloatFilter getDiscount() {
        return discount;
    }

    public FloatFilter discount() {
        if (discount == null) {
            discount = new FloatFilter();
        }
        return discount;
    }

    public void setDiscount(FloatFilter discount) {
        this.discount = discount;
    }

    public FloatFilter getVat() {
        return vat;
    }

    public FloatFilter vat() {
        if (vat == null) {
            vat = new FloatFilter();
        }
        return vat;
    }

    public void setVat(FloatFilter vat) {
        this.vat = vat;
    }

    public FloatFilter getPayment() {
        return payment;
    }

    public FloatFilter payment() {
        if (payment == null) {
            payment = new FloatFilter();
        }
        return payment;
    }

    public void setPayment(FloatFilter payment) {
        this.payment = payment;
    }

    public FloatFilter getTotal() {
        return total;
    }

    public FloatFilter total() {
        if (total == null) {
            total = new FloatFilter();
        }
        return total;
    }

    public void setTotal(FloatFilter total) {
        this.total = total;
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

    public IntegerFilter getStatus() {
        return status;
    }

    public IntegerFilter status() {
        if (status == null) {
            status = new IntegerFilter();
        }
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getOrderDetailsId() {
        return orderDetailsId;
    }

    public LongFilter orderDetailsId() {
        if (orderDetailsId == null) {
            orderDetailsId = new LongFilter();
        }
        return orderDetailsId;
    }

    public void setOrderDetailsId(LongFilter orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public LongFilter getCustomersId() {
        return customersId;
    }

    public LongFilter customersId() {
        if (customersId == null) {
            customersId = new LongFilter();
        }
        return customersId;
    }

    public void setCustomersId(LongFilter customersId) {
        this.customersId = customersId;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public LongFilter memberId() {
        if (memberId == null) {
            memberId = new LongFilter();
        }
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BillsCriteria that = (BillsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(billID, that.billID) &&
            Objects.equals(salesID, that.salesID) &&
            Objects.equals(date, that.date) &&
            Objects.equals(discount, that.discount) &&
            Objects.equals(vat, that.vat) &&
            Objects.equals(payment, that.payment) &&
            Objects.equals(total, that.total) &&
            Objects.equals(customerID, that.customerID) &&
            Objects.equals(status, that.status) &&
            Objects.equals(description, that.description) &&
            Objects.equals(orderDetailsId, that.orderDetailsId) &&
            Objects.equals(customersId, that.customersId) &&
            Objects.equals(memberId, that.memberId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            billID,
            salesID,
            date,
            discount,
            vat,
            payment,
            total,
            customerID,
            status,
            description,
            orderDetailsId,
            customersId,
            memberId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (billID != null ? "billID=" + billID + ", " : "") +
            (salesID != null ? "salesID=" + salesID + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (discount != null ? "discount=" + discount + ", " : "") +
            (vat != null ? "vat=" + vat + ", " : "") +
            (payment != null ? "payment=" + payment + ", " : "") +
            (total != null ? "total=" + total + ", " : "") +
            (customerID != null ? "customerID=" + customerID + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (orderDetailsId != null ? "orderDetailsId=" + orderDetailsId + ", " : "") +
            (customersId != null ? "customersId=" + customersId + ", " : "") +
            (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }
}
