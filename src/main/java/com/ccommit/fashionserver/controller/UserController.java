package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.aop.LoginCheck;
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
    private final UserService userService;

    public static final Logger logger = LogManager.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @RequestMapping(method = RequestMethod.POST, path="")
     * 아래의 @PostMapping("")와 동일. Post일 경우 간결하게 원하면 지금처럼 작성하면 된다.
     */
    @PostMapping("/sign-up")
    public int signUp(@Valid UserDto userDto) {
        int result = result = userService.signUp(userDto);
        return result;
    }

    @PostMapping("/withdraw/{id}")
    @LoginCheck(types = {LoginCheck.UserType.USER})
    public int userWithdraw(Integer loginSession, @PathVariable("id") int id) {
        if(loginSession != id)
            throw new RuntimeException("로그인한 사용자와 탈퇴하려는 사용자가 일치하지 않습니다.");
        else
            return userService.userWithdraw(id);
    }

    @PatchMapping("/{id}")
    @LoginCheck(types = {LoginCheck.UserType.USER, LoginCheck.UserType.SELLER})
    public int userInfoUpdate(Integer loginSession, @PathVariable("id") int id, UserDto userDto) {
        int result = 0;
        if (StringUtils.isBlank(userDto.getUserId()))
            throw new NullPointerException("아이디가 빈 값입니다. 확인해주세요.");
        else
            if(loginSession == id)
                result = userService.userInfoUpdate(id, userDto);
            else
                throw new RuntimeException("로그인한 사용자와 수정하려는 사용자가 일치하지 않습니다.");
        return result;
    }

    @PostMapping("/login")
    public void login(UserDto userDto, HttpSession session) {
        if (StringUtils.isBlank(userDto.getUserId()) || StringUtils.isBlank(userDto.getPassword())) {
            throw new NullPointerException("아이디 또는 비밀번호가 빈 값입니다. 확인해주세요.");
        }
        logger.info("UserId : " + userDto.getUserId() + " Password: " + userDto.getPassword());
        UserDto userInfo = userService.passwordCheck(userDto.getUserId(), userDto.getPassword());
        if (userInfo.getId() == 0 || userInfo == null)
            logger.debug("Login failed");

        userService.insertSession(session, userInfo);
        logger.info("Login success = " + userInfo.getId());
    }

    @GetMapping("/logout")
    @LoginCheck(types = {LoginCheck.UserType.USER, LoginCheck.UserType.SELLER
            , LoginCheck.UserType.ADMIN})
    public void logout(Integer loginSession, HttpSession session) {
        userService.clearSession(session);
        logger.info("Logout success");
    }
}
