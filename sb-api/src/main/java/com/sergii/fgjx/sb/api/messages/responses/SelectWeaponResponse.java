package com.sergii.fgjx.sb.api.messages.responses;

public class SelectWeaponResponse extends Response {
    public SelectWeaponResponse(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return "SelectWeaponResponse{" +
                "id='" + id + '\'' +
                '}';
    }
}
