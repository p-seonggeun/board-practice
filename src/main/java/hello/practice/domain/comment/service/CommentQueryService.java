package hello.practice.domain.comment.service;

import hello.practice.domain.comment.dto.response.CommentDto;
import hello.practice.domain.comment.entity.Comment;
import hello.practice.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

    public List<CommentDto> getCommentByBoardId(Long boardId) {
        List<Comment> comments = commentRepository.findCommentByBoardId(boardId);

        return convertNestedStructure(comments);
    }

    private List<CommentDto> convertNestedStructure(List<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        comments.forEach(comment -> {
            CommentDto commentDto = CommentConverter.toCommentDtoFrom(comment);
            map.put(commentDto.getId(), commentDto);
            if (comment.getParent() != null) {
                map.get(comment.getParent().getId()).getChildren().add(commentDto);
            } else {
                result.add(commentDto);
            }
        });

        return result;
    }
}
