package hello.practice.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {

    private String boardTitle;
    private String content;
    private String writer;

}
