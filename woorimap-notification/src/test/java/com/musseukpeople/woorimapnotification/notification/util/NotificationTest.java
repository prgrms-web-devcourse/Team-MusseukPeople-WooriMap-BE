package com.musseukpeople.woorimapnotification.notification.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.musseukpeople.woorimapnotification.common.exception.ErrorResponse;
import com.musseukpeople.woorimapnotification.common.model.ApiResponse;
import com.musseukpeople.woorimapnotification.notification.presentation.auth.WoorimapClient;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class NotificationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected WoorimapClient woorimapClient;

    protected ErrorResponse getErrorResponse(MockHttpServletResponse response) throws IOException {
        return objectMapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
    }

    protected <T> List<T> getResponseList(MockHttpServletResponse response, Class<T> type) throws IOException {
        JavaType insideType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, insideType);
        ApiResponse<List<T>> result = objectMapper.readValue(response.getContentAsString(), javaType);
        return result.getData();
    }
}
