package com.ccommit.fashionserver.dto;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : CategoryType
 * author         : juoiy
 * date           : 2023-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-27        juoiy       최초 생성
 */
public enum CategoryType {
    CLOTHING("의류", 10),
    BAG("가방", 20),
    ACCESSORY("악세서리", 30),
    SHOES("신발", 40);

    private final String name;
    private final int number;

    CategoryType(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }
}
