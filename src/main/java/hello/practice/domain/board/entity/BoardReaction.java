package hello.practice.domain.board.entity;

import hello.practice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardReaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    @Setter
    private Board board;

    @ManyToOne(fetch = LAZY)
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
