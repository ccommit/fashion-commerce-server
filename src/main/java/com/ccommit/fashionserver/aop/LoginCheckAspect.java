package com.ccommit.fashionserver.aop;

import com.ccommit.fashionserver.utils.SessionUtils;
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
public class LoginCheckAspect {
    @Around("@annotation(com.ccommit.fashionserver.aop.LoginCheck) && @annotation(loginCheck)")
    public Object LoginSessionCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        int id = 0;
        int index = 0;

        for (int i = 0; i < loginCheck.types().length; i++) {
            if (id == 0) {
                switch (loginCheck.types()[i].toString()) {
                    case "USER":
                        try {
                            id = SessionUtils.getUserLoginSession(session);
                        } catch (NullPointerException e) {
                            id = 0;
                        }
                        break;
                    case "SELLER":
                        try {
                            id = SessionUtils.getSellerLoginSession(session);
                        } catch (NullPointerException e) {
                            id = 0;
                        }
                        break;
                    case "ADMIN":
                        try {
                            id = SessionUtils.getAdminLoginSession(session);
                        } catch (NullPointerException e) {
                            id = 0;
                        }
                        break;
                }
            }
        }

        if (id == 0) {
            throw new Exception("로그인이 필요합니다.");
        }
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();
        if (proceedingJoinPoint.getArgs() != null)
            modifiedArgs[index] = id;
        return proceedingJoinPoint.proceed(modifiedArgs);
    }

}
