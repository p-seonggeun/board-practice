package hello.practice.domain.board.service;

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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            log.error("인증되지 않은 사용자입니다.");
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증되지 않은 사용자입니다");
        }
        Optional<User> byUsername = userRepository.findByUsername(customUserDetails.getUsername());
        User user = byUsername.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND, "사용자를 찾을 수 없습니다"));

        Board board = new Board(createBoardRequestDto.getTitle(), createBoardRequestDto.getContent(), user);

        boardRepository.save(board);
        log.info("게시글 생성 완료: {}", board);

        return CreateBoardResponseDto
                .builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(user.getUsername())
                .views(board.getViews())
                .likeCount(board.getLikeCount())
                .hateCount(board.getHateCount())
                .build();
    }


}
