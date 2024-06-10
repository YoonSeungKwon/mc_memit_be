package yoon.mc.memitService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import yoon.mc.memitService.entity.Members;

@Getter
@AllArgsConstructor
public class PostDetailResponse {

    private long postIdx;

    private String writer;

    private String profile;

    private String file;

    private String content;

    private String createAt;

}
