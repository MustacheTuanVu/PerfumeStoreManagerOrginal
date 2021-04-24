package com.coderkiemcom.perfumestoremanager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.coderkiemcom.perfumestoremanager.domain.Roles} entity.
 */
public class RolesDTO implements Serializable {

    private Long id;

    private String roleID;

    private String roleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RolesDTO)) {
            return false;
        }

        RolesDTO rolesDTO = (RolesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rolesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RolesDTO{" +
            "id=" + getId() +
            ", roleID='" + getRoleID() + "'" +
            ", roleName='" + getRoleName() + "'" +
            "}";
    }
}
