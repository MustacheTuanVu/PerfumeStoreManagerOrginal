package com.coderkiemcom.perfumestoremanager.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.coderkiemcom.perfumestoremanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DayWorksDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DayWorksDTO.class);
        DayWorksDTO dayWorksDTO1 = new DayWorksDTO();
        dayWorksDTO1.setId(1L);
        DayWorksDTO dayWorksDTO2 = new DayWorksDTO();
        assertThat(dayWorksDTO1).isNotEqualTo(dayWorksDTO2);
        dayWorksDTO2.setId(dayWorksDTO1.getId());
        assertThat(dayWorksDTO1).isEqualTo(dayWorksDTO2);
        dayWorksDTO2.setId(2L);
        assertThat(dayWorksDTO1).isNotEqualTo(dayWorksDTO2);
        dayWorksDTO1.setId(null);
        assertThat(dayWorksDTO1).isNotEqualTo(dayWorksDTO2);
    }
}
