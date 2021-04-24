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
 * Criteria class for the {@link com.coderkiemcom.perfumestoremanager.domain.DayWorks} entity. This class is used
 * in {@link com.coderkiemcom.perfumestoremanager.web.rest.DayWorksResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /day-works?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DayWorksCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter workID;

    private StringFilter userID;

    private IntegerFilter dayWork;

    private LongFilter memberId;

    public DayWorksCriteria() {}

    public DayWorksCriteria(DayWorksCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.workID = other.workID == null ? null : other.workID.copy();
        this.userID = other.userID == null ? null : other.userID.copy();
        this.dayWork = other.dayWork == null ? null : other.dayWork.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
    }

    @Override
    public DayWorksCriteria copy() {
        return new DayWorksCriteria(this);
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

    public StringFilter getWorkID() {
        return workID;
    }

    public StringFilter workID() {
        if (workID == null) {
            workID = new StringFilter();
        }
        return workID;
    }

    public void setWorkID(StringFilter workID) {
        this.workID = workID;
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

    public IntegerFilter getDayWork() {
        return dayWork;
    }

    public IntegerFilter dayWork() {
        if (dayWork == null) {
            dayWork = new IntegerFilter();
        }
        return dayWork;
    }

    public void setDayWork(IntegerFilter dayWork) {
        this.dayWork = dayWork;
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
        final DayWorksCriteria that = (DayWorksCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(workID, that.workID) &&
            Objects.equals(userID, that.userID) &&
            Objects.equals(dayWork, that.dayWork) &&
            Objects.equals(memberId, that.memberId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workID, userID, dayWork, memberId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DayWorksCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (workID != null ? "workID=" + workID + ", " : "") +
            (userID != null ? "userID=" + userID + ", " : "") +
            (dayWork != null ? "dayWork=" + dayWork + ", " : "") +
            (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }
}
