package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.dto.UserDto;
import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.AlbumMember;
import com.pepino.albumservice.entity.AlbumMemberId;
import com.pepino.albumservice.repository.AlbumMemberRepository;
import com.pepino.albumservice.service.AlbumMemberService;
import com.pepino.albumservice.service.feign.UserFeignRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumMemberServiceImpl implements AlbumMemberService {

    private final AlbumMemberRepository albumMemberRepository;
    private final UserFeignRequestService userFeignRequestService;

    @Override
    public AlbumMember saveAlbumMember(Album album) {
        AlbumMemberId albumMemberId = new AlbumMemberId(album.getId(), album.getCreatorId());
        AlbumMember albumMember = new AlbumMember(albumMemberId, true);
        return albumMemberRepository.save(albumMember);
    }

    @Override
    public List<UserDto> getAllAlbumMembers(UUID albumId) {
        List<AlbumMember> members = albumMemberRepository.findAllByIdAlbumId(albumId);

        List<UUID> userIds = members.stream()
                .map(m -> m.getId().getUserId())
                .toList();

        return userFeignRequestService.getUsersByIds(userIds);
    }

    @Override
    public List<UUID> getAllCreatedAlbums(UUID creatorId) {
        List<AlbumMember> members = albumMemberRepository.findAllByIdUserIdAndCreatorTrue(creatorId);
        return members.stream()
                .map(m -> m.getId().getAlbumId())
                .toList();
    }

    @Override
    public List<UUID> getAllAddedAlbums(UUID userId) {
        List<AlbumMember> members = albumMemberRepository.findAllByIdUserIdAndCreatorFalse(userId);
        return members.stream()
                .map(m -> m.getId().getAlbumId())
                .toList();
    }
}
