package com.coderkiemcom.perfumestoremanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.coderkiemcom.perfumestoremanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DayWorksTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DayWorks.class);
        DayWorks dayWorks1 = new DayWorks();
        dayWorks1.setId(1L);
        DayWorks dayWorks2 = new DayWorks();
        dayWorks2.setId(dayWorks1.getId());
        assertThat(dayWorks1).isEqualTo(dayWorks2);
        dayWorks2.setId(2L);
        assertThat(dayWorks1).isNotEqualTo(dayWorks2);
        dayWorks1.setId(null);
        assertThat(dayWorks1).isNotEqualTo(dayWorks2);
    }
}
