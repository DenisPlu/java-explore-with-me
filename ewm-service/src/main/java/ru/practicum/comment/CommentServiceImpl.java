package ru.practicum.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.CommentNewDto;
import ru.practicum.comment.dto.CommentUpdateDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.CommentState;
import ru.practicum.comment.model.CommentStateAction;
import ru.practicum.event.service.EventRepository;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Override
    public CommentFullDto create(Long userId, CommentNewDto commentNewDto) {
        boolean isNotExists = commentRepository.getByUserAndEventId(userId, commentNewDto.getEventId()).isEmpty();
        if (isNotExists) {
            Comment comment = commentRepository.save(new Comment(
                    commentNewDto.getId(),
                    commentNewDto.getText(),
                    LocalDateTime.now(),
                    null,
                    commentNewDto.getEventId(),
                    userId,
                    CommentState.WAITING
            ));
            return CommentMapper.toCommentFullDtoFromComment(
                    comment,
                    eventRepository.getReferenceById(comment.getEventId()).getTitle(),
                    userRepository.getReferenceById(userId).getName()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Событие не удовлетворяет правилам создания");
        }
    }

    @Override
    public List<CommentFullDto> getByUserId(Long userId, Integer size, Integer from) {
        List<Comment> comments = commentRepository.getByUserId(userId, size, from);
        List<CommentFullDto> fullDtoComments = new ArrayList<>();
        for (Comment comment : comments) {
            fullDtoComments.add(CommentMapper.toCommentFullDtoFromComment(
                    comment,
                    eventRepository.getReferenceById(comment.getEventId()).getTitle(),
                    userRepository.getReferenceById(userId).getName()
            ));
        }
        return fullDtoComments;
    }

    @Override
    public List<CommentFullDto> getByEventId(Long eventId, Integer size, Integer from) {
        List<Comment> comments = commentRepository.getByEventId(eventId, size, from);
        List<CommentFullDto> fullDtoComments = new ArrayList<>();
        for (Comment comment : comments) {
            fullDtoComments.add(CommentMapper.toCommentFullDtoFromComment(
                    comment,
                    eventRepository.getReferenceById(eventId).getTitle(),
                    userRepository.getReferenceById(comment.getAuthorId()).getName()
            ));
        }
        return fullDtoComments;
    }

    @Override
    public CommentFullDto updateByUser(Long userId, Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.getReferenceById(commentId);
        if (comment.getStatus().equals(CommentState.WAITING)
                && comment.getAuthorId().equals(userId)) {
            if (Optional.ofNullable(commentUpdateDto.getText()).isPresent()) {
                comment.setText(commentUpdateDto.getText());
            }
            return CommentMapper.toCommentFullDtoFromComment(
                    comment,
                    eventRepository.getReferenceById(comment.getEventId()).getTitle(),
                    userRepository.getReferenceById(userId).getName()
            );
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Комментарий должен быть в состоянии ожидания");
        }
    }

    @Override
    public CommentFullDto updateByAdmin(Long commentId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.getReferenceById(commentId);
        if (Optional.ofNullable(commentUpdateDto.getText()).isPresent()) {
            comment.setText(commentUpdateDto.getText());
        }
        if (Optional.ofNullable(commentUpdateDto.getStateAction()).isPresent()) {
            if (commentUpdateDto.getStateAction().equals(CommentStateAction.PUBLISHING)){
                comment.setStatus(CommentState.PUBLISHED);
            }
            if (commentUpdateDto.getStateAction().equals(CommentStateAction.REJECTING)){
                comment.setStatus(CommentState.CANCELED);
            }
        }
        return CommentMapper.toCommentFullDtoFromComment(
                comment,
                eventRepository.getReferenceById(comment.getEventId()).getTitle(),
                userRepository.getReferenceById(comment.getAuthorId()).getName()
        );
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}