package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.response.BoardDetailDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.repository.BoardRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {

    private final BoardRepository boardRepository;

    // 게시물 Dto 조회
    public BoardDto findBoardDtoById(Long boardId) {
        Board board = boardRepository.findBoardByIdWithUser(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다"));
        log.info("게시물 조회 완료: {}", board);

        return BoardConverter.toBoardDtoFrom(board);
    }

    // 게시물 상세 Dto 조회(댓글 리스트 포함)
    public BoardDetailDto findBoardDetailDtoById(Long boardId) {
        Board board = boardRepository.findBoardDetailByIdWithUserAndComments(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다"));
        log.info("게시물 상세 조회 완료: {}", board);

        return BoardConverter.toBoardDetailDtoFrom(board);
    }

    // 게시물 엔티티 조회
    public Board findBoardById(Long boardId) {
        Board board = boardRepository.findBoardByIdWithUser(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다"));

        log.info("게시물(엔티티) 조회 완료: {}", board);
        return board;
    }

    // 게시물 페이징 조회
    public Page<BoardDto> findBoardsWithCondition(BoardSearchCondition condition, Pageable pageable) {
        Page<BoardDto> searched = boardRepository.searchBoardsWithPagingAndFilters(condition, pageable);

        log.info("게시물 검색 조회 완료: {}", searched);
        return searched;
    }

}
