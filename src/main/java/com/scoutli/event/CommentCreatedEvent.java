package com.scoutli.event;

import java.time.LocalDateTime;

// ## Phase 2: Simple Producer
// ### Step 4: Create Event Model

public class CommentCreatedEvent {

    public Long commentId;
    public Long discoveryId;
    public String authorEmail;
    public String content;
    public LocalDateTime timestamp;

    public CommentCreatedEvent() {
}

    // TODO: make constructor default
    public CommentCreatedEvent(Long commentId, Long discoveryId,
            String authorEmail, String content) {
        this.commentId = commentId;
        this.discoveryId = discoveryId;
        this.authorEmail = authorEmail;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // TODO: Overide toString() for better debug
    @Override
    public String toString() {
        return "CommentCreatedEvent{" +
                "commentId=" + commentId + ", " +
                "discoveryId=" + discoveryId + ", " +
                "authorEmail=" + authorEmail + ", " +
                "content=" + content + ", " +
                "timestamp=" + timestamp +
                '}';
    }

}
