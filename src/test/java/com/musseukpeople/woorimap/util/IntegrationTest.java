package com.musseukpeople.woorimap.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.musseukpeople.woorimap.common.config.S3MockConfig;

import io.findify.s3mock.S3Mock;

@Import(S3MockConfig.class)
@SpringBootTest
public abstract class IntegrationTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterAll
    public static void tearDown(@Autowired S3Mock s3Mock) {
        s3Mock.stop();
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.execute();
    }
}
