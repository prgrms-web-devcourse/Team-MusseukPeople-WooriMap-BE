package com.musseukpeople.woorimap.image.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import com.google.common.io.Files;
import com.musseukpeople.woorimap.common.config.S3MockConfig;
import com.musseukpeople.woorimap.image.exception.NotSupportImageException;
import com.musseukpeople.woorimap.util.IntegrationTest;

import io.findify.s3mock.S3Mock;

@ActiveProfiles("test")
@Import(S3MockConfig.class)
class S3ImageServiceTest extends IntegrationTest {
    @Autowired
    private S3ImageService s3ImageService;

    @Autowired
    private S3Mock s3Mock;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @ParameterizedTest
    @CsvSource(value = {"mock.png, image/png", "mock.jpg, image/jpeg",
        "mock.jpeg, image/jpeg", "mock.gif, image/gif", "mock.bmp, image/bmp"})
    @DisplayName("s3 이미지 업로드 성공")
    void uploadImage(String imageName, String contentType) {
        //given
        String extension = Files.getFileExtension(imageName);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", imageName,
            contentType, "test data".getBytes());
        long memberId = 1L;
        //when
        String imageUrl = s3ImageService.uploadImage(memberId, mockMultipartFile);
        //then
        assertThat(imageUrl).contains(extension);
    }

    @ParameterizedTest
    @CsvSource(value = {"mock.txt, text/plain", "mock.html, text/html", "mock.pdf, application/pdf"})
    @DisplayName("s3 지원하지 않는 이미지 확장자일시 실패")
    void uploadImage_extension_fail(String imageName, String contentType) {
        //given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", imageName,
            contentType, "test data".getBytes());
        long memberId = 1L;
        //when, then
        assertThatThrownBy(() -> s3ImageService.uploadImage(memberId, mockMultipartFile))
            .isInstanceOf(NotSupportImageException.class);
    }

}
