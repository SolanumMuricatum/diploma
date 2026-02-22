package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.client.AuthServiceClient;
import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.repository.AlbumRepository;
import com.pepino.albumservice.service.AlbumMemberService;
import com.pepino.albumservice.service.AlbumService;
import com.pepino.albumservice.service.feign.UserFeignRequestService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final UserFeignRequestService userFeignRequestService;
    private final AuthServiceClient authServiceClient;
    private final AlbumMemberService albumMemberService;

    @Override
    public Album saveAlbum(Album album) throws Exception {
        if (!userFeignRequestService.isUserExists(album.getCreatorLoginSnapshot())) {
            throw new Exception("Пользователя с таким логином не найдено");
        }
        album.setStartDate(LocalDate.now());
        album.setEndDate(LocalDate.now().plusMonths(1));
        albumRepository.save(album);
        albumMemberService.saveAlbumMember(album);
        return album;
    }

    @Override
    public Album getAlbumById(UUID id) throws Exception {
        return albumRepository.findById(id).orElseThrow(() -> new Exception("Альбом с таким ID не найден"));
    }

    @Override
    public List<Album> getCreatedAlbums(UUID creatorId) throws Exception {
        List<UUID> albumIds = albumMemberService.getAllCreatedAlbums(creatorId);
        return albumRepository.findAllByIdIn(albumIds);
    }

    @Override
    public List<Album> getAddedAlbums(UUID userId) throws Exception {
        List<UUID> albumIds = albumMemberService.getAllAddedAlbums(userId);
        return albumRepository.findAllByIdIn(albumIds);
    }

    public Album updateAlbum(UUID id, Album album) throws Exception {
        String publicKey = authServiceClient.getAccessTokenPublicKey().getBody();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();

        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("AccessToken")).findFirst().orElse(null);
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey key = kf.generatePublic(spec);

        assert cookie != null;
        String username = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(cookie.getValue())
                    .getPayload()
                    .getSubject();

        System.out.println(username);

        Album existingAlbum = albumRepository.findById(id).orElseThrow(() -> new Exception("Альбом с таким ID не найден"));

        if (!existingAlbum.getCreatorLoginSnapshot().equals(username)) {
            throw new Exception("Вы не можете редактировать этот альбом");
        }

        existingAlbum.setName(album.getName());
        existingAlbum.setBackground(album.getBackground());
        existingAlbum.setTextColor(album.getTextColor());
        existingAlbum.setTextFont(album.getTextFont());
        existingAlbum.setTextSize(album.getTextSize());
        albumRepository.save(existingAlbum);
        return existingAlbum;
    }

    @Override
    public Album updateAlbumCreatorLogin(UUID creatorId, String newLogin) throws Exception {
        List<Album> albums = albumRepository.findAllByCreatorId(creatorId);

        if (albums.isEmpty()) {
            throw new Exception("Альбомы с таким creatorId не найдены");
        }

        for (Album album : albums) {
            album.setCreatorLoginSnapshot(newLogin);
            albumRepository.save(album);
        }

        return albums.getFirst();
    }
}
