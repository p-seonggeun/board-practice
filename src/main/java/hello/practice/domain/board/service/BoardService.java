package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.request.UpdateBoardRequestDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.request.CreateBoardRequestDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.repository.BoardRepository;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.entity.User;
import hello.practice.domain.user.repository.UserRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "사용자를 찾을 수 없습니다"));
        Board board = new Board(createBoardRequestDto.getTitle(), createBoardRequestDto.getContent(), user);

        boardRepository.save(board);
        log.info("게시글 생성 완료: {}", board);

        return BoardConverter.toCreateBoardResponseDto(board, user);
    }

    @Transactional
    public BoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다."));
        board.plusViews();
        log.info("게시글 조회 완료: {}", board);
        log.info("게시글 조회수 증가: {}", board.getViews());

        return BoardConverter.toBoardDto(board);
    }

    @Transactional
    public BoardDto updateBoard(Long id, UpdateBoardRequestDto updateBoardRequestDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다."));

        board.updateBoard(updateBoardRequestDto);
        log.info("게시글 수정 완료: {}", board);
        BoardDto boardDto = BoardConverter.toBoardDto(board);
        return boardDto;
    }

    public List<BoardDto> findAll() {
        List<Board> boards = boardRepository.findAll();
        log.info("게시글 전체 조회 완료: {}", boards);
        return boards.stream()
                .map(board -> BoardConverter.toBoardDto(board))
                .toList();
    }

}
