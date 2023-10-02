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

    int isExistId(String userId);

    int userWithdraw(int id);

    int userInfoUpdate(UserDto userDto);

    UserDto readUserInfo(String userId);

    int isJoinPossible(String userId, String joinPossibleDate);

    String getJoinPossibleDate(String userId);
}