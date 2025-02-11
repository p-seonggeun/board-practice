package hello.practice.domain.comment.service;

import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.service.BoardQueryService;
import hello.practice.domain.comment.dto.request.CreateCommentRequestDto;
import hello.practice.domain.comment.dto.response.CommentDto;
import hello.practice.domain.comment.dto.response.CreateChildCommentResponseDto;
import hello.practice.domain.comment.dto.response.CreateCommentResponseDto;
import hello.practice.domain.comment.entity.Comment;
import hello.practice.domain.comment.repository.CommentRepository;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.entity.User;
import hello.practice.domain.user.service.UserQueryService;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {

    private final CommentRepository commentRepository;
    private final UserQueryService userQueryService;
    private final BoardQueryService boardQueryService;

    // 루트 댓글 생성하기
    public CreateCommentResponseDto createComment(Long boardId, CreateCommentRequestDto createCommentRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userQueryService.getUserByUsername(customUserDetails.getUsername());
        log.info("사용자 조회 완료: {}", user);
        Board board = boardQueryService.findBoardById(boardId);
        log.info("게시물 조회 완료: {}", board);

        Comment comment = new Comment(createCommentRequestDto.getContent(), user, board);
        comment.setBoard(board);

        commentRepository.save(comment);
        log.info("댓글 저장 완료: {}", comment);

        // N + 1 문제 예상
        return CommentConverter.toCreateCommentResponseDtoFrom(comment);
    }

    // 자식 댓글 생성하기
    public CreateChildCommentResponseDto createChildComment(Long commentId, CreateCommentRequestDto createCommentRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userQueryService.getUserByUsername(customUserDetails.getUsername());
        log.info("사용자 조회 완료: {}", user);

        Comment comment = commentRepository.findCommentById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND, "댓글을 찾을 수 없습니다."));
        log.info("부모 댓글 조회 완료: {}", comment);

        Comment childComment = new Comment(createCommentRequestDto.getContent(), user, comment.getBoard());
        childComment.setParent(comment);

        commentRepository.save(childComment);
        log.info("자식 댓글 저장 완료: {}", childComment);

        return CommentConverter.toCreateChildCommentResponseDtoFrom(childComment);
    }
}
