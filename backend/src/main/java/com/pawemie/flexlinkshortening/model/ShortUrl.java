package com.pawemie.flexlinkshortening.model;

import com.pawemie.flexlinkshortening.login.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class ShortUrl {

    @Id
    private String code;
    private String longUrl;
    private LocalDateTime createdAt;
    private long clickCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
}
