package com.app.azazte.azazte.Beans;

/**
 * Created by home on 15/08/16.
 */
public class NotificationConfig {
    private String deviceId;
    private String regId;

    public NotificationConfig(String uniqueId, String regId) {
        this.deviceId = uniqueId;
        this.regId = regId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}
