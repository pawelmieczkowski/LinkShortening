package com.pawemie.flexlinkshortening.repository;

import com.pawemie.flexlinkshortening.login.appuser.AppUser;
import com.pawemie.flexlinkshortening.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<ShortUrl, String> {

    List<ShortUrl> findAllByUser(AppUser appUser);
}