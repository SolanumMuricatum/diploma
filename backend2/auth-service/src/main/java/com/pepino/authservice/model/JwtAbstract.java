package com.pepino.authservice.model;

import com.pepino.authservice.config.UserDetailsImpl;
import com.pepino.authservice.properties.JwtProperties;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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


public abstract class JwtAbstract {

    protected ResourceLoader resourceLoader;
    private final KeyPair keyPair;
    @Getter
    private final int lifetime;
    @Getter
    private final String name;

    protected JwtAbstract(ResourceLoader resourceLoader, JwtProperties.TokenConfig config) throws Exception {
        this.resourceLoader = resourceLoader;
        this.keyPair = loadKeyPair(config.getPrivateKeyPath(), config.getPublicKeyPath());
        this.lifetime = config.getExpirationMs();
        this.name = config.getName();
    }

    protected KeyPair loadKeyPair(String privateKeyPath, String publicKeyPath) throws Exception {
        PrivateKey privateKey;
        PublicKey publicKey;

        Resource privateResource = resourceLoader.getResource(privateKeyPath);
        Resource publicResource = resourceLoader.getResource(publicKeyPath);

        try (InputStream privateStream = privateResource.getInputStream();
             InputStream publicStream = publicResource.getInputStream()) {

            String privatePem = new String(privateStream.readAllBytes())
                    .replaceAll("-----\\w+ PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            String publicPem = new String(publicStream.readAllBytes())
                    .replaceAll("-----\\w+ PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            var kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privatePem)));
            publicKey = kf.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicPem)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load key pair", e);
        }

        return new KeyPair(publicKey, privateKey);
    }

    public String generateToken(Authentication auth) {
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public String generateInternalServiceToken(String serviceName) {
        return Jwts.builder()
                .subject(serviceName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(keyPair.getPrivate())
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith(keyPair.getPublic()) // проверка подписи
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getPublicKey() {
        PublicKey publicKey = keyPair.getPublic();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}
