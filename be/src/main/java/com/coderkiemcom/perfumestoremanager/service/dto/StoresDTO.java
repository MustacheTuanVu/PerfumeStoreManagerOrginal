package com.coderkiemcom.perfumestoremanager.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.coderkiemcom.perfumestoremanager.domain.Stores} entity.
 */
public class StoresDTO implements Serializable {

    private Long id;

    private String storeID;

    private String storeName;

    private String storePhone;

    private String storeAdress;

    private Float storeRent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAdress() {
        return storeAdress;
    }

    public void setStoreAdress(String storeAdress) {
        this.storeAdress = storeAdress;
    }

    public Float getStoreRent() {
        return storeRent;
    }

    public void setStoreRent(Float storeRent) {
        this.storeRent = storeRent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoresDTO)) {
            return false;
        }

        StoresDTO storesDTO = (StoresDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoresDTO{" +
            "id=" + getId() +
            ", storeID='" + getStoreID() + "'" +
            ", storeName='" + getStoreName() + "'" +
            ", storePhone='" + getStorePhone() + "'" +
            ", storeAdress='" + getStoreAdress() + "'" +
            ", storeRent=" + getStoreRent() +
            "}";
    }
}
