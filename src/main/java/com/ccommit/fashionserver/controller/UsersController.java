package com.ccommit.fashionserver.controller;

import com.ccommit.fashionserver.dto.UserDto;
import com.ccommit.fashionserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UsersController {

    @Autowired
    private UserService userService;

    // @RequestMapping(method = RequestMethod.POST, path="")
    // 아래의 @PostMapping("")와 동일. Post일 경우 간결하게 원하면 지금처럼 작성하면 된다.
    //회원가입
    @PostMapping("")
    public int signUp(UserDto userDto){
        // postman으로 들어온 값이 null인지 유효성 체크
        // 들어온 값을 받아서 전달

        return 0;
    }


}
