package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.user.entity.User;

public class BoardConverter {

    public static CreateBoardResponseDto toCreateBoardResponseDto(Board board, User user) {
        return CreateBoardResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(user.getNickname())
                .views(board.getViews())
                .likeCount(board.getLikeCount())
                .hateCount(board.getHateCount())
                .build();
    }

    public static BoardDto toBoardDto(Board board) {
        return BoardDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .writer(board.getUser().getNickname())
                .likeCount(board.getLikeCount())
                .hateCount(board.getHateCount())
                .build();
    }
}
