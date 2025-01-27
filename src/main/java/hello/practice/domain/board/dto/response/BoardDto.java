package hello.practice.domain.board.dto.response;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public BoardDto(String title, String content, String writer, int views, int likeCount, int hateCount) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.views = views;
        this.likeCount = likeCount;
        this.hateCount = hateCount;
    }
}
