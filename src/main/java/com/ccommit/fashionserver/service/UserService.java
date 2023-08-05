package com.ccommit.fashionserver.service;

import com.ccommit.fashionserver.dao.UserDAO;
import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

//    @Autowired
//    UserDAO userDAO;

    @Autowired
    UserMapper userMapper;
    //중복된 아이디

    //회원가입
    public UserDto signUp(UserDto userDto){
        //UserDto result =userDto;
        // 들어왔어.
        // 값을 담아서 넘겨야겠지?
        //
        UserDto result = userMapper.register(userDto);
        return result;
    }


}
