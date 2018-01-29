package com.sergii.fgjx.sb.api.messages.requests;

public class CodeTransmissionRequest extends Request {
    public CodeTransmissionRequest(String requester, String id) {
        super(requester, id);
    }

    public CodeTransmissionRequest(String requester) {
        super(requester);
    }
}
