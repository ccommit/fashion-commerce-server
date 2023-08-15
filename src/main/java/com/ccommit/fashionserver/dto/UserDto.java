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
    private String phoneNumber;    //휴대폰번호
    private Date createDate;       //생성날짜
    private Date updateDate;       //수정날짜
    private int signStatus;        //가입상태
    private int isWithdraw;        //탈퇴상태
    // TINYINT 0은 false, 1은 true 로 해석
    // mysql TINYINT = java Integer 로 표현
    @NotBlank
    private String userId;         //아이디
    private int isAdmin;           //관리자여부


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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(int signStatus) {
        this.signStatus = signStatus;
    }

    public int getIsWithdraw() {
        return isWithdraw;
    }

    public void setIsWithdraw(int isWithdraw) {
        this.isWithdraw = isWithdraw;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
