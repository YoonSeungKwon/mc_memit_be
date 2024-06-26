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
import yoon.mc.memitService.entity.Members;
import yoon.mc.memitService.enums.Role;
import yoon.mc.memitService.exception.MemitException;
import yoon.mc.memitService.repository.MemberRepository;
import yoon.mc.memitService.request.MemberRegister;
import yoon.mc.memitService.request.MemberUpdate;
import yoon.mc.memitService.response.MemberResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AmazonS3Client amazonS3Client;

    private final String bucket = "memit";
    private final String region = "ap-northeast-2";

    private MemberResponse toResponse(Members members){
        return new MemberResponse(members.getDeviceId(), members.getNickname(), members.getProfile(), String.valueOf(members.getCreatedAt()));
    }

    @Transactional
    public MemberResponse existById(String deviceId){
        boolean isMember = memberRepository.existsByDeviceId(deviceId);
        if(isMember)return toResponse(memberRepository.findMembersByDeviceId(deviceId).orElseThrow());
        else return null;
    }

    @Transactional
    public boolean existByNickname(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    //유저 정보
    @Transactional(readOnly = true)
    public MemberResponse getInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members currentMember = (Members) authentication.getPrincipal();

        return toResponse(currentMember);
    }

    //회원가입
    @Transactional
    public MemberResponse register(MemberRegister dto){
        String uuid = String.valueOf(UUID.randomUUID());

        Members members = Members.builder()
                .deviceId(uuid)
                .nickname(dto.getNickname())
                .profile("https://memit.s3.ap-northeast-2.amazonaws.com/members/default.jpg")
                .role(Role.USER)
                .build();

        return toResponse(memberRepository.save(members));
    }

    //정보 변경
    @Transactional
    public MemberResponse updateName(MemberUpdate dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members members = (Members) authentication.getPrincipal();

        members.setNickname(dto.getNickname());

        return toResponse(memberRepository.save(members));
    }

    @Transactional
    public MemberResponse updateProfile(MultipartFile file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)
            throw new MemitException("로그인 만료", HttpStatus.UNAUTHORIZED);

        Members members = (Members) authentication.getPrincipal();

        String url;
        UUID uuid = UUID.randomUUID();
        if (!file.getContentType().startsWith("image")) {
            throw new MemitException("지원하지 않는 파일 형식", HttpStatus.BAD_REQUEST);
        }
        try {
            String fileName = uuid + file.getOriginalFilename();
            String fileUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/members/" + members.getMemberIdx() + "/" + fileName;
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            System.out.println(file.getContentType());
            url = fileUrl;
            amazonS3Client.putObject(bucket +"/members/" + members.getMemberIdx(), fileName, file.getInputStream(), objectMetadata);
        } catch (Exception e){
            throw new MemitException("서버에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        members.setProfile(url);

        return toResponse(memberRepository.save(members));

    }

}
