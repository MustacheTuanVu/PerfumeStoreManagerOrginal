package com.coderkiemcom.perfumestoremanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoresMapperTest {

    private StoresMapper storesMapper;

    @BeforeEach
    public void setUp() {
        storesMapper = new StoresMapperImpl();
    }
}
