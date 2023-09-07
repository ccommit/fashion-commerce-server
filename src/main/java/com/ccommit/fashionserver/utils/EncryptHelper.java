package com.ccommit.fashionserver.utils;

/**
 * packageName    : com.ccommit.fashionserver.utils
 * fileName       : EncryptHelper
 * author         : juoiy
 * date           : 2023-08-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-14        juoiy       최초 생성
 */
public interface EncryptHelper {
    public String hashPassword(String password);

    boolean isMach(String password, String hashed);
}
