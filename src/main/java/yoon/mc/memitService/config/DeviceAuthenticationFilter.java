package yoon.mc.memitService.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import yoon.mc.memitService.entity.Members;
import yoon.mc.memitService.repository.MemberRepository;

import java.io.IOException;

@RequiredArgsConstructor
public class DeviceAuthenticationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if(token != null){
            Members members = memberRepository.findMembersByDeviceId(token).orElseThrow();
            Authentication authentication = new UsernamePasswordAuthenticationToken(members, null, members.getAuthority());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);

    }
}
