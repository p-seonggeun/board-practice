package hello.practice.domain.board.repository;

import hello.practice.domain.board.entity.Board;
import hello.practice.domain.board.entity.BoardReaction;
import hello.practice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardReactionRepository extends JpaRepository<BoardReaction, Long> {

    Optional<BoardReaction> findByBoardAndUser(Board board, User user);

}
