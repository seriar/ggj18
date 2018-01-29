package com.sergii.fgjx.sb.api.messages.requests;

public class ActivationRequest extends Request {
    private final String code;

    public ActivationRequest(String requester, String id, String code) {
        super(requester, id);
        this.code = code;
    }

    public ActivationRequest(String requester, String code) {
        super(requester);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ActivationRequest{" +
                "code='" + code + '\'' +
                ", requester='" + requester + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
