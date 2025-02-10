package hello.practice.domain.comment.service;

import hello.practice.domain.comment.dto.response.CommentDto;
import hello.practice.domain.comment.entity.Comment;

public class CommentConverter {

    public static CommentDto toCommentDtoFrom(Comment comment) {
        return new CommentDto(comment.getBoard().getTitle(), comment.getContent(), comment.getUser().getNickname());
    }
}
