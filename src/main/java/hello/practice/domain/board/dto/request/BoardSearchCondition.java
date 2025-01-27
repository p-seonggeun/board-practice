package hello.practice.domain.board.dto.request;

import lombok.Data;

@Data
public class BoardSearchCondition {

    private String title;
    private String content;
    private String writer;

}
