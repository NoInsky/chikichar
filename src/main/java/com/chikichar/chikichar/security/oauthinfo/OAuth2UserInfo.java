package com.chikichar.chikichar.security.oauthinfo;

import com.chikichar.chikichar.model.Social;
import lombok.Getter;

import java.util.Map;

@Getter
public abstract class OAuth2UserInfo {

    protected Map<String ,Object> attr;

    public OAuth2UserInfo(Map<String, Object> attr) {
        this.attr = attr;
    }

    public abstract String getEmail();
    public abstract String getName();
    public abstract String getNickname();
    public abstract String getPhone();

    public static OAuth2UserInfo getOAuth2UserInfo(Social social, Map<String, Object> attr) {
        switch (social) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attr);
            case NAVER: return new NaverOAuth2UserInfo(attr);
            default: throw new IllegalArgumentException("소셜 로그인 지원 불가");
        }
    }

}
