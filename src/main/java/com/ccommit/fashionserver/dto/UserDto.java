package com.ccommit.fashionserver.dto;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

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

public class UserDto {
    private int id;                 //번호
    @NotBlank
    private String address;         //주소
    @NotBlank
    private String password;        //비밀번호
    @NotBlank
    private String phone_number;    //휴대폰번호
    private Date create_date;       //생성날짜
    private Date update_date;       //수정날짜
    private int sign_status;        //가입상태
    private int is_withdraw;        //탈퇴상태
    // TINYINT 0은 false, 1은 true 로 해석
    // mysql TINYINT = java Integer 로 표현
    @NotBlank
    private String user_id;         //아이디
    private int is_admin;           //관리자여부


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
    }

    public int getSign_status() {
        return sign_status;
    }

    public void setSign_status(int sign_status) {
        this.sign_status = sign_status;
    }

    public int getIs_withdraw() {
        return is_withdraw;
    }

    public void setIs_withdraw(int is_withdraw) {
        this.is_withdraw = is_withdraw;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }


}
