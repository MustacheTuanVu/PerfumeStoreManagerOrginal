package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userID;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "role_id")
    private String roleID;

    @Column(name = "store_id")
    private String storeID;

    @Column(name = "salary")
    private Float salary;

    @OneToMany(mappedBy = "member")
    @JsonIgnoreProperties(value = { "orderDetails", "customers", "member" }, allowSetters = true)
    private Set<Bills> bills = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    private Set<DayWorks> dayWorks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "members" }, allowSetters = true)
    private Stores stores;

    @ManyToOne
    @JsonIgnoreProperties(value = { "members" }, allowSetters = true)
    private Roles roles;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserID() {
        return this.userID;
    }

    public Member userID(String userID) {
        this.userID = userID;
        return this;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return this.name;
    }

    public Member name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Member phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Member email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleID() {
        return this.roleID;
    }

    public Member roleID(String roleID) {
        this.roleID = roleID;
        return this;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getStoreID() {
        return this.storeID;
    }

    public Member storeID(String storeID) {
        this.storeID = storeID;
        return this;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public Float getSalary() {
        return this.salary;
    }

    public Member salary(Float salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public Set<Bills> getBills() {
        return this.bills;
    }

    public Member bills(Set<Bills> bills) {
        this.setBills(bills);
        return this;
    }

    public Member addBills(Bills bills) {
        this.bills.add(bills);
        bills.setMember(this);
        return this;
    }

    public Member removeBills(Bills bills) {
        this.bills.remove(bills);
        bills.setMember(null);
        return this;
    }

    public void setBills(Set<Bills> bills) {
        if (this.bills != null) {
            this.bills.forEach(i -> i.setMember(null));
        }
        if (bills != null) {
            bills.forEach(i -> i.setMember(this));
        }
        this.bills = bills;
    }

    public Set<DayWorks> getDayWorks() {
        return this.dayWorks;
    }

    public Member dayWorks(Set<DayWorks> dayWorks) {
        this.setDayWorks(dayWorks);
        return this;
    }

    public Member addDayWorks(DayWorks dayWorks) {
        this.dayWorks.add(dayWorks);
        dayWorks.setMember(this);
        return this;
    }

    public Member removeDayWorks(DayWorks dayWorks) {
        this.dayWorks.remove(dayWorks);
        dayWorks.setMember(null);
        return this;
    }

    public void setDayWorks(Set<DayWorks> dayWorks) {
        if (this.dayWorks != null) {
            this.dayWorks.forEach(i -> i.setMember(null));
        }
        if (dayWorks != null) {
            dayWorks.forEach(i -> i.setMember(this));
        }
        this.dayWorks = dayWorks;
    }

    public Stores getStores() {
        return this.stores;
    }

    public Member stores(Stores stores) {
        this.setStores(stores);
        return this;
    }

    public void setStores(Stores stores) {
        this.stores = stores;
    }

    public Roles getRoles() {
        return this.roles;
    }

    public Member roles(Roles roles) {
        this.setRoles(roles);
        return this;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", userID='" + getUserID() + "'" +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", roleID='" + getRoleID() + "'" +
            ", storeID='" + getStoreID() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
