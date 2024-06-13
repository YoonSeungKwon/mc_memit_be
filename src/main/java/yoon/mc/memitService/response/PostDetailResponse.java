package yoon.mc.memitService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import yoon.mc.memitService.entity.Members;

@Getter
@Setter
@AllArgsConstructor
public class PostDetailResponse {

    private long postIdx;

    private String writer;

    private String profile;

    private String file;

    private String content;

    private String createAt;

    private long like;

    private boolean liked;

}
