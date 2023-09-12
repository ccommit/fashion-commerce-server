package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.dto.UserType;
import com.ccommit.fashionserver.mapper.UserMapper;
import com.ccommit.fashionserver.utils.BcryptEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
    private final UserMapper userMapper;

    @Autowired
    private final BcryptEncoder encrypt;

    public UserService(UserMapper userMapper, BcryptEncoder encrypt) {
        this.userMapper = userMapper;
        this.encrypt = encrypt;
    }

    public boolean isExistId(String userId) {
        return userMapper.isExistId(userId) == 1;
    }

    //회원가입
    public int signUp(UserDto userDto) {
        int result = 0;

        if (isExistId(userDto.getUserId())) {
            throw new RuntimeException("중복된 아이디 존재");
        } else {
            if(userMapper.isJoinPossible(userDto.getUserId()) == 1){
                throw new RuntimeException("탈퇴날짜 기준으로 30일 이내로 재가입 불가");
            }
            userDto.setPassword(encrypt.hashPassword(userDto.getPassword()));
            userDto.setPhoneNumber(userDto.getPhoneNumber());
            userDto.setJoin(true);
            userDto.setWithdraw(false);
            if (userDto.getUserType().equals(UserType.USER))
                userDto.setUserType(UserType.USER);
            else if (userDto.getUserType().equals(UserType.SELLER))
                userDto.setUserType(UserType.SELLER);
            else if (userDto.getUserType().equals(UserType.ADMIN))
                userDto.setUserType(UserType.ADMIN);
            result = userMapper.signUp(userDto);
        }
        return result;
    }

    public int userWithdraw(String userId) {
        int result = 0;
        result = userMapper.userWithdraw(userId);
        return result;
    }

    public int userInfoUpdate(UserDto userDto) {
        int result = 0;

        if (!isExistId(userDto.getUserId())) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        } else {
            if (!StringUtils.isBlank(userDto.getPassword())) {
                userDto.setPassword(encrypt.hashPassword(userDto.getPassword()));
            }
            if (!StringUtils.isBlank(userDto.getPhoneNumber())) {
                userDto.setPhoneNumber(userDto.getPhoneNumber());
            }
            if (!StringUtils.isBlank(userDto.getAddress())) {
                userDto.setAddress(userDto.getAddress());
            }
            if (userDto.getUserType().equals("user"))
                userDto.setUserType(UserType.USER);
            else if (userDto.getUserType().equals("seller"))
                userDto.setUserType(UserType.SELLER);
            else if (userDto.getUserType().equals(UserType.ADMIN))
                userDto.setUserType(UserType.ADMIN);
            result = userMapper.userInfoUpdate(userDto);
        }
        return result;
    }

    public boolean passwordCheck(String id, String password) {
        boolean result = false;
        String hashedPassword = "";
        if (!isExistId(id)) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        } else {
            hashedPassword = userMapper.readUserInfo(id).getPassword();
        }
        result = encrypt.isMach(password, hashedPassword);
        return result;
    }

    public void clearSession(HttpSession session) {
        session.invalidate();
    }
}
