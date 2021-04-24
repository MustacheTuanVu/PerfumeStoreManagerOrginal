package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Stores.
 */
@Entity
@Table(name = "stores")
public class Stores implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id")
    private String storeID;

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "store_phone")
    private String storePhone;

    @Column(name = "store_adress")
    private String storeAdress;

    @Column(name = "store_rent")
    private Float storeRent;

    @OneToMany(mappedBy = "stores")
    @JsonIgnoreProperties(value = { "bills", "dayWorks", "stores", "roles" }, allowSetters = true)
    private Set<Member> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stores id(Long id) {
        this.id = id;
        return this;
    }

    public String getStoreID() {
        return this.storeID;
    }

    public Stores storeID(String storeID) {
        this.storeID = storeID;
        return this;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public Stores storeName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return this.storePhone;
    }

    public Stores storePhone(String storePhone) {
        this.storePhone = storePhone;
        return this;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAdress() {
        return this.storeAdress;
    }

    public Stores storeAdress(String storeAdress) {
        this.storeAdress = storeAdress;
        return this;
    }

    public void setStoreAdress(String storeAdress) {
        this.storeAdress = storeAdress;
    }

    public Float getStoreRent() {
        return this.storeRent;
    }

    public Stores storeRent(Float storeRent) {
        this.storeRent = storeRent;
        return this;
    }

    public void setStoreRent(Float storeRent) {
        this.storeRent = storeRent;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public Stores members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public Stores addMember(Member member) {
        this.members.add(member);
        member.setStores(this);
        return this;
    }

    public Stores removeMember(Member member) {
        this.members.remove(member);
        member.setStores(null);
        return this;
    }

    public void setMembers(Set<Member> members) {
        if (this.members != null) {
            this.members.forEach(i -> i.setStores(null));
        }
        if (members != null) {
            members.forEach(i -> i.setStores(this));
        }
        this.members = members;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stores)) {
            return false;
        }
        return id != null && id.equals(((Stores) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stores{" +
            "id=" + getId() +
            ", storeID='" + getStoreID() + "'" +
            ", storeName='" + getStoreName() + "'" +
            ", storePhone='" + getStorePhone() + "'" +
            ", storeAdress='" + getStoreAdress() + "'" +
            ", storeRent=" + getStoreRent() +
            "}";
    }
}
