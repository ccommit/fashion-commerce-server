package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * packageName    : com.ccommit.fashionserver.service
 * fileName       : UserService
 * author         : juoiy
 * date           : 2023-07-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-27        juoiy       최초 생성
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    // 중복아이디 체크
    public boolean idCheck(String user_id){
        return userMapper.idCheck(user_id) == 1;
    }

    //회원가입
    public int signUp(UserDto userDto){
        int result = 0;
        // 중복회원 유무 체크
        if (idCheck(userDto.getUser_id())){
            System.out.println("중복된 아이디 존재");
            //예외처리
        }else {
            userDto.setPassword(userDto.getPassword());
            userDto.setPhone_number(userDto.getPhone_number());
            userDto.setSign_status(1);      //가입상태 {0:미가입, 1:가입}
            userDto.setIs_admin(1);         //탈퇴상태 {1:회원, 0:탈퇴}
            userDto.setIs_withdraw(10);     //관리자여부 {10:회원, 20:판매자, 30:관리자}
            result = userMapper.signUp(userDto);
        }

        return result;
    }


}
