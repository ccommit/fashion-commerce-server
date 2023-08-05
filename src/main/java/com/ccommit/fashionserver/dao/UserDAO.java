package com.ccommit.fashionserver.dao;

import com.ccommit.fashionserver.dto.UserDto;
import org.springframework.stereotype.Repository;

/**
 *packageName    : com.ccommit.fashionserver.dao
 * fileName       : UserDAO
 * author         : juoiy
 * date           : 2023-08-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-04        juoiy       최초 생성
 */

@Repository
public class UserDAO {
    public UserDto signUp(UserDto userDto){
        UserDto resultDto = userDto;

        return resultDto;
    }

}
