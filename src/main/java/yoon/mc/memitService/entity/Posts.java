package yoon.mc.memitService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postIdx;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Members members;

    @Column(nullable = false, length = 500)
    private String file;

    @Column(nullable = false, length = 500)
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private long like;

    @Builder
    Posts(Members members, String file, String content){
        this.members = members;
        this.file = file;
        this.content = content;
        this.like = 0;
    }

    public void like(){
        like += 1;
    }

    public void dislike(){
        like -=1;
    }

}