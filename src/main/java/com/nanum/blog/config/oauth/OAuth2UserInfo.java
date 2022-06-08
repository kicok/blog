package com.nanum.blog.config.oauth;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider(); // google, kakako, facebook
    String getEmail();
    String getName();
}
