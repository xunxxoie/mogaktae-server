package code.mogaktae.domain.challenge.controller;

import code.mogaktae.domain.challenge.dto.req.ChallengeCreateRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeJoinRequestDto;
import code.mogaktae.domain.challenge.dto.req.ChallengeRequestDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeDetailsResponseDto;
import code.mogaktae.domain.challenge.dto.res.ChallengeResponseDto;
import code.mogaktae.domain.challenge.service.ChallengeService;
import code.mogaktae.domain.common.dto.ResponseDto;
import code.mogaktae.domain.result.dto.res.ChallengeResultResponseDto;
import code.mogaktae.global.security.oauth.domain.common.OAuth2UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    @PostMapping
    public ResponseEntity<ResponseDto<Long>> createChallenges(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                              @Valid @RequestBody ChallengeCreateRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.of(challengeService.createChallenge(user, request), "챌린지 생성 성공"));
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseDto<ChallengeResponseDto>> getChallengesSummary(@Valid @RequestBody ChallengeRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengesSummary(request.getSize(), request.getLastCursorId()), "요약 챌린지 조회 성공"));
    }

    @GetMapping("/info/details/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeDetailsResponseDto>> getChallengesDetail(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                                         @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengeDetails(user, challengeId), "챌린지 상세 조회 완료"));
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<Long>> joinChallenges(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                            @Valid @RequestBody ChallengeJoinRequestDto request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.joinChallenge(user, request), "챌린지 참여 성공"));
    }

    @GetMapping("/results/{challengeId}")
    public ResponseEntity<ResponseDto<ChallengeResultResponseDto>> getChallengeResult(@AuthenticationPrincipal OAuth2UserDetailsImpl user,
                                                                                      @PathVariable Long challengeId){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.getChallengeResult(user, challengeId), "챌린지 결과 조회 성공"));
    }

    @PostMapping("/push")
    public ResponseEntity<ResponseDto<?>> pushCodingTestCommit(@RequestBody Map<String, Object> request){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.of(challengeService.pushCodingTestCommit(request), "레포지토리 푸쉬 웹훅 처리 완료"));
    }
}
