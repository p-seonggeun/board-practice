package hello.practice.domain.comment.service;

import hello.practice.domain.comment.dto.response.CommentDto;
import hello.practice.domain.comment.dto.response.CreateCommentResponseDto;
import hello.practice.domain.comment.entity.Comment;

public class CommentConverter {

    public static CreateCommentResponseDto toCreateCommentResponseDtoFrom(Comment comment) {
        return new CreateCommentResponseDto(comment.getBoard().getTitle(), comment.getContent(), comment.getUser().getNickname());
    }

    public static CommentDto toCommentDtoFrom(Comment comment) {
        return new CommentDto(comment.getContent(), comment.getUser().getNickname());
    }
}
