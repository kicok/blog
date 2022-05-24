package com.nanum.blog.config.auth;

import com.nanum.blog.config.auth.Annotation.AuthUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final boolean isRegUserAnnotation = parameter.getParameterAnnotation(AuthUser.class) != null;
        final boolean isPrincipalDetails = parameter.getParameterType().equals(PrincipalDetails.class);
        return isRegUserAnnotation && isPrincipalDetails;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = null;

        // 인증 정보가 없는 경우 인증 정보 등록
        if(authentication != null ) {
            principal = authentication.getPrincipal();
        }

        // 인증 정보가 없다면 return null
        if(principal == null || principal.getClass() == String.class) {
            return null;
        }

        return principal;
    }
}
