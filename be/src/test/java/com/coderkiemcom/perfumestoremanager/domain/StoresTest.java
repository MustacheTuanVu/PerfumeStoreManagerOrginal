package com.coderkiemcom.perfumestoremanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.coderkiemcom.perfumestoremanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StoresTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stores.class);
        Stores stores1 = new Stores();
        stores1.setId(1L);
        Stores stores2 = new Stores();
        stores2.setId(stores1.getId());
        assertThat(stores1).isEqualTo(stores2);
        stores2.setId(2L);
        assertThat(stores1).isNotEqualTo(stores2);
        stores1.setId(null);
        assertThat(stores1).isNotEqualTo(stores2);
    }
}
