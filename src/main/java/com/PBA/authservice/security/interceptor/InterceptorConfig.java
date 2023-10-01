package com.pba.authservice.security.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class InterceptorConfig implements WebMvcConfigurer {
    private static final String USER_LOGIN_ENDPOINT = "/api/user/login";
    private static final String USER_REGISTER_ENDPOINT = "/api/user/register";
    private static final String USER_ACTIVATE_ENDPOINT = "/api/user/activate/**";
    private static final String SWAGGER_ENDPOINT = "/swagger-ui/**";
    private static final String API_DOCS_ENDPOINT = "/v3/api-docs/**";
    private static final String GROUP_LOGIN_INFO_ENDPOINT = "/api/group/login-info";
    private static final String[] EXCLUDED_ENDPOINTS = {
            USER_LOGIN_ENDPOINT,
            USER_REGISTER_ENDPOINT,
            USER_ACTIVATE_ENDPOINT,
            SWAGGER_ENDPOINT,
            API_DOCS_ENDPOINT,
            GROUP_LOGIN_INFO_ENDPOINT
    };
    private final JwtUserTokenValidationInterceptor jwtUserTokenValidationInterceptor;
    private final UserContextHolderProviderInterceptor userContextHolderProviderInterceptor;
    private final JwtGroupTokenValidationInterceptor jwtGroupTokenValidationInterceptor;
    private final GroupContextHolderProviderInterceptor groupContextHolderProviderInterceptor;

    public InterceptorConfig(JwtUserTokenValidationInterceptor jwtUserTokenValidationInterceptor,
                             UserContextHolderProviderInterceptor userContextHolderProviderInterceptor,
                             JwtGroupTokenValidationInterceptor jwtGroupTokenValidationInterceptor,
                             GroupContextHolderProviderInterceptor groupContextHolderProviderInterceptor) {
        this.jwtUserTokenValidationInterceptor = jwtUserTokenValidationInterceptor;
        this.userContextHolderProviderInterceptor = userContextHolderProviderInterceptor;
        this.jwtGroupTokenValidationInterceptor = jwtGroupTokenValidationInterceptor;
        this.groupContextHolderProviderInterceptor = groupContextHolderProviderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtUserTokenValidationInterceptor)
                .excludePathPatterns(EXCLUDED_ENDPOINTS);

        registry.addInterceptor(userContextHolderProviderInterceptor)
                .excludePathPatterns(EXCLUDED_ENDPOINTS);

        registry.addInterceptor(jwtGroupTokenValidationInterceptor)
                .addPathPatterns(GROUP_LOGIN_INFO_ENDPOINT);

        registry.addInterceptor(groupContextHolderProviderInterceptor)
                .addPathPatterns(GROUP_LOGIN_INFO_ENDPOINT);
    }
}
