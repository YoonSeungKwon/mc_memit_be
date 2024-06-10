package yoon.mc.memitService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoon.mc.memitService.request.MemberRegister;
import yoon.mc.memitService.request.MemberUpdate;
import yoon.mc.memitService.response.MemberResponse;
import yoon.mc.memitService.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    //유저 정보
    @GetMapping("/{idx}")
    public ResponseEntity<MemberResponse> getUserInfo(@PathVariable long idx){

        MemberResponse response = memberService.getInfo(idx);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> createAccount(@RequestBody MemberRegister dto){

        MemberResponse response = memberService.register(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //회원 정보 변경
    @PatchMapping("/{idx}")
    public ResponseEntity<MemberResponse> updateUserName(@PathVariable long idx, @RequestBody MemberUpdate dto){

        MemberResponse response = memberService.updateName(idx, dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/profile/{idx}")
    public ResponseEntity<MemberResponse> updateUserName(@PathVariable long idx, @RequestBody MultipartFile file){

        MemberResponse response = memberService.updateProfile(idx, file);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
