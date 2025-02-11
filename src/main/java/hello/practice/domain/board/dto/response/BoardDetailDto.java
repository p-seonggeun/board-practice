package hello.practice.domain.board.dto.response;

import hello.practice.domain.comment.dto.response.CommentDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardDetailDto {

    private String title;
    private String content;
    private String writer;
    private int views;
    private int likeCount;
    private int hateCount;
    private List<CommentDto> comments;


}
