package com.sergii.fgjx.sb.api.messages.responses;

public class ActivationResponse extends Response {

    private final int quality;
    private final int time;

    public ActivationResponse(String id, int quality, int time) {
        super(id);
        this.quality = quality;
        this.time = time;
    }

    public int getQuality() {
        return quality;
    }

    public int getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "ActivationResponse{" +
                "quality=" + quality +
                ", time=" + time +
                ", id='" + id + '\'' +
                '}';
    }
}
