package yoon.mc.memitService.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {

    private long postIdx;

    private String file;

    private long like;

}
