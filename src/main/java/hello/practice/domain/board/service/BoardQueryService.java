package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.repository.BoardRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardQueryService {

    private final BoardRepository boardRepository;

    public BoardDto findBoardById(Long boardId) {
        Board board = boardRepository.findBoardByIdWithUser(boardId).orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다"));
        log.info("게시물 조회 완료: {}", board);

        return BoardConverter.toBoardDto(board);
    }

    // 사용 안하고 있음
    public Page<BoardDto> findAllWithPaging(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Order.desc("createdAt")));
        }

        Page<Board> boards = boardRepository.findAll(pageable);
        log.info("게시물 전체 조회 완료: {}", boards);

        return boards
                .map(BoardConverter::toBoardDto);
    }

    public Page<BoardDto> findBoardsWithCondition(BoardSearchCondition condition, Pageable pageable) {
        Page<BoardDto> searched = boardRepository.searchBoardsWithPagingAndFilters(condition, pageable);
        log.info("게시물 검색 조회 완료: {}", searched);
        return searched;
    }
}
