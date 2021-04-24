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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.OrderDetails} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.OrderDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /order-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter orderID;

    private StringFilter billID;

    private StringFilter productID;

    private IntegerFilter quantity;

    private FloatFilter price;

    private LongFilter billsId;

    private LongFilter productsId;

    public OrderDetailsCriteria() {}

    public OrderDetailsCriteria(OrderDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderID = other.orderID == null ? null : other.orderID.copy();
        this.billID = other.billID == null ? null : other.billID.copy();
        this.productID = other.productID == null ? null : other.productID.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.billsId = other.billsId == null ? null : other.billsId.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
    }

    @Override
    public OrderDetailsCriteria copy() {
        return new OrderDetailsCriteria(this);
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

    public StringFilter getOrderID() {
        return orderID;
    }

    public StringFilter orderID() {
        if (orderID == null) {
            orderID = new StringFilter();
        }
        return orderID;
    }

    public void setOrderID(StringFilter orderID) {
        this.orderID = orderID;
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

    public StringFilter getProductID() {
        return productID;
    }

    public StringFilter productID() {
        if (productID == null) {
            productID = new StringFilter();
        }
        return productID;
    }

    public void setProductID(StringFilter productID) {
        this.productID = productID;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
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

    public LongFilter getProductsId() {
        return productsId;
    }

    public LongFilter productsId() {
        if (productsId == null) {
            productsId = new LongFilter();
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderDetailsCriteria that = (OrderDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderID, that.orderID) &&
            Objects.equals(billID, that.billID) &&
            Objects.equals(productID, that.productID) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(price, that.price) &&
            Objects.equals(billsId, that.billsId) &&
            Objects.equals(productsId, that.productsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderID, billID, productID, quantity, price, billsId, productsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderID != null ? "orderID=" + orderID + ", " : "") +
            (billID != null ? "billID=" + billID + ", " : "") +
            (productID != null ? "productID=" + productID + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (billsId != null ? "billsId=" + billsId + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            "}";
    }
}
