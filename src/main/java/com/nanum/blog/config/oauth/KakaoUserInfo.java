package com.nanum.blog.config.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes; // oAuth2User.getAttributes()
    private Map<String, Object> account;
    private Map<String, Object> profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        account = (Map<String,Object>) attributes.get("kakao_account");
        profile = (Map<String,Object>) account.get("profile");
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) account.get("email");
    }

    @Override
    public String getName() {
        return (String) profile.get("nickname");
    }

    public String getProfileImage(){
        return (String) profile.get("profile_image");
    }
}
