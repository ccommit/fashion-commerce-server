package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

/**
 * @Controller + @ResponseBody = @RestController
 * @ResponseBody를 붙여서 JSON을 만들었지만,
 * @RestController로 쉽게 알아서 전송 가능한 문자열 만들어준다.
 */
@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    public static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * @RequestMapping(method = RequestMethod.POST, path="")
     * 아래의 @PostMapping("")와 동일. Post일 경우 간결하게 원하면 지금처럼 작성하면 된다.
     */
    @PostMapping("/sign-up")
    public int signUp(@Valid UserDto userDto) {
        int result = result = userService.signUp(userDto);
        return result;
    }

    @PostMapping("/withdraw")
    public int userWithdraw(@Valid String userId) {
        return userService.userWithdraw(userId);
    }

    @PatchMapping("")
    public int userInfoUpdate(UserDto userDto) {
        int result = 0;
        if (StringUtils.isBlank(userDto.getUserId()))
            logger.debug("log4j2 INFO: 아이디가 빈 값입니다. 확인해주세요.");
        else
            result = userService.userInfoUpdate(userDto);
        return result;
    }

    @PostMapping("/login")
    public void login(String id, String password, HttpSession session) {
        boolean loginCheckResult = false;
        if (StringUtils.isBlank(id) || StringUtils.isBlank(password)) {
            logger.debug("log4j2 INFO: 아이디 또는 비밀번호가 빈 값입니다. 확인해주세요.");
            throw new RuntimeException("아이디 또는 비밀번호가 빈 값입니다. 확인해주세요.");
        }
        loginCheckResult = userService.passwordCheck(id, password);
        if (loginCheckResult) {
            session.setAttribute("userId", id);
            logger.debug("log4j2 DEBUG: 로그인 성공");
        } else {
            logger.debug("log4j2 DEBUG: 로그인 실패");
        }
        logger.info("로그인 세션 = " + session.getAttribute("userId"));
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        userService.clearSession(session);
        logger.info("log4j2 INFO: 로그아웃");
    }
}
