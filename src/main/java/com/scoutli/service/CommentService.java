package com.scoutli.service;

import com.scoutli.api.dto.CommentDTO;
// import com.scoutli.api.dto.UserDTO;
// import com.scoutli.client.AuthServiceRestClient;
import com.scoutli.domain.entity.Comment;
import com.scoutli.domain.repository.CommentRepository;
import com.scoutli.event.CommentCreatedEvent;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
// import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
// import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    // Temporarily disabled for Kafka testing
    // @Inject
    // @RestClient // Inject the REST Client
    // AuthServiceRestClient authServiceRestClient;

    @Inject
    @Channel("comment-created-out")
    Emitter<CommentCreatedEvent> eventEmitter;

    // @Inject
    // ObjectMapper objectMapper;

    public List<CommentDTO> getCommentsByDiscoveryId(Long discoveryId) {
        return commentRepository.list("discoveryId", discoveryId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO createComment(Long discoveryId, CommentDTO.CreateRequest request, String userEmail) {
        log.info("Creating comment for discovery {} by user {}", discoveryId, userEmail);

        // Temporarily disabled for Kafka testing
        // Optional<UserDTO> userDetailsOptional =
        // authServiceRestClient.getMyUserDetails()
        // .onFailure().recoverWithItem((UserDTO) null)
        // .onItem().transform(Optional::ofNullable)
        // .await().indefinitely();
        //
        // if (userDetailsOptional.isPresent()) {
        // UserDTO userDetails = userDetailsOptional.get();
        // log.info("Fetched user details from Auth Service: {}", userDetails);
        // } else {
        // log.warn("Proceeding with comment creation using only userEmail due to
        // failure to fetch user details from Auth Service.");
        // }

        Comment comment = new Comment();
        comment.setContent(request.content);
        comment.setUserEmail(userEmail);
        comment.setDiscoveryId(discoveryId);

        // save comment to db
        commentRepository.persist(comment);
        CommentDTO dto = toDTO(comment);

        CommentCreatedEvent event = new CommentCreatedEvent(comment.getId(), discoveryId, userEmail,
                request.content);

        eventEmitter.send(event).toCompletableFuture().join();
        log.info("✅ Comment created (ID: {}) and event emitted to Kafka", comment.getId());

        return dto;
    }

    private CommentDTO toDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUserEmail(),
                comment.getCreatedAt());
    }
}
