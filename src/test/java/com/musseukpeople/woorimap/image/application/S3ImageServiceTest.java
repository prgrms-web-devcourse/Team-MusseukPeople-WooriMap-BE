package com.musseukpeople.woorimap.image.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import com.google.common.io.Files;
import com.musseukpeople.woorimap.common.config.S3MockConfig;
import com.musseukpeople.woorimap.image.exception.NotSupportImageException;
import com.musseukpeople.woorimap.util.IntegrationTest;

import io.findify.s3mock.S3Mock;

@Import(S3MockConfig.class)
class S3ImageServiceTest extends IntegrationTest {
    @Autowired
    private S3ImageService s3ImageService;

    @AfterAll
    public static void tearDown(@Autowired S3Mock s3Mock) {
        s3Mock.stop();
    }

    @ParameterizedTest(name = "이미지 파일명 : {0}, 이미지 Content-Type: {1}")
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

    @ParameterizedTest(name = "이미지 파일명 : {0}, 이미지 Content-Type: {1}")
    @NullAndEmptySource
    @DisplayName("빈 공백이 들어왔을경우 실패")
    void uploadImage_nullOrBlank_fail(String imageName) {
        //given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", imageName,
            "image/jpeg", "test data".getBytes());
        long memberId = 1L;
        //when
        //then
        assertThatThrownBy(() -> s3ImageService.uploadImage(memberId, mockMultipartFile))
            .isInstanceOf(NotSupportImageException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"mock.txt, text/plain", "mock.html, text/html", "mock.pdf, application/pdf"})
    @DisplayName("지원하지 않는 이미지 확장자일시 실패")
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
