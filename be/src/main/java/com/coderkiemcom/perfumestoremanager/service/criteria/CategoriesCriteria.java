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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Categories} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.CategoriesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoriesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter categoryID;

    private StringFilter categoryName;

    private LongFilter productsId;

    public CategoriesCriteria() {}

    public CategoriesCriteria(CategoriesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.categoryID = other.categoryID == null ? null : other.categoryID.copy();
        this.categoryName = other.categoryName == null ? null : other.categoryName.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
    }

    @Override
    public CategoriesCriteria copy() {
        return new CategoriesCriteria(this);
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

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public StringFilter categoryName() {
        if (categoryName == null) {
            categoryName = new StringFilter();
        }
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
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
        final CategoriesCriteria that = (CategoriesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(categoryID, that.categoryID) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(productsId, that.productsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoryID, categoryName, productsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (categoryID != null ? "categoryID=" + categoryID + ", " : "") +
            (categoryName != null ? "categoryName=" + categoryName + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            "}";
    }
}
