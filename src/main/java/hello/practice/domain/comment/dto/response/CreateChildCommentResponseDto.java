package hello.practice.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateChildCommentResponseDto {

    private String boardTitle;
    private Long parentCommentId;
    private String content;
    private String writer;

}
