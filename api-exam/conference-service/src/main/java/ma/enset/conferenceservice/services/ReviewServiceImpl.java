package ma.enset.conferenceservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.conferenceservice.dtos.ReviewDTO;
import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.entities.Review;
import ma.enset.conferenceservice.exceptions.ConferenceNotFoundException;
import ma.enset.conferenceservice.exceptions.ReviewNotFoundException;
import ma.enset.conferenceservice.mappers.ReviewMapper;
import ma.enset.conferenceservice.repositories.ConferenceRepository;
import ma.enset.conferenceservice.repositories.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ConferenceRepository conferenceRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDTO saveReview(Long conferenceId, ReviewDTO reviewDTO) {
        log.info("Saving new review for conference id: {}", conferenceId);

        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + conferenceId));

        Review review = reviewMapper.toEntity(reviewDTO);
        review.setConference(conference);
        review.setDate(LocalDate.now());

        // Validate note is between 1 and 5
        if (review.getNote() < 1 || review.getNote() > 5) {
            throw new IllegalArgumentException("Note must be between 1 and 5");
        }

        Review savedReview = reviewRepository.save(review);

        // Update conference score
        updateConferenceScore(conference);

        return reviewMapper.toDTO(savedReview);
    }

    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        log.info("Updating review with id: {}", id);

        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));

        existingReview.setTexte(reviewDTO.getTexte());
        existingReview.setNote(reviewDTO.getNote());

        // Validate note is between 1 and 5
        if (existingReview.getNote() < 1 || existingReview.getNote() > 5) {
            throw new IllegalArgumentException("Note must be between 1 and 5");
        }

        Review updatedReview = reviewRepository.save(existingReview);

        // Update conference score
        updateConferenceScore(existingReview.getConference());

        return reviewMapper.toDTO(updatedReview);
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        log.info("Getting review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));
        return reviewMapper.toDTO(review);
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        log.info("Getting all reviews");
        List<Review> reviews = reviewRepository.findAll();
        return reviewMapper.toDTOList(reviews);
    }

    @Override
    public List<ReviewDTO> getReviewsByConferenceId(Long conferenceId) {
        log.info("Getting reviews for conference id: {}", conferenceId);
        if (!conferenceRepository.existsById(conferenceId)) {
            throw new ConferenceNotFoundException("Conference not found with id: " + conferenceId);
        }
        List<Review> reviews = reviewRepository.findByConferenceId(conferenceId);
        return reviewMapper.toDTOList(reviews);
    }

    @Override
    public void deleteReview(Long id) {
        log.info("Deleting review with id: {}", id);
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));

        Conference conference = review.getConference();
        reviewRepository.deleteById(id);

        // Update conference score after deletion
        updateConferenceScore(conference);
    }

    private void updateConferenceScore(Conference conference) {
        List<Review> reviews = reviewRepository.findByConferenceId(conference.getId());
        if (!reviews.isEmpty()) {
            double averageScore = reviews.stream()
                    .mapToInt(Review::getNote)
                    .average()
                    .orElse(0.0);
            conference.setScore(Math.round(averageScore * 100.0) / 100.0);
            conferenceRepository.save(conference);
        }
    }
}
