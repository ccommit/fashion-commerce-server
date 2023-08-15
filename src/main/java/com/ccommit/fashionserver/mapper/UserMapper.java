package com.ccommit.fashionserver.mapper;

import com.ccommit.fashionserver.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * packageName    : com.ccommit.fashionserver.mapper
 * fileName       : userMapper
 * author         : juoiy
 * date           : 2023-08-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-05        juoiy       최초 생성
 */
@Mapper
public interface UserMapper {
    int signUp(UserDto userDto);
    int idCheck(String user_id);
    int userWithdraw(String user_id);
}