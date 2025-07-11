package code.mogaktae.domain.challenge.repository;

import code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse;
import code.mogaktae.domain.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom{

    @Query("""
        SELECT new code.mogaktae.domain.challenge.dto.res.ChallengeInfoSummaryResponse(
            c.id,
            c.challengeImageUrl,
            c.name,
            c.dailyProblem,
            c.startDate,
            c.endDate
        )
        FROM Challenge c
        WHERE (:lastCursorId IS NULL OR c.id < :lastCursorId)
        ORDER BY c.id DESC
    """)
    Page<ChallengeInfoSummaryResponse> findByIdLessThanOrderByIdDesc(@Param("lastCursorId") Long lastCursorId, Pageable pageable);
}
