package com.scoutli.service;

import com.scoutli.api.dto.RatingDTO;
import com.scoutli.domain.entity.Rating;
import com.scoutli.domain.repository.RatingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class RatingService {

    @Inject
    RatingRepository ratingRepository;

    @Transactional
    public RatingDTO createOrUpdateRating(Long discoveryId, RatingDTO.CreateRequest request, String userEmail) {
        log.info("Rating discovery {} by user {} with score {}", discoveryId, userEmail, request.score);

        Rating rating = ratingRepository.find("userEmail = ?1 and discoveryId = ?2", userEmail, discoveryId)
                .firstResult();
        if (rating == null) {
            rating = new Rating();
            rating.setUserEmail(userEmail);
            rating.setDiscoveryId(discoveryId);
        }
        rating.setScore(request.score);

        ratingRepository.persist(rating);
        return toDTO(rating);
    }

    private RatingDTO toDTO(Rating rating) {
        return new RatingDTO(
                rating.getId(),
                rating.getScore(),
                rating.getUserEmail());
    }
}
