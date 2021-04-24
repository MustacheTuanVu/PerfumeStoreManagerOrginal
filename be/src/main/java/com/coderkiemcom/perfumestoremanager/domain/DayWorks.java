package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A DayWorks.
 */
@Entity
@Table(name = "day_works")
public class DayWorks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_id")
    private String workID;

    @Column(name = "user_id")
    private String userID;

    @Column(name = "day_work")
    private Integer dayWork;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bills", "dayWorks", "stores", "roles" }, allowSetters = true)
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayWorks id(Long id) {
        this.id = id;
        return this;
    }

    public String getWorkID() {
        return this.workID;
    }

    public DayWorks workID(String workID) {
        this.workID = workID;
        return this;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getUserID() {
        return this.userID;
    }

    public DayWorks userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getDayWork() {
        return this.dayWork;
    }

    public DayWorks dayWork(Integer dayWork) {
        this.dayWork = dayWork;
        return this;
    }

    public void setDayWork(Integer dayWork) {
        this.dayWork = dayWork;
    }

    public Member getMember() {
        return this.member;
    }

    public DayWorks member(Member member) {
        this.setMember(member);
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DayWorks)) {
            return false;
        }
        return id != null && id.equals(((DayWorks) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DayWorks{" +
            "id=" + getId() +
            ", workID='" + getWorkID() + "'" +
            ", userID='" + getUserID() + "'" +
            ", dayWork=" + getDayWork() +
            "}";
    }
}
