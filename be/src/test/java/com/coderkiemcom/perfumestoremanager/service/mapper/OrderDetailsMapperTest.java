package com.coderkiemcom.perfumestoremanager.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderDetailsMapperTest {

    private OrderDetailsMapper orderDetailsMapper;

    @BeforeEach
    public void setUp() {
        orderDetailsMapper = new OrderDetailsMapperImpl();
    }
}
