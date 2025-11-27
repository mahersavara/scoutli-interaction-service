package com.scoutli.service;

import com.scoutli.api.dto.CommentDTO;
import com.scoutli.api.dto.UserDTO;
import com.scoutli.client.AuthServiceRestClient;
import com.scoutli.domain.entity.Comment;
import com.scoutli.domain.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Slf4j
public class CommentService {

    @Inject
    CommentRepository commentRepository;

    @Inject
    @RestClient // Inject the REST Client
    AuthServiceRestClient authServiceRestClient;

    public List<CommentDTO> getCommentsByDiscoveryId(Long discoveryId) {
        return commentRepository.list("discoveryId", discoveryId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO createComment(Long discoveryId, CommentDTO.CreateRequest request, String userEmail) {
        log.info("Creating comment for discovery {} by user {}", discoveryId, userEmail);

        // Example of inter-service communication: Fetching User details from Auth Service
        // Using Uni<T> and blocking for simplicity in a @Transactional method
        Optional<UserDTO> userDetailsOptional = authServiceRestClient.getMyUserDetails()
                .onFailure().invoke(failure -> log.warn("Could not fetch user details from Auth Service: {}", failure.getMessage()))
                .onItem().transformToOptional(Optional::of)
                .await().indefinitely(); // Block until Uni emits an item or fails

        if (userDetailsOptional.isPresent()) {
            UserDTO userDetails = userDetailsOptional.get();
            log.info("Fetched user details from Auth Service: {}", userDetails);
            // You can now use userDetails.id, userDetails.role etc.
            // For example, if you wanted to store userId instead of userEmail,
            // you'd modify Comment entity and this line:
            // comment.setUserId(userDetails.id);
        } else {
            // Handle case where user details could not be fetched (e.g., Auth Service down or token invalid)
            // For now, proceed with comment creation using only userEmail, or throw an exception.
            log.warn("Proceeding with comment creation using only userEmail due to failure to fetch user details from Auth Service.");
        }

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
