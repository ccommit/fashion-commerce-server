package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.mapper.UserMapper;
import com.ccommit.fashionserver.utils.BcryptEncoder;
import org.mindrot.jbcrypt.BCrypt;
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

    @Autowired
    BcryptEncoder encrypt;


    // 중복아이디 체크
    public boolean idCheck(String user_id){
        return userMapper.idCheck(user_id) == 1;
    }

    //회원가입
    public int signUp(UserDto userDto){
        int result = 0;
        // 중복회원 유무 체크
        if (idCheck(userDto.getUserId())){
            System.out.println("중복된 아이디 존재");
            //예외처리
        }else {
            userDto.setPassword(encrypt.hashPassword(userDto.getPassword()));
            userDto.setPhoneNumber(userDto.getPhoneNumber());
            userDto.setSignStatus(1);      //가입상태 {0:미가입, 1:가입}
            userDto.setIsAdmin(10);        //관리자여부 {10:회원, 20:판매자, 30:관리자}
            userDto.setIsWithdraw(1);     //탈퇴상태 {1:회원, 0:탈퇴}
            result = userMapper.signUp(userDto);
        }
        return result;
    }


    //회원 탈퇴
    public int userWithdraw(String userId){
        int result = 0;
        result = userMapper.userWithdraw(userId);
        return result;
    }

    //회원수정


}
