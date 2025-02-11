package hello.practice.domain.board.repository;

import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.response.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    /**
     * 기본 정렬 조건: createdAt 내림차순
     * 동적 검색 조건:
     * title 완전 일치
     * content 포함
     * writer 완전 일치
     * @param condition
     * @param pageable
     * @return
     */
    Page<BoardDto> searchBoardsWithPagingAndFilters(BoardSearchCondition condition, Pageable pageable);
}
