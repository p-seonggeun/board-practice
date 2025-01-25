package hello.practice.domain.board.entity;

import hello.practice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public void changeReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public BoardReaction(Board board, User user, ReactionType reactionType) {
        this.board = board;
        this.user = user;
        this.reactionType = reactionType;
    }
}
