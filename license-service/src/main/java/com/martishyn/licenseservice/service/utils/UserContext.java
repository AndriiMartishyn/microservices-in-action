package com.martishyn.licenseservice.service.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {

    public static final String CORRELATION_ID_HEADER = "tmx-correlation-id";

    public static final String AUTH_TOKEN_HEADER = "Authorization";

    public static final String USER_ID_HEADER = "tmx-user-id";

    public static final String ORGANIZATION_ID_HEADER = "tmx-organization-id";

    private String correlationId;

    private String authToken;

    private String userId;

    private String organizationId;
}
