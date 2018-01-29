package com.sergii.fgjx.sb.api.messages.responses;

public class RoleResponse extends Response{
    private final boolean master;

    public RoleResponse(String id, boolean master) {
        super(id);
        this.master = master;
    }

    public boolean isMaster() {
        return master;
    }

    @Override
    public String toString() {
        return "RoleResponse{" +
                "master=" + master +
                ", id='" + id + '\'' +
                '}';
    }
}
