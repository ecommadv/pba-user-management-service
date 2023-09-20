package com.pba.authservice.security.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class JwtValidationInterceptorConfig implements WebMvcConfigurer {
    private static final String USER_LOGIN_ENDPOINT = "/api/user/login";
    private static final String USER_REGISTER_ENDPOINT = "/api/user/register";
    private static final String USER_ACTIVATE_ENDPOINT = "/api/user/activate/**";
    private final JwtValidationInterceptor jwtValidationInterceptor;
    private final SecurityContextHolderProviderInterceptor contextHolderProviderInterceptor;

    public JwtValidationInterceptorConfig(JwtValidationInterceptor jwtValidationInterceptor, SecurityContextHolderProviderInterceptor contextHolderProviderInterceptor) {
        this.jwtValidationInterceptor = jwtValidationInterceptor;
        this.contextHolderProviderInterceptor = contextHolderProviderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtValidationInterceptor)
                .excludePathPatterns(USER_LOGIN_ENDPOINT, USER_REGISTER_ENDPOINT, USER_ACTIVATE_ENDPOINT);

        registry.addInterceptor(contextHolderProviderInterceptor)
                .excludePathPatterns(USER_LOGIN_ENDPOINT, USER_REGISTER_ENDPOINT, USER_ACTIVATE_ENDPOINT);
    }
}
