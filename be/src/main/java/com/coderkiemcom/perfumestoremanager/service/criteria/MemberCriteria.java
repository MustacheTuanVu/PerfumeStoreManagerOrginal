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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Member} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.MemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter userID;

    private StringFilter name;

    private StringFilter phone;

    private StringFilter email;

    private StringFilter roleID;

    private StringFilter storeID;

    private FloatFilter salary;

    private LongFilter billsId;

    private LongFilter dayWorksId;

    private LongFilter storesId;

    private LongFilter rolesId;

    public MemberCriteria() {}

    public MemberCriteria(MemberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userID = other.userID == null ? null : other.userID.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.roleID = other.roleID == null ? null : other.roleID.copy();
        this.storeID = other.storeID == null ? null : other.storeID.copy();
        this.salary = other.salary == null ? null : other.salary.copy();
        this.billsId = other.billsId == null ? null : other.billsId.copy();
        this.dayWorksId = other.dayWorksId == null ? null : other.dayWorksId.copy();
        this.storesId = other.storesId == null ? null : other.storesId.copy();
        this.rolesId = other.rolesId == null ? null : other.rolesId.copy();
    }

    @Override
    public MemberCriteria copy() {
        return new MemberCriteria(this);
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

    public StringFilter getUserID() {
        return userID;
    }

    public StringFilter userID() {
        if (userID == null) {
            userID = new StringFilter();
        }
        return userID;
    }

    public void setUserID(StringFilter userID) {
        this.userID = userID;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getRoleID() {
        return roleID;
    }

    public StringFilter roleID() {
        if (roleID == null) {
            roleID = new StringFilter();
        }
        return roleID;
    }

    public void setRoleID(StringFilter roleID) {
        this.roleID = roleID;
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

    public FloatFilter getSalary() {
        return salary;
    }

    public FloatFilter salary() {
        if (salary == null) {
            salary = new FloatFilter();
        }
        return salary;
    }

    public void setSalary(FloatFilter salary) {
        this.salary = salary;
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

    public LongFilter getDayWorksId() {
        return dayWorksId;
    }

    public LongFilter dayWorksId() {
        if (dayWorksId == null) {
            dayWorksId = new LongFilter();
        }
        return dayWorksId;
    }

    public void setDayWorksId(LongFilter dayWorksId) {
        this.dayWorksId = dayWorksId;
    }

    public LongFilter getStoresId() {
        return storesId;
    }

    public LongFilter storesId() {
        if (storesId == null) {
            storesId = new LongFilter();
        }
        return storesId;
    }

    public void setStoresId(LongFilter storesId) {
        this.storesId = storesId;
    }

    public LongFilter getRolesId() {
        return rolesId;
    }

    public LongFilter rolesId() {
        if (rolesId == null) {
            rolesId = new LongFilter();
        }
        return rolesId;
    }

    public void setRolesId(LongFilter rolesId) {
        this.rolesId = rolesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberCriteria that = (MemberCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userID, that.userID) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(roleID, that.roleID) &&
            Objects.equals(storeID, that.storeID) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(billsId, that.billsId) &&
            Objects.equals(dayWorksId, that.dayWorksId) &&
            Objects.equals(storesId, that.storesId) &&
            Objects.equals(rolesId, that.rolesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userID, name, phone, email, roleID, storeID, salary, billsId, dayWorksId, storesId, rolesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userID != null ? "userID=" + userID + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (roleID != null ? "roleID=" + roleID + ", " : "") +
            (storeID != null ? "storeID=" + storeID + ", " : "") +
            (salary != null ? "salary=" + salary + ", " : "") +
            (billsId != null ? "billsId=" + billsId + ", " : "") +
            (dayWorksId != null ? "dayWorksId=" + dayWorksId + ", " : "") +
            (storesId != null ? "storesId=" + storesId + ", " : "") +
            (rolesId != null ? "rolesId=" + rolesId + ", " : "") +
            "}";
    }
}
