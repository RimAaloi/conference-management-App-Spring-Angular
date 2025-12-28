package ma.enset.conferenceservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.enset.conferenceservice.dtos.ConferenceDTO;
import ma.enset.conferenceservice.dtos.ReviewDTO;
import ma.enset.conferenceservice.enums.ConferenceType;
import ma.enset.conferenceservice.services.ConferenceService;
import ma.enset.conferenceservice.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
@RequiredArgsConstructor
@Tag(name = "Conference", description = "Conference Management API")
public class ConferenceRestController {

    private final ConferenceService conferenceService;
    private final ReviewService reviewService;

    @Operation(summary = "Get all conferences", description = "Retrieve a list of all conferences with keynote information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConferenceDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<ConferenceDTO>> getAllConferences(
            @Parameter(description = "Include keynote details") @RequestParam(defaultValue = "true") boolean withKeynotes) {
        if (withKeynotes) {
            return ResponseEntity.ok(conferenceService.getAllConferencesWithKeynotes());
        }
        return ResponseEntity.ok(conferenceService.getAllConferences());
    }

    @Operation(summary = "Get conference by ID", description = "Retrieve a conference by its ID with keynote information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved conference"),
            @ApiResponse(responseCode = "404", description = "Conference not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConferenceDTO> getConferenceById(
            @Parameter(description = "Conference ID") @PathVariable Long id,
            @Parameter(description = "Include keynote details") @RequestParam(defaultValue = "true") boolean withKeynote) {
        if (withKeynote) {
            return ResponseEntity.ok(conferenceService.getConferenceByIdWithKeynote(id));
        }
        return ResponseEntity.ok(conferenceService.getConferenceById(id));
    }

    @Operation(summary = "Create a new conference", description = "Create a new conference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conference created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ConferenceDTO> createConference(@RequestBody ConferenceDTO conferenceDTO) {
        ConferenceDTO savedConference = conferenceService.saveConference(conferenceDTO);
        return new ResponseEntity<>(savedConference, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a conference", description = "Update an existing conference by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conference updated successfully"),
            @ApiResponse(responseCode = "404", description = "Conference not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConferenceDTO> updateConference(
            @Parameter(description = "Conference ID") @PathVariable Long id,
            @RequestBody ConferenceDTO conferenceDTO) {
        return ResponseEntity.ok(conferenceService.updateConference(id, conferenceDTO));
    }

    @Operation(summary = "Delete a conference", description = "Delete a conference by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conference deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Conference not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConference(
            @Parameter(description = "Conference ID") @PathVariable Long id) {
        conferenceService.deleteConference(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get conferences by type", description = "Retrieve conferences by type (ACADEMIC or COMMERCIAL)")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByType(
            @Parameter(description = "Conference Type") @PathVariable ConferenceType type) {
        return ResponseEntity.ok(conferenceService.getConferencesByType(type));
    }

    @Operation(summary = "Search conferences by title", description = "Search conferences by their title")
    @GetMapping("/search")
    public ResponseEntity<List<ConferenceDTO>> searchByTitre(
            @Parameter(description = "Title to search") @RequestParam String titre) {
        return ResponseEntity.ok(conferenceService.searchByTitre(titre));
    }

    @Operation(summary = "Get conferences by keynote", description = "Get all conferences for a specific keynote")
    @GetMapping("/keynote/{keynoteId}")
    public ResponseEntity<List<ConferenceDTO>> getConferencesByKeynoteId(
            @Parameter(description = "Keynote ID") @PathVariable Long keynoteId) {
        return ResponseEntity.ok(conferenceService.getConferencesByKeynoteId(keynoteId));
    }

    // Review endpoints nested under conferences
    @Operation(summary = "Get reviews for a conference", description = "Retrieve all reviews for a specific conference")
    @GetMapping("/{conferenceId}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByConferenceId(
            @Parameter(description = "Conference ID") @PathVariable Long conferenceId) {
        return ResponseEntity.ok(reviewService.getReviewsByConferenceId(conferenceId));
    }

    @Operation(summary = "Add a review to a conference", description = "Add a new review to a specific conference")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Conference not found")
    })
    @PostMapping("/{conferenceId}/reviews")
    public ResponseEntity<ReviewDTO> addReview(
            @Parameter(description = "Conference ID") @PathVariable Long conferenceId,
            @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO savedReview = reviewService.saveReview(conferenceId, reviewDTO);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }
}
