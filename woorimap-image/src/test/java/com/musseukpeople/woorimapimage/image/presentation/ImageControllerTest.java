package com.musseukpeople.woorimapimage.image.presentation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import com.musseukpeople.woorimapimage.common.ImageTest;
import com.musseukpeople.woorimapimage.common.model.ApiResponse;
import com.musseukpeople.woorimapimage.image.presentation.auth.login.MemberResponse;

class ImageControllerTest extends ImageTest {

    @BeforeEach
    void setUp() {
        given(woorimapClient.getMemberResponse(anyString()))
            .willReturn(new ApiResponse<>(new MemberResponse(1L, "test@gmail.com", null, "test", false)));
    }

    @DisplayName("단일 이미지 업로드 성공")
    @Test
    void uploadImage_success() throws Exception {
        // given
        String token = "accessToken";
        MockMultipartFile imageFile = getImageFile("file", "test.png");

        // when
        MockHttpServletResponse response = mockMvc.perform(multipart(HttpMethod.POST, "/api/image")
                .file(imageFile)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andReturn().getResponse();

        // then
        String imageUrl = getResponseObject(response, String.class);
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(imageUrl).isNotNull()
        );
    }

    @DisplayName("다중 이미지 업로드 성공")
    @Test
    void uploadImages_success() throws Exception {
        // given
        String token = "accessToken";
        MockMultipartFile imageFile = getImageFile("files", "test.png");
        MockMultipartFile imageFile2 = getImageFile("files", "test2.png");

        // when
        MockHttpServletResponse response = mockMvc.perform(multipart(HttpMethod.POST, "/api/images")
                .file(imageFile)
                .file(imageFile2)
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andReturn().getResponse();

        // then
        assertAll(
            () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value())
        );
    }

    private MockMultipartFile getImageFile(String fileName, String originalFilename) throws IOException {
        return new MockMultipartFile(fileName, originalFilename, IMAGE_PNG_VALUE, getImageByte(originalFilename));
    }

    private byte[] getImageByte(String imageName) throws IOException {
        return IOUtils.toByteArray(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imageName)));
    }
}
