package com.podmate.global.util.oauth2;

import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.enums.Role;
import com.podmate.domain.user.domain.enums.SocialType;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import com.podmate.global.util.oauth2.dto.KaKaoResponse;
import com.podmate.global.util.oauth2.dto.OAuth2Response;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


// OAuth2 제공자(카카오)로부터 제공받은 사용자 정보를, 우리 서비스에 맞게 가공, 변환
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("CustomOAuth2UserService :: {}", oAuth2User);
        log.info("oAuthUser.getAttributes :: {}", oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("kakao")) {
            oAuth2Response = new KaKaoResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 OAuth2 Provider 입니다.");
        }

        // DB에 유저가 있는지 판단
        Optional<User> foundUser = userRepository.findByEmail(oAuth2Response.getEmail());

        // DB에 유저 없으면 - 회원가입
        if (foundUser.isEmpty()){

//            User user = User.builder()
//                    .email(oAuth2Response.getEmail())
//                    .nickname(oAuth2Response.getNickName())
//                    .profileImage(oAuth2Response.getProfileImage())
//                    .socialType(SocialType.KAKAO)
//                    .role(Role.USER) // 일반 유저 설정
//                    .build();

            User user = User.createFirstLoginUser(oAuth2Response.getNickName(), oAuth2Response.getEmail(),
                    oAuth2Response.getProfileImage(),
                    SocialType.KAKAO, Role.USER);

            userRepository.save(user);

            return new CustomOAuth2User(user);
        } else {
            // DB에 유저 존재하면 - 로그인 진행 (이때 로그인 처리는 안하고, OAuth2LoginSuccessHandler에서 담당함)
            User user = foundUser.get();

            return new CustomOAuth2User(user);
        }
    }

}
