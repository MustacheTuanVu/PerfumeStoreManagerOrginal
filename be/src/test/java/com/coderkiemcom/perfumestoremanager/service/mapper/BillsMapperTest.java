package com.coderkiemcom.perfumestoremanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BillsMapperTest {

    private BillsMapper billsMapper;

    @BeforeEach
    public void setUp() {
        billsMapper = new BillsMapperImpl();
    }
}
