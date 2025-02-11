package hello.practice.domain.comment.repository;

import hello.practice.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    /**
     * 댓글 id로 조회시
     * 게시물 페치 조인
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.board WHERE c.id = :commentId")
    Optional<Comment> findCommentById(Long commentId);
}
