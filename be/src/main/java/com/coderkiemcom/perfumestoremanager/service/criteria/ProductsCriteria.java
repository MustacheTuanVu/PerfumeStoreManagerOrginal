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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Products} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.ProductsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter productID;

    private StringFilter productName;

    private IntegerFilter quantityAvailable;

    private FloatFilter price;

    private LocalDateFilter dateImport;

    private LocalDateFilter expireDate;

    private StringFilter description;

    private StringFilter categoryID;

    private FloatFilter volume;

    private LongFilter orderDetailsId;

    private LongFilter categoriesId;

    public ProductsCriteria() {}

    public ProductsCriteria(ProductsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.productID = other.productID == null ? null : other.productID.copy();
        this.productName = other.productName == null ? null : other.productName.copy();
        this.quantityAvailable = other.quantityAvailable == null ? null : other.quantityAvailable.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.dateImport = other.dateImport == null ? null : other.dateImport.copy();
        this.expireDate = other.expireDate == null ? null : other.expireDate.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.categoryID = other.categoryID == null ? null : other.categoryID.copy();
        this.volume = other.volume == null ? null : other.volume.copy();
        this.orderDetailsId = other.orderDetailsId == null ? null : other.orderDetailsId.copy();
        this.categoriesId = other.categoriesId == null ? null : other.categoriesId.copy();
    }

    @Override
    public ProductsCriteria copy() {
        return new ProductsCriteria(this);
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

    public StringFilter getProductName() {
        return productName;
    }

    public StringFilter productName() {
        if (productName == null) {
            productName = new StringFilter();
        }
        return productName;
    }

    public void setProductName(StringFilter productName) {
        this.productName = productName;
    }

    public IntegerFilter getQuantityAvailable() {
        return quantityAvailable;
    }

    public IntegerFilter quantityAvailable() {
        if (quantityAvailable == null) {
            quantityAvailable = new IntegerFilter();
        }
        return quantityAvailable;
    }

    public void setQuantityAvailable(IntegerFilter quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
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

    public LocalDateFilter getDateImport() {
        return dateImport;
    }

    public LocalDateFilter dateImport() {
        if (dateImport == null) {
            dateImport = new LocalDateFilter();
        }
        return dateImport;
    }

    public void setDateImport(LocalDateFilter dateImport) {
        this.dateImport = dateImport;
    }

    public LocalDateFilter getExpireDate() {
        return expireDate;
    }

    public LocalDateFilter expireDate() {
        if (expireDate == null) {
            expireDate = new LocalDateFilter();
        }
        return expireDate;
    }

    public void setExpireDate(LocalDateFilter expireDate) {
        this.expireDate = expireDate;
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

    public StringFilter getCategoryID() {
        return categoryID;
    }

    public StringFilter categoryID() {
        if (categoryID == null) {
            categoryID = new StringFilter();
        }
        return categoryID;
    }

    public void setCategoryID(StringFilter categoryID) {
        this.categoryID = categoryID;
    }

    public FloatFilter getVolume() {
        return volume;
    }

    public FloatFilter volume() {
        if (volume == null) {
            volume = new FloatFilter();
        }
        return volume;
    }

    public void setVolume(FloatFilter volume) {
        this.volume = volume;
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

    public LongFilter getCategoriesId() {
        return categoriesId;
    }

    public LongFilter categoriesId() {
        if (categoriesId == null) {
            categoriesId = new LongFilter();
        }
        return categoriesId;
    }

    public void setCategoriesId(LongFilter categoriesId) {
        this.categoriesId = categoriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductsCriteria that = (ProductsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(productID, that.productID) &&
            Objects.equals(productName, that.productName) &&
            Objects.equals(quantityAvailable, that.quantityAvailable) &&
            Objects.equals(price, that.price) &&
            Objects.equals(dateImport, that.dateImport) &&
            Objects.equals(expireDate, that.expireDate) &&
            Objects.equals(description, that.description) &&
            Objects.equals(categoryID, that.categoryID) &&
            Objects.equals(volume, that.volume) &&
            Objects.equals(orderDetailsId, that.orderDetailsId) &&
            Objects.equals(categoriesId, that.categoriesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            productID,
            productName,
            quantityAvailable,
            price,
            dateImport,
            expireDate,
            description,
            categoryID,
            volume,
            orderDetailsId,
            categoriesId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (productID != null ? "productID=" + productID + ", " : "") +
            (productName != null ? "productName=" + productName + ", " : "") +
            (quantityAvailable != null ? "quantityAvailable=" + quantityAvailable + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (dateImport != null ? "dateImport=" + dateImport + ", " : "") +
            (expireDate != null ? "expireDate=" + expireDate + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (categoryID != null ? "categoryID=" + categoryID + ", " : "") +
            (volume != null ? "volume=" + volume + ", " : "") +
            (orderDetailsId != null ? "orderDetailsId=" + orderDetailsId + ", " : "") +
            (categoriesId != null ? "categoriesId=" + categoriesId + ", " : "") +
            "}";
    }
}
