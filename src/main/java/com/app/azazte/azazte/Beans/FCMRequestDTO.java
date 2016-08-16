package com.app.azazte.azazte.Beans;

/**
 * Created by home on 15/08/16.
 */
public class FCMRequestDTO {
    private String id;
    private String fcmId;

    public FCMRequestDTO(String uniqueId, String regId) {
        this.id = uniqueId;
        this.fcmId = regId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }
}
