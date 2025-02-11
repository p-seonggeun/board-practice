package hello.practice.domain.board.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.practice.domain.board.dto.request.BoardSearchCondition;
import hello.practice.domain.board.dto.response.BoardDto;
import hello.practice.domain.board.dto.response.QBoardDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
                .orderBy(getSortOrder(pageable))
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

    /**
     * 게시물 제목이 일치하는가
     * @param title
     * @return
     */
    private BooleanExpression titleEq(String title) {
        return StringUtils.hasText(title) ? board.title.eq(title) : null;
    }

    /**
     * 게시물 내용을 포함하는가
     * @param content
     * @return
     */
    private BooleanExpression contentLike(String content) {
        return StringUtils.hasText(content) ? board.content.like("%" + content + "%") : null;
    }

    /**
     * 작성자가 일치하는가
     * @param writer
     * @return
     */
    private BooleanExpression writerEq(String writer) {
        return StringUtils.hasText(writer) ? user.nickname.eq(writer) : null;
    }

    /**
     * 정렬 조건
     * 기본정렬: createdAt 내림차순
     * @param pageable
     * @return
     */
    private OrderSpecifier<?>[] getSortOrder(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return new OrderSpecifier[]{board.createdAt.desc()}; // 기본 정렬 조건
        }

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty())
            );
            orders.add(orderSpecifier);
        });

        return orders.toArray(new OrderSpecifier[0]);
    }
}
