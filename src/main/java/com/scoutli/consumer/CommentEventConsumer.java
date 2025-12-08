package com.scoutli.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import com.scoutli.event.CommentCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CommentEventConsumer {
    
    @Inject
    ObjectMapper objectMapper;

    /**
     *  Consumer handling CommentCreatedEvent messages from the Kafka
     * 
     * @Incoming: only handle the channel name ( match config name)
     * 
     * @Blocking: handle Synchonously ( default is non-blocking / async)
     * 
     */

    @Incoming("comment-created-in")
    @Blocking
    public CompletionStage<Void> handleCommentCreated(String eventJson) {
        return CompletableFuture.runAsync(() -> {
            try {
                CommentCreatedEvent event = objectMapper.readValue(eventJson, CommentCreatedEvent.class);
                
                log.info("📬 Received event from Kafka: {}", event.toString());

                //TODO: Implement business logic
                // Example: Create a notification for the discovery author, sending email, update cache, etc.
                // For now, just log the event
                log.info("  - Comment ID: {}", event.commentId);
                log.info("  - Discovery ID: {}", event.discoveryId);
                log.info("  - Author Email: {}", event.authorEmail);
                log.info("  - Content: {}", event.content);
                log.info("  - Timestamp: {}", event.timestamp); 

                log.info("✅ Event processing completed.");
            } catch (Exception e) {
                log.error("Failed to deserialize event from JSON", e);
            }
        });
        
    }


    



    
}
