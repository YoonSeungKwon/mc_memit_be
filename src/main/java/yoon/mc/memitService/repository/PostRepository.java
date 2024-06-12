package yoon.mc.memitService.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import yoon.mc.memitService.entity.Members;
import yoon.mc.memitService.entity.Posts;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Posts> findPostsByPostIdx(long idx);
    List<Posts> findAllByMembers(Members members);
}
