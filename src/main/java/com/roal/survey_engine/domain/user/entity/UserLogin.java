package com.roal.survey_engine.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class UserLogin {

    @Column(name = "last_login")
    private LocalDateTime dateTime;
    @Column(name = "last_login_ip")
    private String ip;

    public UserLogin(LocalDateTime dateTime, String ip) {
        this.dateTime = dateTime;
        this.ip = ip;
    }

    public UserLogin() {

    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getIp() {
        return ip;
    }
}
