package com.pepino.albumservice.service;

import com.pepino.albumservice.entity.MemberInvitation;

import java.util.List;
import java.util.UUID;

public interface MemberInvitationService {
    List<MemberInvitation> findAllInvitationsForAlbum(UUID albumId);
    List<MemberInvitation> findAllInvitationsForUser(UUID userId);
    void save(MemberInvitation memberInvitation);
    void retrySendingInvitation(MemberInvitation memberInvitation);
    void declineInvitation(UUID albumId, UUID userId);
    void acceptInvitation(UUID albumId, UUID userId);
    boolean checkForNewInvitations(UUID userId);
}
