package hello.practice.domain.board.repository;

import hello.practice.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    List<Board> findAll();
    List<Board> findByTitle(String title);
    List<Board> findByContent(String content);

    Optional<Board> findById(Long id);

    void deleteById(Long id);
}
