package com.sergii.fgjx.sb.api.messages.requests;

public class ReportRequest extends Request {
    public ReportRequest(String requester, String id) {
        super(requester, id);
    }

    public ReportRequest(String requester) {
        super(requester);
    }
}
