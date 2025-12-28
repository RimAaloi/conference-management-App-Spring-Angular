package ma.enset.keynoteservice.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.enset.keynoteservice.dtos.KeynoteDTO;
import ma.enset.keynoteservice.services.KeynoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keynotes")
@RequiredArgsConstructor
@Tag(name = "Keynote", description = "Keynote Management API")
public class KeynoteRestController {

    private final KeynoteService keynoteService;

    @Operation(summary = "Get all keynotes", description = "Retrieve a list of all keynotes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = KeynoteDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<KeynoteDTO>> getAllKeynotes() {
        return ResponseEntity.ok(keynoteService.getAllKeynotes());
    }

    @Operation(summary = "Get keynote by ID", description = "Retrieve a keynote by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved keynote"),
            @ApiResponse(responseCode = "404", description = "Keynote not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<KeynoteDTO> getKeynoteById(
            @Parameter(description = "Keynote ID") @PathVariable Long id) {
        return ResponseEntity.ok(keynoteService.getKeynoteById(id));
    }

    @Operation(summary = "Create a new keynote", description = "Create a new keynote speaker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Keynote created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<KeynoteDTO> createKeynote(@RequestBody KeynoteDTO keynoteDTO) {
        KeynoteDTO savedKeynote = keynoteService.saveKeynote(keynoteDTO);
        return new ResponseEntity<>(savedKeynote, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a keynote", description = "Update an existing keynote by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Keynote updated successfully"),
            @ApiResponse(responseCode = "404", description = "Keynote not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<KeynoteDTO> updateKeynote(
            @Parameter(description = "Keynote ID") @PathVariable Long id,
            @RequestBody KeynoteDTO keynoteDTO) {
        return ResponseEntity.ok(keynoteService.updateKeynote(id, keynoteDTO));
    }

    @Operation(summary = "Delete a keynote", description = "Delete a keynote by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Keynote deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Keynote not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeynote(
            @Parameter(description = "Keynote ID") @PathVariable Long id) {
        keynoteService.deleteKeynote(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search keynotes by name", description = "Search keynotes by their name")
    @GetMapping("/search/nom")
    public ResponseEntity<List<KeynoteDTO>> searchByNom(
            @Parameter(description = "Name to search") @RequestParam String nom) {
        return ResponseEntity.ok(keynoteService.searchByNom(nom));
    }

    @Operation(summary = "Search keynotes by function", description = "Search keynotes by their function/role")
    @GetMapping("/search/fonction")
    public ResponseEntity<List<KeynoteDTO>> searchByFonction(
            @Parameter(description = "Function to search") @RequestParam String fonction) {
        return ResponseEntity.ok(keynoteService.searchByFonction(fonction));
    }
}
