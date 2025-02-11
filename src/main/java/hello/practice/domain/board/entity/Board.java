package hello.practice.domain.board.entity;

import hello.practice.domain.board.dto.request.UpdateBoardRequestDto;
import hello.practice.domain.comment.entity.Comment;
import hello.practice.domain.common.BaseEntity;
import hello.practice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"user"})
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private int views;
    private int likeCount;
    private int hateCount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = REMOVE, orphanRemoval = true)
    private List<BoardReaction> boardReactions = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        views = 0;
        likeCount = 0;
        hateCount = 0;
    }

    public void updateBoard(UpdateBoardRequestDto updateBoardRequestDto) {
        this.title = updateBoardRequestDto.getTitle();
        this.content = updateBoardRequestDto.getContent();
    }
}
