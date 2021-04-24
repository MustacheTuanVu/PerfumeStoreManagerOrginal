package com.coderkiemcom.perfumestoremanager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.coderkiemcom.perfumestoremanager.domain.DayWorks} entity.
 */
public class DayWorksDTO implements Serializable {

    private Long id;

    private String workID;

    private String userID;

    private Integer dayWork;

    private MemberDTO member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getDayWork() {
        return dayWork;
    }

    public void setDayWork(Integer dayWork) {
        this.dayWork = dayWork;
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
        if (!(o instanceof DayWorksDTO)) {
            return false;
        }

        DayWorksDTO dayWorksDTO = (DayWorksDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dayWorksDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DayWorksDTO{" +
            "id=" + getId() +
            ", workID='" + getWorkID() + "'" +
            ", userID='" + getUserID() + "'" +
            ", dayWork=" + getDayWork() +
            ", member=" + getMember() +
            "}";
    }
}
