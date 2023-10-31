package com.ccommit.fashionserver.aop;

import com.ccommit.fashionserver.exception.ErrorCode;
import com.ccommit.fashionserver.exception.FashionServerException;
import com.ccommit.fashionserver.utils.SessionUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;


/**
 * packageName    : com.ccommit.fashionserver.aop
 * fileName       : LoginCheckAspect
 * author         : juoiy
 * date           : 2023-09-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-22        juoiy       최초 생성
 */
@Aspect
@Component
@Log4j2
public class LoginCheckAspect {
    @Around("@annotation(com.ccommit.fashionserver.aop.LoginCheck) && @annotation(loginCheck)")
    public Object LoginSessionCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        int id = 0;
        int index = 0;
        Boolean isLoginCheck = false;
        for (int i = 0; i < loginCheck.types().length; i++) {
            if (isLoginCheck == false) {
                switch (loginCheck.types()[i].toString()) {
                    case "USER":
                        try {
                            id = SessionUtils.getUserLoginSession(session);
                        } catch (NullPointerException e) {
                            isLoginCheck = false;
                        }
                        break;
                    case "SELLER":
                        try {
                            id = SessionUtils.getSellerLoginSession(session);
                        } catch (NullPointerException e) {
                            isLoginCheck = false;
                        }
                        break;
                    case "ADMIN":
                        try {
                            id = SessionUtils.getAdminLoginSession(session);
                        } catch (NullPointerException e) {
                            isLoginCheck = false;
                        }
                        break;
                }
                if (id != 0)
                    isLoginCheck = true;
            }
        }
        if (isLoginCheck == false)
            throw new FashionServerException(ErrorCode.valueOf("LOGIN_ERROR").getMessage(), 605);
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();
        if (proceedingJoinPoint.getArgs() != null)
            modifiedArgs[index] = id;
        return proceedingJoinPoint.proceed(modifiedArgs);
    }

}
