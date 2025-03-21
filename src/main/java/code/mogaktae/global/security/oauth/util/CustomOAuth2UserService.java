package code.mogaktae.global.security.oauth.util;

import code.mogaktae.domain.user.dto.res.TokenResponseDto;
import code.mogaktae.domain.user.repository.UserRepository;
import code.mogaktae.global.security.jwt.JwtTokenProvider;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserInfo;
import code.mogaktae.global.security.oauth.exception.OAuth2AuthenticationProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try{
            return convertToOAuth2Principal(userRequest, oAuth2User);
        }catch (AuthenticationException e){
            throw e;
        }catch (Exception e){
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User convertToOAuth2Principal(OAuth2UserRequest userRequest, OAuth2User oAuth2User){

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes(), accessToken);

        if(!StringUtils.hasText(oAuth2UserInfo.getNickname()))
            throw new OAuth2AuthenticationProcessingException("Email not found with OAuth2UserInfo");

        return new OAuth2UserDetailsImpl(oAuth2UserInfo);
    }

    public Boolean checkUserPresent(String nickname){
        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public TokenResponseDto oAuth2Login(Authentication authentication){

        return jwtTokenProvider.generateToken(authentication);
    }
}
