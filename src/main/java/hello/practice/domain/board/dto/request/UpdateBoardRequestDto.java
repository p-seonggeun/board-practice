package hello.practice.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateBoardRequestDto {

    @NotBlank(message = "제목은 공백일 수 없습니다")
    @Size(min = 1, max = 30, message = "제목은 1글자 이상, 30글자 이하여야 합니다")
    private String title;

    @NotBlank(message = "내용은 공백일 수 없습니다")
    @Size(min = 1, message = "내용은 1글자 이상이여야 합니다.")
    private String content;

}
