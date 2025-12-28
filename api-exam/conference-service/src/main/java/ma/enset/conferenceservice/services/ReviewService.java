package ma.enset.conferenceservice.services;

import ma.enset.conferenceservice.dtos.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO saveReview(Long conferenceId, ReviewDTO reviewDTO);

    ReviewDTO updateReview(Long id, ReviewDTO reviewDTO);

    ReviewDTO getReviewById(Long id);

    List<ReviewDTO> getAllReviews();

    List<ReviewDTO> getReviewsByConferenceId(Long conferenceId);

    void deleteReview(Long id);
}
