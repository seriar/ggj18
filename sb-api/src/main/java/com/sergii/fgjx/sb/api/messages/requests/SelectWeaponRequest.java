package com.sergii.fgjx.sb.api.messages.requests;

import com.sergii.fgjx.sb.api.Messages;

public class SelectWeaponRequest extends Request {
    private final Messages.Weapon weapon;

    public SelectWeaponRequest(String requester, String id, Messages.Weapon weapon) {
        super(requester, id);
        this.weapon = weapon;
    }

    public SelectWeaponRequest(String requester, Messages.Weapon weapon) {
        super(requester);
        this.weapon = weapon;
    }

    public Messages.Weapon getWeapon() {
        return weapon;
    }

    @Override
    public String toString() {
        return "SelectWeaponRequest{" +
                "weapon=" + weapon +
                ", requester='" + requester + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
