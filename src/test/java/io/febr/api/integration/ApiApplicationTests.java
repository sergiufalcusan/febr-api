package io.febr.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiApplicationTests extends BaseIntegrationTest {
    @Test
    void contextLoads() {
        Assertions.assertTrue(postgresContainer.isRunning());
    }
}
