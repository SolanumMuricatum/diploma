package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.AlbumMember;
import com.pepino.albumservice.entity.AlbumMemberId;
import com.pepino.albumservice.repository.AlbumMemberRepository;
import com.pepino.albumservice.service.AlbumMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlbumMemberServiceImpl implements AlbumMemberService {

    private final AlbumMemberRepository albumMemberRepository;

    public AlbumMember saveAlbumMember(Album album) {
        AlbumMemberId albumMemberId = new AlbumMemberId(album.getId(), album.getCreatorId());
        AlbumMember albumMember = new AlbumMember(albumMemberId);
        return albumMemberRepository.save(albumMember);
    }
}
