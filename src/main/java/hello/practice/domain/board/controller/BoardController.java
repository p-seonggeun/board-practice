package hello.practice.domain.board.controller;

import hello.practice.domain.board.dto.request.UpdateBoardRequestDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.request.CreateBoardRequestDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.service.BoardCommandService;
import hello.practice.domain.board.service.BoardQueryService;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;

    @PostMapping("/boards")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        CreateBoardResponseDto createBoardResponseDto = boardCommandService.createBoard(createBoardRequestDto, customUserDetails);

        return ResponseEntity.ok(createBoardResponseDto);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardDto>> getAllBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        List<BoardDto> boardDtos = boardQueryService.findAll();

        return ResponseEntity.ok(boardDtos);
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        boardCommandService.increaseBoardViewsById(boardId);
        BoardDto boardDto = boardQueryService.findBoardById(boardId);

        return ResponseEntity.ok(boardDto);
    }

    @PatchMapping("/boards/{id}")
    public ResponseEntity<BoardDto> updateBoardById(@PathVariable("id") Long boardId, @Valid @RequestBody UpdateBoardRequestDto updateBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        if (!boardQueryService.findBoardById(boardId).getWriter().equals(customUserDetails.getNickname())) {
            throw new BusinessException(ErrorCode.BOARD_FORBIDDEN, "게시물에 권한이 없습니다");
        }
        BoardDto boardDto = boardCommandService.updateBoardById(boardId, updateBoardRequestDto);

        return ResponseEntity.ok(boardDto);
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        if (!boardQueryService.findBoardById(boardId).getWriter().equals(customUserDetails.getNickname())) {
            throw new BusinessException(ErrorCode.BOARD_FORBIDDEN, "게시물에 권한이 없습니다");
        }
        boardCommandService.deleteBoardById(boardId);

        return ResponseEntity.ok("게시물 삭제 완료");
    }

    @PostMapping("/boards/{id}/like")
    public ResponseEntity<BoardDto> likeBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        boardCommandService.toggleLike(boardId, customUserDetails);
        BoardDto boardDto = boardQueryService.findBoardById(boardId);

        return ResponseEntity.ok(boardDto);
    }

    @PostMapping("/boards/{id}/hate")
    public ResponseEntity<BoardDto> hateBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        boardCommandService.toggleHate(boardId, customUserDetails);
        BoardDto boardDto = boardQueryService.findBoardById(boardId);

        return ResponseEntity.ok(boardDto);
    }
}
