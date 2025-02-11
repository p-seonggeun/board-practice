package hello.practice.domain.board.service;

import hello.practice.domain.board.dto.response.BoardDetailDto;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.board.entity.Board;
import hello.practice.domain.comment.dto.response.CommentDto;
import hello.practice.domain.comment.entity.Comment;
import hello.practice.domain.comment.service.CommentConverter;
import hello.practice.domain.user.entity.User;

import java.util.List;

public class BoardConverter {

    public static CreateBoardResponseDto toCreateBoardResponseDtoFrom(Board board, User user) {
        return CreateBoardResponseDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .writer(user.getNickname())
                .views(board.getViews())
                .likeCount(board.getLikeCount())
                .hateCount(board.getHateCount())
                .build();
    }

    public static BoardDto toBoardDtoFrom(Board board) {
        return BoardDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .writer(board.getUser().getNickname())
                .likeCount(board.getLikeCount())
                .hateCount(board.getHateCount())
                .build();
    }

    public static BoardDetailDto toBoardDetailDtoFrom(Board board, List<CommentDto> commentDtos) {
        return BoardDetailDto.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .views(board.getViews())
                .writer(board.getUser().getNickname())
                .likeCount(board.getLikeCount())
                .hateCount(board.getHateCount())
                .comments(commentDtos)
                .build();
    }
}
