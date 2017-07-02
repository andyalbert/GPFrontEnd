package com.project.locateme.models.android.specific;

import com.project.locateme.dataHolder.userManagement.Profile;

/**
 * @version 1.0
 * @since 10/4/2017
 * @author andrew
 */

public class FriendInvitationState {
    public enum InvitationState{
        PARTICIPANT, INVITED, NONE
    }
    private Profile profile;
    private InvitationState state;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public InvitationState getState() {
        return state;
    }

    public void setState(InvitationState state) {
        this.state = state;
    }
}
