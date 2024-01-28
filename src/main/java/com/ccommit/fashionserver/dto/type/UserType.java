package com.ccommit.fashionserver.dto.type;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : UserType
 * author         : juoiy
 * date           : 2023-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-23        juoiy       최초 생성
 */
public enum UserType {
    USER("USER"),
    SELLER("SELLER"),
    ADMIN("ADMIN");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
