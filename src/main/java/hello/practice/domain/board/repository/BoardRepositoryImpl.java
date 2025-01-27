package hello.practice.domain.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.response.QBoardDto;
import hello.practice.domain.board.entity.QBoard;
import hello.practice.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static hello.practice.domain.board.entity.QBoard.*;
import static hello.practice.domain.user.entity.QUser.*;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<BoardDto> searchBoardsWithPagingAndFilters(BoardSearchCondition condition, Pageable pageable) {
        List<BoardDto> content = queryFactory
                .select(new QBoardDto(
                        board.title,
                        board.content,
                        user.nickname.as("writer"),
                        board.views,
                        board.likeCount,
                        board.hateCount))
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        titleEq(condition.getTitle()),
                        contentLike(condition.getContent()),
                        writerEq(condition.getWriter())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(board.count())
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        titleEq(condition.getTitle()),
                        contentLike(condition.getContent()),
                        writerEq(condition.getWriter())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }

    private BooleanExpression titleEq(String title) {
        return StringUtils.hasText(title) ? board.title.eq(title) : null;
    }

    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? board.content.like(content) : null;
    }

    private BooleanExpression writerEq(String writer) {
        return StringUtils.hasText(writer) ? user.nickname.eq(writer) : null;
    }
}
