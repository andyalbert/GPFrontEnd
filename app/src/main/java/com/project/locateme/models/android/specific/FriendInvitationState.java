package com.project.locateme.models.android.specific;

import com.project.locateme.dataHolder.userManagement.Profile;

/**
 * Created by andrew on 10/04/17.
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
