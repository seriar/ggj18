package com.sergii.fgjx.sb.api.messages.responses;

public class CodeTransmissionResponse extends Response {
    private final String code;

    public CodeTransmissionResponse(String id, String code) {
        super(id);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "CodeTransmissionResponse{" +
                "code='" + code + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
