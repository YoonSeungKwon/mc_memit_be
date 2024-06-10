package yoon.mc.memitService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import yoon.mc.memitService.enums.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members")
public class Members {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberIdx;

    @Column(nullable = false, length = 500)
    private String deviceId;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 250)
    private String profile;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    Members(String deviceId, String nickname, String profile, Role role){
        this.deviceId = deviceId;
        this.nickname = nickname;
        this.profile = profile;
        this.role = role;
    }

    public Collection<GrantedAuthority> getAuthority(){
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getValue()));
        return authorities;
    }

}

