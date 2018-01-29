package com.sergii.fgjx.sb.api.messages;

import com.sergii.fgjx.sb.api.messages.requests.Request;
import com.sergii.fgjx.sb.api.messages.responses.Response;

public interface MessageSerializer {

    Request deserializeRequest(byte[] payload);
    Response deserializeResponse(byte[] payload);
    byte[] serializeRequest(Request request);
    byte[] serializeResponse(Response request);
}
