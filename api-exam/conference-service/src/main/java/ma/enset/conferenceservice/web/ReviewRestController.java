package ma.enset.conferenceservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.enset.conferenceservice.dtos.ReviewDTO;
import ma.enset.conferenceservice.services.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Review Management API")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Operation(summary = "Get all reviews", description = "Retrieve a list of all reviews")
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @Operation(summary = "Get review by ID", description = "Retrieve a review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved review"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @Parameter(description = "Review ID") @PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @Operation(summary = "Update a review", description = "Update an existing review by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "Review ID") @PathVariable Long id,
            @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDTO));
    }

    @Operation(summary = "Delete a review", description = "Delete a review by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID") @PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
