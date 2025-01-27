package hello.practice.domain.board.controller;

import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.request.UpdateBoardRequestDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.request.CreateBoardRequestDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.service.BoardCommandService;
import hello.practice.domain.board.service.BoardQueryService;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardCommandService boardCommandService;
    private final BoardQueryService boardQueryService;

    // 게시물 생성 기능
    @PostMapping("/boards")
    public ResponseEntity<CreateBoardResponseDto> createBoard(@Valid @RequestBody CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        CreateBoardResponseDto createBoardResponseDto = boardCommandService.createBoard(createBoardRequestDto, customUserDetails);
        return ResponseEntity.ok(createBoardResponseDto);
    }

//    // 전체 게시물 조회 기능
//    @GetMapping("/boards")
//    public ResponseEntity<Page<BoardDto>> getAllBoard(Pageable pageable) {
//        Page<BoardDto> boardDtos = boardQueryService.findAllWithPaging(pageable);
//
//        return ResponseEntity.ok(boardDtos);
//    }

    // 게시물 상세 조회 기능
    @GetMapping("/boards/{id}")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable("id") Long boardId) {
        boardCommandService.increaseBoardViewsById(boardId);
        BoardDto boardDto = boardQueryService.findBoardById(boardId);

        return ResponseEntity.ok(boardDto);
    }

    // 게시물 수정 기능
    @PatchMapping("/boards/{id}")
    public ResponseEntity<BoardDto> updateBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody UpdateBoardRequestDto updateBoardRequestDto) {
        BoardDto boardDto = boardCommandService.updateBoardById(boardId, updateBoardRequestDto);

        return ResponseEntity.ok(boardDto);
    }

    // 게시물 삭제 기능
    @DeleteMapping("/boards/{id}")
    public ResponseEntity<String> deleteBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boardCommandService.deleteBoardById(boardId);

        return ResponseEntity.ok("게시물 삭제 완료");
    }

    // 좋아요 기능
    @PostMapping("/boards/{id}/like")
    public ResponseEntity<BoardDto> likeBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boardCommandService.toggleLike(boardId, customUserDetails);
        BoardDto boardDto = boardQueryService.findBoardById(boardId);

        return ResponseEntity.ok(boardDto);
    }

    // 싫어요 기능
    @PostMapping("/boards/{id}/hate")
    public ResponseEntity<BoardDto> hateBoardById(@PathVariable("id") Long boardId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boardCommandService.toggleHate(boardId, customUserDetails);
        BoardDto boardDto = boardQueryService.findBoardById(boardId);

        return ResponseEntity.ok(boardDto);
    }

    /**
     * 동적쿼리 게시물 검색 기능 + 페이징
     * 정렬 조건이 없다면 기본적으로 createdAt 내림차순
     */
    @GetMapping("/boards")
    public Page<BoardDto> searchBoards(BoardSearchCondition condition, Pageable pageable) {
        return boardQueryService.findBoardsWithCondition(condition, pageable);
    }
}
