package yoon.mc.memitService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.mc.memitService.entity.Members;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

    boolean existsByDeviceId(String deviceIdx);

    boolean existsByNickname(String nickname);

    Optional<Members> findMembersByMemberIdx(long idx);

    Optional<Members> findMembersByDeviceId(String id);

}
