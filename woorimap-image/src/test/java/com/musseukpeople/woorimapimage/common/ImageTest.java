package com.musseukpeople.woorimapimage.common;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimapimage.common.model.ApiResponse;
import com.musseukpeople.woorimapimage.image.presentation.auth.WoorimapClient;

import io.findify.s3mock.S3Mock;

@Import(S3MockConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class ImageTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected WoorimapClient woorimapClient;

    @AfterAll
    public static void tearDown(@Autowired S3Mock s3Mock) {
        s3Mock.stop();
    }

    protected <T> T getResponseObject(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, type);
        ApiResponse<T> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }
}
