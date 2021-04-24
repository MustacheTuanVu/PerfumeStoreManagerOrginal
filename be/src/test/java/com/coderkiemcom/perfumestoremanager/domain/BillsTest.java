package com.coderkiemcom.perfumestoremanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.coderkiemcom.perfumestoremanager.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BillsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bills.class);
        Bills bills1 = new Bills();
        bills1.setId(1L);
        Bills bills2 = new Bills();
        bills2.setId(bills1.getId());
        assertThat(bills1).isEqualTo(bills2);
        bills2.setId(2L);
        assertThat(bills1).isNotEqualTo(bills2);
        bills1.setId(null);
        assertThat(bills1).isNotEqualTo(bills2);
    }
}
