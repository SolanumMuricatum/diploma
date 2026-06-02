package com.pepino.authservice.model;

import com.pepino.authservice.config.UserDetailsImpl;
import com.pepino.authservice.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtRefresh extends JwtAbstract {

    protected JwtRefresh(ResourceLoader resourceLoader, JwtProperties jwtProperties) throws Exception {
        super(resourceLoader, jwtProperties.getRefresh());
    }
}
