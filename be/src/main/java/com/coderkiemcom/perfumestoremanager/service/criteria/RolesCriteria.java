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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.Roles} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.RolesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RolesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter roleID;

    private StringFilter roleName;

    private LongFilter memberId;

    public RolesCriteria() {}

    public RolesCriteria(RolesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.roleID = other.roleID == null ? null : other.roleID.copy();
        this.roleName = other.roleName == null ? null : other.roleName.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
    }

    @Override
    public RolesCriteria copy() {
        return new RolesCriteria(this);
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

    public StringFilter getRoleName() {
        return roleName;
    }

    public StringFilter roleName() {
        if (roleName == null) {
            roleName = new StringFilter();
        }
        return roleName;
    }

    public void setRoleName(StringFilter roleName) {
        this.roleName = roleName;
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
        final RolesCriteria that = (RolesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(roleID, that.roleID) &&
            Objects.equals(roleName, that.roleName) &&
            Objects.equals(memberId, that.memberId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleID, roleName, memberId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (roleID != null ? "roleID=" + roleID + ", " : "") +
            (roleName != null ? "roleName=" + roleName + ", " : "") +
            (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }
}
