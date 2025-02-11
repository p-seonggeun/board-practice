package hello.practice.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.practice.domain.comment.entity.Comment;
import jakarta.persistence.EntityManager;

import java.util.List;

import static hello.practice.domain.comment.entity.QComment.*;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> findCommentByBoardId(Long boardId) {
        return queryFactory
                .selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.board.id.eq(boardId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst()
                ).fetch();
    }
}
