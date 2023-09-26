package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.controller.UserController;
import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.dto.UserType;
import com.ccommit.fashionserver.exception.DuplicateException;
import com.ccommit.fashionserver.exception.PermissionDeniedException;
import com.ccommit.fashionserver.mapper.UserMapper;
import com.ccommit.fashionserver.utils.BcryptEncoder;
import com.ccommit.fashionserver.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Arrays;

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

    public static final Logger logger = LogManager.getLogger(UserController.class);

    public UserService(UserMapper userMapper, BcryptEncoder encrypt) {
        this.userMapper = userMapper;
        this.encrypt = encrypt;
    }

    public boolean isExistId(String userId) {
        return userMapper.isExistId(userId) == 1;
    }

    //회원가입
    public UserDto signUp(UserDto userDto) {
        UserDto result = new UserDto();
        if (isExistId(userDto.getUserId())) {
            throw new DuplicateException("중복된 아이디입니다. 확인해주세요.");
        } else {
            if (userMapper.isJoinPossible(userDto.getUserId()) == 1) {
                logger.debug("탈퇴날짜 기준으로 30일 이내로 재가입 불가");
                throw new PermissionDeniedException("가입 권한이 없습니다.");
            }
            userDto.setPassword(encrypt.hashPassword(userDto.getPassword()));
            userDto.setPhoneNumber(userDto.getPhoneNumber());
            userDto.setJoin(true);
            userDto.setWithdraw(false);
            Arrays.stream(UserType.values())
                    .filter(userType -> userDto.getUserType().equals(userType.getName()))
                    .forEach(userType -> {
                        if (userDto.getUserType().equals(userType.getName())) {
                            userDto.setUserType(userType);
                        } else {
                            logger.debug("존재하지 않는 회원 타입입니다.");
                            throw new NullPointerException("존재하지 않는 회원 타입입니다.");
                        }
                    });
            userMapper.signUp(userDto);
            result = userMapper.readUserInfo(userDto.getUserId());
        }
        return result;
    }

    public int userWithdraw(int id) {
        return userMapper.userWithdraw(id);
    }

    public int userInfoUpdate(int id, UserDto userDto) {
        int result = 0;

        if (!isExistId(userDto.getUserId())) {
            throw new NullPointerException("존재하지 않는 회원입니다.");
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
            userDto.setId(id);
            result = userMapper.userInfoUpdate(userDto);
        }
        return result;
    }

    public UserDto passwordCheck(String userId, String password) {
        UserDto result = new UserDto();
        boolean isMachPassword = false;
        String hashedPassword = "";
        if (!isExistId(userId))
            throw new NullPointerException("존재하지 않는 회원입니다.");
        else
            hashedPassword = userMapper.readUserInfo(userId).getPassword();

        if (StringUtils.isBlank(hashedPassword))
            throw new NullPointerException("회원 정보가 없습니다.");
        else
            isMachPassword = encrypt.isMach(password, hashedPassword);

        if (isMachPassword)
            result = userMapper.readUserInfo(userId);
        return result;
    }

    public void insertSession(HttpSession session, UserDto userDto) {
        if (userDto.getUserType() == UserType.USER)
            SessionUtils.setUserLoginSession(session, userDto.getId());
        else if (userDto.getUserType() == UserType.SELLER)
            SessionUtils.setSellerLoginSession(session, userDto.getId());
        else if (userDto.getUserType() == UserType.ADMIN)
            SessionUtils.setAdminLoginSession(session, userDto.getId());
    }

    public void clearSession(HttpSession session) {
        SessionUtils.clearSession(session);
    }
}
