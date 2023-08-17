package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.regex.Pattern;

/**
 * packageName    : com.ccommit.fashionserver.controller
 * fileName       : UsersController
 * author         : juoiy (Windows current login user)
 * date           : 2023-07-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-07-27        juoiy       최초 생성
 */

/*
 *   @Controller + @ResponseBody = @RestController
 *   @ResponseBody를 붙여서 JSON을 만들었지만,
 *   @RestController로 쉽게 알아서 전송 가능한 문자열 만들어준다.
 * */
@RestController
@RequestMapping("/user") // 이곳으로 들어오는 api주소를 mapping, /api 주소로 받겠다(localhost:8080/user)
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @RequestMapping(method = RequestMethod.POST, path="")
     * 아래의 @PostMapping("")와 동일. Post일 경우 간결하게 원하면 지금처럼 작성하면 된다.
     */
    @PostMapping("/signUp")
    public int signUp(@Valid UserDto userDto) {
        int result = 0;
        String regexPhoneNum = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
        boolean isRegexPhoneNum = Pattern.matches(regexPhoneNum, userDto.getPhoneNumber());
        if (!isRegexPhoneNum) {
            System.out.println("휴대폰번호를 확인해주세요. (입력 예시:010-1234-1234) ");
        } else {
            result = userService.signUp(userDto);
        }
        return result;
    }

    @PostMapping("/userWithdraw")
    public int userWithdraw(@Valid String userId) {
        return userService.userWithdraw(userId);
    }

    @PatchMapping("")
    public int userInfoUpdate(UserDto userDto) {
        int result = 0;
        if (StringUtils.isBlank(userDto.getUserId()))
            System.out.println("회원아이디 없음.");
        else
            result = userService.userInfoUpdate(userDto);
        return result;
    }

    //TODO: kakao 로그인
    @PostMapping("/kakao-login")
    public void kakaoLogin() {
    }
}
