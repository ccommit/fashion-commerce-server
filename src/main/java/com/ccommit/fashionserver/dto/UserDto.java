package com.ccommit.fashionserver.dto;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * packageName    : com.ccommit.fashionserver.dto
 * fileName       : UserDto
 * author         : juoiy
 * date           : 2023-07-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-27        juoiy       최초 생성
 */
/*
 * 번호
 * 아이디
 * 비밀번호
 * 휴대폰번호
 * 생성날짜
 * 수정날짜
 * 가입날짜
 * 탈퇴상태
 * 관리자여부
 */
/*@Getter
@Setter*/
public class UserDto {

    private String id;
    private String user_id;
    private String address;
    private String password;
    private String phone_number;
    private String create_date;
    private String update_date;
    private String sign_status;
    private String is_withdraw;
    private String is_admin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getSign_status() {
        return sign_status;
    }

    public void setSign_status(String sign_status) {
        this.sign_status = sign_status;
    }

    public String getIs_withdraw() {
        return is_withdraw;
    }

    public void setIs_withdraw(String is_withdraw) {
        this.is_withdraw = is_withdraw;
    }

    public String getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(String is_admin) {
        this.is_admin = is_admin;
    }

    //회원가입 시 null 유효성 체크

}
