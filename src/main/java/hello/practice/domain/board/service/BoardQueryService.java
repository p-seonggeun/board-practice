package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.repository.BoardRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<BoardDto> findAll() {
        List<Board> boards = boardRepository.findAll();
        log.info("게시물 전체 조회 완료: {}", boards);
        return boards.stream()
                .map(board -> BoardConverter.toBoardDto(board))
                .toList();
    }
}
