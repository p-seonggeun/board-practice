package hello.practice.domain.board.repository;

import hello.practice.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    /**
     * 게시물 id로 조회 시 유저 페치 조인
     * @param id
     * @return
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id")
    Optional<Board> findBoardByIdWithUser(Long id);

    /**
     * 게시물 ID로 상세조회
     * 유저 페치 조인
     * @param boardId
     * @return Optional<Board>
     */
    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :boardId")
    Optional<Board> findBoardDetailByIdWithUser(Long boardId);

    /**
     * 특정 게시물 삭제: 나중에 isDeleted 고려해보기
     * @param id
     */
    void deleteById(Long id);

    /**
     * 특정 게시물 조회수 증가
     * @param boardId
     */
    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.id = :boardId")
    void incrementViews(@Param("boardId") Long boardId);

    /**
     * 특정 게시물 좋아요 증가
     * @param boardId
     */
    @Modifying
    @Query("UPDATE Board b SET b.likeCount = b.likeCount + 1 WHERE b.id = :boardId")
    void plusLikes(@Param("boardId") Long boardId);

    /**
     * 특정 게시물 좋아요 감소
     * @param boardId
     */
    @Modifying
    @Query("UPDATE Board b SET b.likeCount = b.likeCount - 1 WHERE b.id = :boardId")
    void minusLikes(@Param("boardId") Long boardId);

    /**
     * 특정 게시물 싫어요 증가
     * @param boardId
     */
    @Modifying
    @Query("UPDATE Board b SET b.hateCount = b.hateCount + 1 WHERE b.id = :boardId")
    void plusHates(@Param("boardId") Long boardId);

    /**
     * 특정 게시물 싫어요 감소
     * @param boardId
     */
    @Modifying
    @Query("UPDATE Board b SET b.hateCount = b.hateCount - 1 WHERE b.id = :boardId")
    void minusHates(@Param("boardId") Long boardId);
}
