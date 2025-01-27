package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.request.UpdateBoardRequestDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.request.CreateBoardRequestDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.entity.BoardReaction;
import hello.practice.domain.board.entity.ReactionType;
import hello.practice.domain.board.repository.BoardReactionRepository;
import hello.practice.domain.board.repository.BoardRepository;
import hello.practice.domain.user.dto.request.CustomUserDetails;
import hello.practice.domain.user.entity.User;
import hello.practice.domain.user.repository.UserRepository;
import hello.practice.global.exception.BusinessException;
import hello.practice.global.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardCommandService {

    private final BoardRepository boardRepository;
    private final BoardReactionRepository boardReactionRepository;
    private final UserRepository userRepository;

    public CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다.");
                    return new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "사용자를 찾을 수 없습니다");
                });
        Board board = new Board(createBoardRequestDto.getTitle(), createBoardRequestDto.getContent(), user);

        boardRepository.save(board);
        log.info("게시물 생성 완료: {}", board);

        return BoardConverter.toCreateBoardResponseDto(board, user);
    }

    public BoardDto updateBoardById(Long boardId, UpdateBoardRequestDto updateBoardRequestDto) {
        Board board = boardRepository.findBoardByIdWithUser(boardId).orElseThrow(() -> {
            log.error("게시물을 찾을 수 없습니다.");
            return new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다");
        });

        board.updateBoard(updateBoardRequestDto);
        log.info("게시물 수정 완료: {}", board);
        BoardDto boardDto = BoardConverter.toBoardDto(board);
        return boardDto;
    }

    public void deleteBoardById(Long boardId) {
        if (boardRepository.existsById(boardId)) {
            boardRepository.deleteById(boardId);
            log.info("게시물 삭제 완료: {}", boardId);
        } else {
            log.error("게시물을 찾을 수 없습니다.");
            throw new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다");
        }
    }

    public void increaseBoardViewsById(Long boardId) {
        Board board = boardRepository.findBoardByIdWithUser(boardId)
                .orElseThrow(() -> {
                    log.error("게시물을 찾을 수 없습니다.");
                    return new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다");
                });

        boardRepository.incrementViews(boardId);
        log.info("게시물 조회수 증가 완료: {}", board.getViews());
    }

    public void toggleLike(Long boardId, CustomUserDetails customUserDetails) {
        // 게시물 찾기
        Board board = boardRepository.findBoardByIdWithUser(boardId)
                .orElseThrow(() -> {
                    log.error("게시물을 찾을 수 없습니다.");
                    return new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다");
                });

        // 유저 찾기
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다.");
                    return new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "사용자를 찾을 수 없습니다");
                });

        // 좋아요 기록 찾기
        boardReactionRepository.findByBoardAndUser(board, user)
                .ifPresentOrElse(
                        boardReaction -> {
                            if (boardReaction.getReactionType() == ReactionType.LIKE) {
                                boardRepository.minusLikes(boardId);
                                boardReactionRepository.delete(boardReaction);
                                log.info("{}의 {} 좋아요 삭제 완료", user.getNickname(), board.getTitle());

                            } else {
                                boardRepository.plusLikes(boardId);
                                boardRepository.minusHates(boardId);
                                boardReaction.changeReactionType(ReactionType.LIKE);
                                log.info("{}의 {} 싫어요 -> 좋아요 변경 완료", user.getNickname(), board.getTitle());

                            }
                        },
                        () -> {
                            BoardReaction boardReaction = new BoardReaction(board, user, ReactionType.LIKE);
                            boardRepository.plusLikes(boardId);
                            boardReactionRepository.save(boardReaction);
                            log.info("{}의 {} 좋아요 저장 완료", user.getNickname(), board.getTitle());
                        }
                );
        log.info("좋아요 토글 완료: {}", board.getLikeCount());
    }

    public void toggleHate(Long boardId, CustomUserDetails customUserDetails) {
        Board board = boardRepository.findBoardByIdWithUser(boardId)
                .orElseThrow(() -> {
                    log.error("게시물을 찾을 수 없습니다.");
                    return new BusinessException(ErrorCode.BOARD_NOT_FOUND, "게시물을 찾을 수 없습니다");
                });
        User user = userRepository.findByUsername(customUserDetails.getUsername())
                .orElseThrow(() -> {
                    log.error("사용자를 찾을 수 없습니다.");
                    return new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "사용자를 찾을 수 없습니다");
                });
        boardReactionRepository.findByBoardAndUser(board, user)
                .ifPresentOrElse(
                        boardReaction -> {
                            if (boardReaction.getReactionType() == ReactionType.HATE) {
                                boardRepository.minusHates(boardId);
                                boardReactionRepository.delete(boardReaction);
                                log.info("{}의 {} 싫어요 삭제 완료", user.getNickname(), board.getTitle());
                            } else {
                                boardRepository.plusHates(boardId);
                                boardRepository.minusLikes(boardId);
                                boardReaction.changeReactionType(ReactionType.HATE);
                                log.info("{}의 {} 좋아요 -> 싫어요 변경 완료", user.getNickname(), board.getTitle());
                            }
                        },
                        () -> {
                            BoardReaction boardReaction = new BoardReaction(board, user, ReactionType.HATE);
                            boardRepository.plusHates(boardId);
                            boardReactionRepository.save(boardReaction);
                            log.info("{}의 {} 싫어요 저장 완료", user.getNickname(), board.getTitle());
                        }
                );
        log.info("싫어요 토글 완료: {}", board.getHateCount());
    }
}
