package hello.practice.domain.board.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardDto {

    private String title;
    private String content;
    private String writer;
    private int views;
    private int likeCount;
    private int hateCount;

}
