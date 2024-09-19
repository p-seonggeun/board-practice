package hello.practice.domain.board.controller;

import hello.practice.domain.board.dto.request.UpdateBoardRequestDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.request.CreateBoardRequestDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.service.BoardService;
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
    private final BoardService boardService;

    @PostMapping("/boards")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        CreateBoardResponseDto createBoardResponseDto = boardService.createBoard(createBoardRequestDto, customUserDetails);

        return ResponseEntity.ok(createBoardResponseDto);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardDto>> getAllBoard(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        List<BoardDto> boardDtos = boardService.findAll();

        return ResponseEntity.ok(boardDtos);
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardDto> getBoard(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        BoardDto boardDto = boardService.getBoard(boardId);

        return ResponseEntity.ok(boardDto);
    }

    @PostMapping("/boards/{id}")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable("id") Long boardId, @Valid @RequestBody UpdateBoardRequestDto updateBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        BoardDto boardDto = boardService.updateBoard(boardId, updateBoardRequestDto);

        return ResponseEntity.ok(boardDto);
    }
}
