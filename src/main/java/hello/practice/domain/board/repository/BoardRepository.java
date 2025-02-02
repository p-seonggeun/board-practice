package hello.practice.domain.board.repository;

import hello.practice.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Override
    Page<Board> findAll(Pageable pageable);

    List<Board> findByTitle(String title);

    List<Board> findByContent(String content);

    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id")
    Optional<Board> findBoardByIdWithUser(Long id);

    void deleteById(Long id);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.id = :boardId")
    void incrementViews(@Param("boardId") Long boardId);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Query("UPDATE Board b SET b.likeCount = b.likeCount + 1 WHERE b.id = :boardId")
    void plusLikes(@Param("boardId") Long boardId);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Query("UPDATE Board b SET b.likeCount = b.likeCount - 1 WHERE b.id = :boardId")
    void minusLikes(@Param("boardId") Long boardId);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Query("UPDATE Board b SET b.hateCount = b.hateCount + 1 WHERE b.id = :boardId")
    void plusHates(@Param("boardId") Long boardId);

//    @Modifying(clearAutomatically = true)
    @Modifying
    @Query("UPDATE Board b SET b.hateCount = b.hateCount - 1 WHERE b.id = :boardId")
    void minusHates(@Param("boardId") Long boardId);
}
