package com.coderkiemcom.perfumestoremanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coderkiemcom.perfumestoremanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoresDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoresDTO.class);
        StoresDTO storesDTO1 = new StoresDTO();
        storesDTO1.setId(1L);
        StoresDTO storesDTO2 = new StoresDTO();
        assertThat(storesDTO1).isNotEqualTo(storesDTO2);
        storesDTO2.setId(storesDTO1.getId());
        assertThat(storesDTO1).isEqualTo(storesDTO2);
        storesDTO2.setId(2L);
        assertThat(storesDTO1).isNotEqualTo(storesDTO2);
        storesDTO1.setId(null);
        assertThat(storesDTO1).isNotEqualTo(storesDTO2);
    }
}
