package hello.practice.domain.board.entity;

import hello.practice.domain.board.dto.response.CreateBoardResponseDto;
import hello.practice.domain.common.BaseEntity;
import hello.practice.domain.token.entity.RefreshToken;
import hello.practice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    public Board(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        views = 0;
        likeCount = 0;
        hateCount = 0;
    }

    public CreateBoardResponseDto toCreateBoardResponseDto() {
        return CreateBoardResponseDto
                        .builder()
                        .title(title)
                        .content(content)
                        .writer(user.getNickname())
                        .views(views)
                        .likeCount(likeCount)
                        .hateCount(hateCount)
                        .build();
    }
}
