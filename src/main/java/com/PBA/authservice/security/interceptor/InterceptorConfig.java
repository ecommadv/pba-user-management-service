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
    private static final String[] EXCLUDED_ENDPOINTS = {
            USER_LOGIN_ENDPOINT,
            USER_REGISTER_ENDPOINT,
            USER_ACTIVATE_ENDPOINT,
            SWAGGER_ENDPOINT,
            API_DOCS_ENDPOINT
    };
    private final JwtValidationInterceptor jwtValidationInterceptor;
    private final SecurityContextHolderProviderInterceptor contextHolderProviderInterceptor;

    public InterceptorConfig(JwtValidationInterceptor jwtValidationInterceptor, SecurityContextHolderProviderInterceptor contextHolderProviderInterceptor) {
        this.jwtValidationInterceptor = jwtValidationInterceptor;
        this.contextHolderProviderInterceptor = contextHolderProviderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtValidationInterceptor)
                .excludePathPatterns(EXCLUDED_ENDPOINTS);

        registry.addInterceptor(contextHolderProviderInterceptor)
                .excludePathPatterns(EXCLUDED_ENDPOINTS);
    }
}
