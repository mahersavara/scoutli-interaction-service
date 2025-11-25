package com.scoutli.service;

import com.scoutli.api.dto.CommentDTO;
import com.scoutli.domain.entity.Comment;
import com.scoutli.domain.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    public List<CommentDTO> getCommentsByDiscoveryId(Long discoveryId) {
        return commentRepository.list("discoveryId", discoveryId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO createComment(Long discoveryId, CommentDTO.CreateRequest request, String userEmail) {
        log.info("Creating comment for discovery {} by user {}", discoveryId, userEmail);

        Comment comment = new Comment();
        comment.setContent(request.content);
        comment.setUserEmail(userEmail);
        comment.setDiscoveryId(discoveryId);

        commentRepository.persist(comment);
        return toDTO(comment);
    }

    private CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUserEmail(),
                comment.getCreatedAt());
    }
}
