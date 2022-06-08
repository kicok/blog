package com.nanum.blog.config.oauth;

import com.nanum.blog.config.auth.PrincipalDetails;
import com.nanum.blog.model.RoleType;
import com.nanum.blog.model.User;
import com.nanum.blog.model.UserRole;
import com.nanum.blog.repository.UserRepository;
import com.nanum.blog.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private final UserService userService;

    // @Lazy 를 넣어서 순환 참조 에러를 피할수 있다.
    // SecurityConfig 에서도 PrincipalOauth2UserService 을 참조하여 주입하고
    // 현재 클랙스에서도 SecurityConfig 클래스에서 Bean 으로 만드는 BCryptPasswordEncoder 를 주입하려 하고 있음
    // 그러므로 @Lazy 를 넣어서 순환 참조 에러를 피함
    public PrincipalOAuth2UserService(@Lazy BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, @Lazy UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
       // return super.loadUser(userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 1. 카카오로그인창-> 로그인완료-> code를 리턴(Oauth-Client 라이브러리) -> AccessToken요청하고 받음
        // 2. userRequest 정보 -> loadUser함수 호출 -> 카카오 로부터 회원프로필 받아옴

        System.out.println("getAttributes : " + oAuth2User.getAttributes());

        OAuth2UserInfo oAuth2UserInfo = null;
        String userRequestProviderId = userRequest.getClientRegistration().getRegistrationId();

        if(userRequestProviderId.equals("kakao")) {
            System.out.println("kakao 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
                                                                                      // "Name: [2184008481], Granted Authorities: [[ROLE_USER, SCOPE_account_email, SCOPE_profile_image, SCOPE_profile_nickname]], User Attributes: [{id=2184008481, connected_at=2022-04-01T08:38:03Z, properties={nickname=김창옥, profile_image=http://k.kakaocdn.net/dn/dFvNhi/btqNGNTt76i/RUtZuqgSoGNxw5NEPvF3t1/img_640x640.jpg, thumbnail_image=http://k.kakaocdn.net/dn/dFvNhi/btqNGNTt76i/RUtZuqgSoGNxw5NEPvF3t1/img_110x110.jpg}, kakao_account={profile_nickname_needs_agreement=false, profile_image_needs_agreement=false, profile={nickname=김창옥, thumbnail_image_url=http://k.kakaocdn.net/dn/dFvNhi/btqNGNTt76i/RUtZuqgSoGNxw5NEPvF3t1/img_110x110.jpg, profile_image_url=http://k.kakaocdn.net/dn/dFvNhi/btqNGNTt76i/RUtZuqgSoGNxw5NEPvF3t1/img_640x640.jpg, is_default_image=false}, has_email=true, email_needs_agreement=false, is_email_valid=true, is_email_verified=true, email=kicok@kakao.com}}]"

        }else{
            System.out.println("우리는 kakao만 지원해요");
        }

        String provider = oAuth2UserInfo.getProvider(); //google, facebook
        String providerId = oAuth2UserInfo.getProviderId(); // google, facebook 가입자의 primary id
        String username = provider + "_" + providerId; // google_109522245212489089761
        String password = bCryptPasswordEncoder.encode("kicok"); // 의미 없는 형식적인 password
        String email = oAuth2UserInfo.getEmail();
        String nickname = oAuth2UserInfo.getName();

        Optional<User> user = userRepository.findByUsername(username);

        User userEntry = null;

        if(!user.isPresent()){
            System.out.println(oAuth2UserInfo.getProvider()  + "로그인 최초 로그인!!!");
            userEntry = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .nickname(nickname)
                    .provider(provider)
                    .provider_id(providerId)
                    .build();
            userEntry = userRepository.save(userEntry);

            List<UserRole> userRoles =  userService.roleMake(userEntry, new ArrayList<>(Arrays.asList(RoleType.USER, RoleType.ADMIN)));

           // List<UserRole> userRoles = new ArrayList<>(Arrays.asList(userRole));
            //userRoles.add(userRole);
            userEntry.setRoles(userRoles);

        }else {
            userEntry = user.get();
            System.out.println(oAuth2UserInfo.getProvider() + " 로그인을 이미 한적이 있습니다!!!.");
        }

        // OAuth2User는 현 method 의 반환값이고 PrincipalDetails 는 OAuth2User 를 implements 하도록 변경하였음
        // PrincipalDetails를 리턴하면 Oauth로그인이든 일반 로그인이든 상관 없이 User의 프로필 정보(userEntity)를 받아올수 있다.
        // PrincipalDetails 가 리턴될때 Authentication가 자동으로 만들어지고
        // 시큐리티 세션에는 무조건 Authentication 객체 만 들어갈 수 있다
        // 즉 현재 메서드를 override 한 목적은 PrincipalDetails를 리턴하여 Authentication 만들기 위함이다.
        // 굳이 단순하게 super.loadUser(userRequest)를 리턴하거나  OAuth2User를 리턴하는게 목적이었다면 현재 메서드를 오버라이드를 할필요가 없었음.
        return new PrincipalDetails(userEntry, oAuth2User.getAttributes());

    }
}
