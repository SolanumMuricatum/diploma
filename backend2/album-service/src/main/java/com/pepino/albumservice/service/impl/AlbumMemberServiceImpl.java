package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.dto.UserDto;
import com.pepino.albumservice.entity.Album;
import com.pepino.albumservice.entity.AlbumMember;
import com.pepino.albumservice.entity.AlbumMemberId;
import com.pepino.albumservice.repository.AlbumMemberRepository;
import com.pepino.albumservice.service.AlbumMemberService;
import com.pepino.albumservice.service.PhotoService;
import com.pepino.albumservice.service.feign.UserFeignRequestService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumMemberServiceImpl implements AlbumMemberService {

    private final AlbumMemberRepository albumMemberRepository;
    private final UserFeignRequestService userFeignRequestService;
    private final PhotoService photoService;

    @Override
    public void saveAlbumCreator(Album album) {
        AlbumMemberId albumMemberId = new AlbumMemberId(album.getId(), album.getCreatorId());
        AlbumMember albumMember = new AlbumMember(albumMemberId, true);
        albumMemberRepository.save(albumMember);
    }

    @Override
    public void saveAlbumMember(UUID albumId, UUID userId) {
        AlbumMemberId albumMemberId = new AlbumMemberId(albumId, userId);
        AlbumMember albumMember = new AlbumMember(albumMemberId, false);
        albumMemberRepository.save(albumMember);
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
    public List<UserDto> getAlbumMembersWithoutTheCreator(UUID albumId) {
        List<AlbumMember> members = albumMemberRepository.findAllByIdAlbumIdAndCreatorFalse(albumId);

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

    @Override
    @Transactional
    public void deleteAllMembersWithAlbumId(UUID albumId) {
        albumMemberRepository.deleteAllByIdAlbumId(albumId);
    }

    @Override
    @Transactional
    public void leaveAddedAlbum(UUID albumId, UUID userId) {
        photoService.deleteAllMemberPhotos(albumId, userId);
        albumMemberRepository.deleteByIdAlbumIdAndIdUserId(albumId, userId);
    }

    @Override
    public boolean isUserAlbumsCreator(UUID albumId, UUID userId) {
        return albumMemberRepository.existsByIdAlbumIdAndIdUserIdAndCreatorTrue(albumId, userId);
    }
}
