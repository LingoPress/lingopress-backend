package com.kidchang.lingopress._base.utils;


import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    public static Long getUserId() {
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName()
            .equals("anonymousUser")) {
            throw new GeneralException(Code.AUTH_INFO_NOT_FOUND);
        }

        return Long.parseLong(authentication.getName());
    }
}
