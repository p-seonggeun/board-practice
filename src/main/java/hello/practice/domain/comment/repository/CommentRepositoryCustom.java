package hello.practice.domain.comment.repository;

import hello.practice.domain.comment.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentByBoardId(Long boardId);
}
