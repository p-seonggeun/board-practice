package hello.practice.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCommentRequestDto {

    @NotBlank(message = "내용은 공백일 수 없습니다")
    @Size(min = 1, message = "내용은 1글자 이상이여야 합니다.")
    private String content;

}
