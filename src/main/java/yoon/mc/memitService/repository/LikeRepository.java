package yoon.mc.memitService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoon.mc.memitService.entity.Likes;
import yoon.mc.memitService.entity.Members;
import yoon.mc.memitService.entity.Posts;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {

    boolean existsByMembersAndPosts(Members members, Posts posts);

    Likes findLikesByMembersAndPosts(Members members, Posts posts);

}
