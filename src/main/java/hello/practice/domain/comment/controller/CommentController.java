package hello.practice.domain.comment.controller;

import hello.practice.domain.comment.dto.request.CreateCommentRequestDto;
import hello.practice.domain.comment.dto.response.CreateChildCommentResponseDto;
import hello.practice.domain.comment.dto.response.CreateCommentResponseDto;
import hello.practice.domain.comment.service.CommentCommandService;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentCommandService commentCommandService;

    // 게시물에 댓글 작성
    @PostMapping("/comments/{boardId}")
    public ResponseEntity<CreateCommentResponseDto> createComment(@PathVariable("boardId") Long boardId,
                                                                  @Valid @RequestBody CreateCommentRequestDto createCommentRequestDto,
                                                                  @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CreateCommentResponseDto createCommentResponseDto = commentCommandService.createComment(boardId, createCommentRequestDto, customUserDetails);

        return ResponseEntity.ok(createCommentResponseDto);
    }

    @PostMapping("/comments/{commentId}/child")
    public ResponseEntity<CreateChildCommentResponseDto> createChildComment(@PathVariable("commentId") Long commentId,
                                                                            @Valid @RequestBody CreateCommentRequestDto createCommentRequestDto,
                                                                            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CreateChildCommentResponseDto createChildCommentResponseDto = commentCommandService.createChildComment(commentId, createCommentRequestDto, customUserDetails);

        return ResponseEntity.ok(createChildCommentResponseDto);
    }
}