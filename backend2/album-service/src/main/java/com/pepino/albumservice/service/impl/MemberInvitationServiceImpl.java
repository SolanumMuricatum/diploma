package com.pepino.albumservice.service.impl;

import com.pepino.albumservice.entity.AlbumMemberId;
import com.pepino.albumservice.entity.MemberInvitation;
import com.pepino.albumservice.repository.MemberInvitationRepository;
import com.pepino.albumservice.service.AlbumMemberService;
import com.pepino.albumservice.service.MemberInvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberInvitationServiceImpl implements MemberInvitationService {

    private final MemberInvitationRepository memberInvitationRepository;
    private final AlbumMemberService albumMemberService;

    @Override
    public List<MemberInvitation> findAllInvitationsForAlbum(UUID albumId) {
        return memberInvitationRepository.findAllByIdAlbumId(albumId);
    }

    @Override
    public List<MemberInvitation> findAllInvitationsForUser(UUID userId) {
        return memberInvitationRepository.findAllByIdUserId(userId);
    }

    @Override
    public void save(MemberInvitation memberInvitation) {
        memberInvitationRepository.save(memberInvitation);
    }

    @Override
    public void retrySendingInvitation(MemberInvitation memberInvitation) {
        memberInvitationRepository.resetInvitationStatus(memberInvitation.getId());
    }

    @Override
    public void declineInvitation(UUID albumId, UUID userId) {
        AlbumMemberId albumMemberId = new AlbumMemberId(albumId, userId);
        MemberInvitation memberInvitation = memberInvitationRepository
                .findById(albumMemberId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Приглашение с таким id не найдено")
                );
        memberInvitation.setAccepted(false);

        memberInvitationRepository.save(memberInvitation);
    }

    @Override
    public void acceptInvitation(UUID albumId, UUID userId) {
        AlbumMemberId albumMemberId = new AlbumMemberId(albumId, userId);
        MemberInvitation memberInvitation = memberInvitationRepository
                .findById(albumMemberId)
                .orElseThrow(
                        () -> new IllegalArgumentException("Приглашение с таким id не найдено")
                );
        memberInvitation.setAccepted(true);

        albumMemberService.saveAlbumMember(albumId, userId);
        memberInvitationRepository.save(memberInvitation);
    }

    @Override
    public boolean checkForNewInvitations(UUID userId) {
        return memberInvitationRepository.hasPendingInvitations(userId);
    }
}
