package com.musseukpeople.woorimap.util;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
