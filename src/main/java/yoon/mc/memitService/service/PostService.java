package yoon.mc.memitService.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoon.mc.memitService.entity.Likes;
import yoon.mc.memitService.entity.Members;
import yoon.mc.memitService.entity.Posts;
import yoon.mc.memitService.exception.MemitException;
import yoon.mc.memitService.repository.LikeRepository;
import yoon.mc.memitService.repository.PostRepository;
import yoon.mc.memitService.request.PostDto;
import yoon.mc.memitService.response.PostDetailResponse;
import yoon.mc.memitService.response.PostResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final AmazonS3Client amazonS3Client;

    private final LikeRepository likeRepository;

    private final String bucket = "memit";
    private final String region = "ap-northeast-2";

    private PostResponse toResponse(Posts posts){
        return new PostResponse(posts.getPostIdx(), posts.getFile(), posts.getLike());
    }

    private PostDetailResponse toDetailResponse(Posts posts){
        return new PostDetailResponse(posts.getPostIdx(), posts.getMembers().getNickname(),
                posts.getMembers().getProfile(), posts.getFile(), posts.getContent(), String.valueOf(posts.getCreatedAt()), posts.getLike(),false);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getMyPost(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members currentMember = (Members) authentication.getPrincipal();

        List<Posts> list = postRepository.findAllByMembers(currentMember);
        List<PostResponse> result = new ArrayList<>();

        for(Posts p:list){
            result.add(toResponse(p));
        }
        return result;
    }


    @Transactional(readOnly = true)
    public List<PostResponse> getAllPost(){
        List<Posts> list = postRepository.findAllWithLikeDesc();
        List<PostResponse> result = new ArrayList<>();

        for(Posts p:list){
            result.add(toResponse(p));
        }
        return result;
    }

    //글 디테일 불러오기
    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(long idx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members members = (Members) authentication.getPrincipal();

        Posts post = postRepository.findPostsByPostIdx(idx).orElseThrow();
        PostDetailResponse response = toDetailResponse(post);
        response.setLiked(likeRepository.existsByMembersAndPosts(members, post));
        return response;
    }

    //글 쓰기
    @Transactional
    public PostDetailResponse createPost(PostDto dto, MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members currentMember = (Members) authentication.getPrincipal();

        String url;
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new MemitException("잘못된 파일 형식", HttpStatus.BAD_REQUEST);
        }
        UUID uuid = UUID.randomUUID();
        try {
            String fileName = uuid + file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/projects/" + fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            System.out.println(file.getContentType());
            url = fileUrl;
            amazonS3Client.putObject(bucket +"/projects", fileName, file.getInputStream(), objectMetadata);
        } catch (Exception e){
            throw new MemitException("서버에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Posts post = Posts.builder()
                .members(currentMember)
                .file(url)
                .content(dto.getContent())
                .build();

        return toDetailResponse(postRepository.save(post));
    }

    //글 수정
    @Transactional
    public PostDetailResponse update(long idx, PostDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members currentMember = (Members) authentication.getPrincipal();
        Posts post = postRepository.findPostsByPostIdx(idx).orElseThrow();

        if(!post.getMembers().equals(currentMember))
            throw new MemitException("권한이 없음", HttpStatus.UNAUTHORIZED);

        post.setContent(dto.getContent());

        return toDetailResponse(postRepository.save(post));
    }

    @Transactional
    public long like(long idx){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members currentMember = (Members) authentication.getPrincipal();
        Posts posts = postRepository.findPostsByPostIdx(idx).orElseThrow();

        if(posts == null)
            throw new MemitException("Post가 없음", HttpStatus.NOT_FOUND);

        if(likeRepository.existsByMembersAndPosts(currentMember, posts)){
            posts.dislike();
            likeRepository.delete(likeRepository.findLikesByMembersAndPosts(currentMember, posts));
        }
        else{
            Likes likes = Likes.builder().members(currentMember).posts(posts).build();
            posts.like();
            likeRepository.save(likes);
        }
        return posts.getLike();
    }

}
