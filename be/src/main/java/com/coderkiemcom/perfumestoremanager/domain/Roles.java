package com.coderkiemcom.perfumestoremanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Roles.
 */
@Entity
@Table(name = "roles")
public class Roles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id")
    private String roleID;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "bills", "dayWorks", "stores", "roles" }, allowSetters = true)
    private Set<Member> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Roles id(Long id) {
        this.id = id;
        return this;
    }

    public String getRoleID() {
        return this.roleID;
    }

    public Roles roleID(String roleID) {
        this.roleID = roleID;
        return this;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public Roles roleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public Roles members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public Roles addMember(Member member) {
        this.members.add(member);
        member.setRoles(this);
        return this;
    }

    public Roles removeMember(Member member) {
        this.members.remove(member);
        member.setRoles(null);
        return this;
    }

    public void setMembers(Set<Member> members) {
        if (this.members != null) {
            this.members.forEach(i -> i.setRoles(null));
        }
        if (members != null) {
            members.forEach(i -> i.setRoles(this));
        }
        this.members = members;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Roles)) {
            return false;
        }
        return id != null && id.equals(((Roles) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Roles{" +
            "id=" + getId() +
            ", roleID='" + getRoleID() + "'" +
            ", roleName='" + getRoleName() + "'" +
            "}";
    }
}
