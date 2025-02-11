package hello.practice.domain.comment.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CommentDto {

    private Long id;
    private String content;
    private String writer;
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Long id, String content, String writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }
}
