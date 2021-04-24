package com.coderkiemcom.perfumestoremanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coderkiemcom.perfumestoremanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillsDTO.class);
        BillsDTO billsDTO1 = new BillsDTO();
        billsDTO1.setId(1L);
        BillsDTO billsDTO2 = new BillsDTO();
        assertThat(billsDTO1).isNotEqualTo(billsDTO2);
        billsDTO2.setId(billsDTO1.getId());
        assertThat(billsDTO1).isEqualTo(billsDTO2);
        billsDTO2.setId(2L);
        assertThat(billsDTO1).isNotEqualTo(billsDTO2);
        billsDTO1.setId(null);
        assertThat(billsDTO1).isNotEqualTo(billsDTO2);
    }
}
