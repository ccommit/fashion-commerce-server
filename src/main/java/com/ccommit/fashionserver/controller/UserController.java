package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.aop.CommonResponse;
import com.ccommit.fashionserver.aop.LoginCheck;
import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * @RequestMapping(method = RequestMethod.POST, path="")
     * 아래의 @PostMapping("")와 동일. Post일 경우 간결하게 원하면 지금처럼 작성하면 된다.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse<UserDto>> signUp(@Valid @RequestBody UserDto userDto) {
        log.debug("Sign Up Start");
        UserDto userDtoResult = userService.signUp(userDto);
        CommonResponse<UserDto> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", userDto.getUserId() + "님 정상적으로 가입되었습니다.", userDtoResult);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw/{id}")
    @LoginCheck(types = {LoginCheck.UserType.USER})
    public ResponseEntity<CommonResponse<String>> userWithdraw(Integer loginSession, @PathVariable("id") int id) {
        if (loginSession != id)
            throw new FashionServerException(ErrorCode.valueOf("USER_UPDATE_ERROR").getMessage(), 602);
        userService.userWithdraw(id);
        CommonResponse<String> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "정상적으로 탈퇴되었습니다.", loginSession.toString());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("")
    @LoginCheck(types = {LoginCheck.UserType.USER, LoginCheck.UserType.SELLER})
    public ResponseEntity<CommonResponse<String>> userInfoUpdate(Integer loginSession, @RequestBody UserDto userDto) {
        userService.userInfoUpdate(loginSession, userDto);
        CommonResponse<String> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "정상적으로 회원정보가 수정되었습니다.", userDto.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<UserDto>> login(@RequestBody UserDto userDto, HttpSession session) {
        if (StringUtils.isBlank(userDto.getUserId()) || StringUtils.isBlank(userDto.getPassword()))
            throw new NullPointerException("빈 값이 존재합니다. 확인해주세요.");
        log.debug("UserId : " + userDto.getUserId() + " Password: " + userDto.getPassword());
        UserDto userInfo = userService.passwordCheck(userDto.getUserId(), userDto.getPassword());
        if (userInfo.getId() == 0 || userInfo == null)
            throw new FashionServerException(ErrorCode.valueOf("USER_NOT_USING_ERROR").getMessage(), 602);
        userService.insertSession(session, userInfo);
        log.debug("Login success = " + userInfo.getId());
        CommonResponse<UserDto> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", userDto.getUserId() + "회원 로그인", userInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    @LoginCheck(types = {LoginCheck.UserType.USER, LoginCheck.UserType.SELLER
            , LoginCheck.UserType.ADMIN})
    public ResponseEntity<CommonResponse<String>> logout(Integer loginSession, HttpSession session) {
        userService.clearSession(session);
        log.debug("Logout success = " + loginSession);
        CommonResponse<String> response = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "정상적으로 로그아웃 되었습니다.", loginSession.toString());
        return ResponseEntity.ok(response);
    }
}
