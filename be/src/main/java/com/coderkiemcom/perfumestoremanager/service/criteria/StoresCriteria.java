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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Stores} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.StoresResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StoresCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter storeID;

    private StringFilter storeName;

    private StringFilter storePhone;

    private StringFilter storeAdress;

    private FloatFilter storeRent;

    private LongFilter memberId;

    public StoresCriteria() {}

    public StoresCriteria(StoresCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.storeID = other.storeID == null ? null : other.storeID.copy();
        this.storeName = other.storeName == null ? null : other.storeName.copy();
        this.storePhone = other.storePhone == null ? null : other.storePhone.copy();
        this.storeAdress = other.storeAdress == null ? null : other.storeAdress.copy();
        this.storeRent = other.storeRent == null ? null : other.storeRent.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
    }

    @Override
    public StoresCriteria copy() {
        return new StoresCriteria(this);
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

    public StringFilter getStoreID() {
        return storeID;
    }

    public StringFilter storeID() {
        if (storeID == null) {
            storeID = new StringFilter();
        }
        return storeID;
    }

    public void setStoreID(StringFilter storeID) {
        this.storeID = storeID;
    }

    public StringFilter getStoreName() {
        return storeName;
    }

    public StringFilter storeName() {
        if (storeName == null) {
            storeName = new StringFilter();
        }
        return storeName;
    }

    public void setStoreName(StringFilter storeName) {
        this.storeName = storeName;
    }

    public StringFilter getStorePhone() {
        return storePhone;
    }

    public StringFilter storePhone() {
        if (storePhone == null) {
            storePhone = new StringFilter();
        }
        return storePhone;
    }

    public void setStorePhone(StringFilter storePhone) {
        this.storePhone = storePhone;
    }

    public StringFilter getStoreAdress() {
        return storeAdress;
    }

    public StringFilter storeAdress() {
        if (storeAdress == null) {
            storeAdress = new StringFilter();
        }
        return storeAdress;
    }

    public void setStoreAdress(StringFilter storeAdress) {
        this.storeAdress = storeAdress;
    }

    public FloatFilter getStoreRent() {
        return storeRent;
    }

    public FloatFilter storeRent() {
        if (storeRent == null) {
            storeRent = new FloatFilter();
        }
        return storeRent;
    }

    public void setStoreRent(FloatFilter storeRent) {
        this.storeRent = storeRent;
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
        final StoresCriteria that = (StoresCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(storeID, that.storeID) &&
            Objects.equals(storeName, that.storeName) &&
            Objects.equals(storePhone, that.storePhone) &&
            Objects.equals(storeAdress, that.storeAdress) &&
            Objects.equals(storeRent, that.storeRent) &&
            Objects.equals(memberId, that.memberId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, storeID, storeName, storePhone, storeAdress, storeRent, memberId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoresCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (storeID != null ? "storeID=" + storeID + ", " : "") +
            (storeName != null ? "storeName=" + storeName + ", " : "") +
            (storePhone != null ? "storePhone=" + storePhone + ", " : "") +
            (storeAdress != null ? "storeAdress=" + storeAdress + ", " : "") +
            (storeRent != null ? "storeRent=" + storeRent + ", " : "") +
            (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }
}
