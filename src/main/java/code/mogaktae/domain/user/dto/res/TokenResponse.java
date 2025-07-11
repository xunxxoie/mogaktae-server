package code.mogaktae.domain.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "로그인 Response")
public record TokenResponse(

        @Schema(description = "토큰 타입", example = "Bearer")
        String type,

        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1VCJ9.eyJzdWUiIsImlhdCI6MTcwNjNzA2MDc4MjAwfQ.1234abcd5678efgh9012ijkl3456mnop")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "eyJhbGckpXVCJ9.eyJzdWIiOiJ1c2VyQGMDc2NDAwLCJleHAiOjE3MDcyODYwMDB9.ab8ijkl9012mnop3456")
        String refreshToken
) {

    public static TokenResponse create(String accessToken, String refreshToken) {
        return TokenResponse.builder()
                .type("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse("Bearer", accessToken, refreshToken);
    }
}