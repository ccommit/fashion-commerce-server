package com.ccommit.fashionserver.service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * packageName    : com.ccommit.fashionserver.service
 * fileName       : PhoneNumCheckValidator
 * author         : juoiy
 * date           : 2023-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-24        juoiy       최초 생성
 */
public class PhoneNumCheckValidator implements ConstraintValidator<PhoneNumCheck, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$");
    }
}
