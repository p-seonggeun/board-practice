package hello.practice.domain.board.repository;

import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.response.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardDto> searchBoardsWithPagingAndFilters(BoardSearchCondition condition, Pageable pageable);
}
