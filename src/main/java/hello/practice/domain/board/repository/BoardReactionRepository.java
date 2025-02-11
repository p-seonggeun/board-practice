package hello.practice.domain.board.repository;

import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.entity.BoardReaction;
import hello.practice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardReactionRepository extends JpaRepository<BoardReaction, Long> {

    /**
     * 유저 id, 게시물 id로 reaction 찾기
     * @param board
     * @param user
     * @return
     */
    @Query("SELECT br FROM BoardReaction br " +
            "JOIN FETCH br.user " +
            "JOIN FETCH br.board " +
            "WHERE br.board = :board " +
            "AND br.user = :user")
    Optional<BoardReaction> findByBoardAndUser(@Param("board") Board board, @Param("user") User user);

}
