package code.mogaktae.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static code.mogaktae.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Long> findUserIdByNicknameIn(List<String> nicknames){
        return jpaQueryFactory
                .select(user.id)
                .from(user)
                .where(user.nickname.in(nicknames))
                .fetch();
    }

    @Override
    public Optional<String> findSolvedAcIdByNickname(String nickname){
        return Optional.ofNullable(jpaQueryFactory
                .select(user.solvedAcId)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne()
        );
    }
}
