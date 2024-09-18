package hello.practice.domain.board.controller;

import hello.practice.domain.board.dto.request.CreateBoardRequestDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.service.BoardService;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CreateBoardResponseDto createBoardResponseDto = boardService.createBoard(createBoardRequestDto, customUserDetails);

        return ResponseEntity.ok(createBoardResponseDto);
    }
}
