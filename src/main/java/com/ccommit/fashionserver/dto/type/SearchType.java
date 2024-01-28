package com.ccommit.fashionserver.dto.type;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : SearchType
 * author         : juoiy
 * date           : 2023-09-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-05        juoiy       최초 생성
 */
public enum SearchType {
    NEW("NEW"),
    LOW_PRICE("LOW_PRICE"),
    HIGH_PRICE("HIGH_PRICE"),
    LIKE("LIKE");

    private final String name;

    SearchType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
