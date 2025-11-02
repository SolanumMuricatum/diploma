package com.pepino.authservice.model;

import com.pepino.authservice.properties.JwtProperties;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class JwtInternalService extends JwtAbstract{

    protected JwtInternalService(ResourceLoader resourceLoader, JwtProperties jwtProperties) throws Exception {
        super(resourceLoader, jwtProperties.getInternalService());
    }
}
