package com.musseukpeople.woorimap.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageUrlValidator implements ConstraintValidator<ImageUrl, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return ImageUtil.isImageUrl(value);
    }
}
