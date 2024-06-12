package yoon.mc.memitService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoon.mc.memitService.request.PostDto;
import yoon.mc.memitService.response.PostDetailResponse;
import yoon.mc.memitService.response.PostResponse;
import yoon.mc.memitService.service.PostService;

import java.util.List;

@Tag(name = "게시글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    //내 글 불러오기
    @Operation(summary = "내가 쓴 게시글 불러오기")
    @GetMapping("/my")
    public ResponseEntity<List<PostResponse>> getMyPostList(){

        List<PostResponse> result = postService.getMyPost();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //전체 글 불러오기
    @Operation(summary = "전체 게시글 불러오기")
    @GetMapping()
    public ResponseEntity<List<PostResponse>> getPostList(){

        List<PostResponse> result = postService.getAllPost();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //글 디테일 불러오기
    @Operation(summary = "idx에 해당하는 게시글 정보 불러오기")
    @GetMapping("/detail/{idx}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable long idx){

        PostDetailResponse result = postService.getPostDetail(idx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //글 쓰기
    @Operation(summary = "글 쓰기")
    @PostMapping()
    public ResponseEntity<PostDetailResponse> createNewPost(@RequestPart MultipartFile file, @RequestPart PostDto dto){

        System.out.println(dto.getContent());
        System.out.println(file);

        PostDetailResponse result = postService.createPost(dto, file);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    //글 수정
    @Operation(summary = "글 수정 (내용만)")
    @PutMapping("/{idx}")
    public ResponseEntity<PostDetailResponse> updatePost(@PathVariable long idx, @RequestBody PostDto dto){

        PostDetailResponse result = postService.update(idx, dto);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "좋아요")
    @GetMapping("/likes/{idx}")
    public ResponseEntity<Long> likePost(@PathVariable long idx){

        long result = postService.like(idx);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
