package yoon.mc.memitService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yoon.mc.memitService.request.MemberRegister;
import yoon.mc.memitService.request.MemberUpdate;
import yoon.mc.memitService.response.MemberResponse;
import yoon.mc.memitService.service.MemberService;

@Tag(name = "멤버 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "회원인지 확인")
    @PostMapping("/is-member")
    public ResponseEntity<?> isMember(@RequestBody String deviceId){

        MemberResponse response = memberService.existById(deviceId);

        if(response == null)return new ResponseEntity<>(false, HttpStatus.OK);

        else return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(summary = "이름 중복 확인")
    @PostMapping("/nickname-check")
    public ResponseEntity<Boolean> isExist(@RequestBody String nickname){

        boolean result = memberService.existByNickname(nickname);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    //유저 정보
    @Operation(summary = "사용자 정보 불러오기 {idx} = 사용자 고유 번호")
    @GetMapping("/{idx}")
    public ResponseEntity<MemberResponse> getUserInfo(@PathVariable long idx){

        MemberResponse response = memberService.getInfo(idx);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //회원가입
    @Operation(summary = "회원가입")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> createAccount(@RequestBody MemberRegister dto){

        MemberResponse response = memberService.register(dto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    //회원 정보 변경
    @Operation(summary = "이름 바꾸기")
    @PatchMapping("/{idx}")
    public ResponseEntity<MemberResponse> updateUserName(@PathVariable long idx, @RequestBody MemberUpdate dto){

        MemberResponse response = memberService.updateName(idx, dto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "프로필 사진 바꾸기 (아직 미완)")
    @PostMapping("/profile/{idx}")
    public ResponseEntity<MemberResponse> updateUserName(@PathVariable long idx, @RequestBody MultipartFile file){

        MemberResponse response = memberService.updateProfile(idx, file);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
