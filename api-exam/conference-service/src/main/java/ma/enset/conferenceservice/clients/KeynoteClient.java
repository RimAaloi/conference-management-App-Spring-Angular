package ma.enset.conferenceservice.clients;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import ma.enset.conferenceservice.models.KeynoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "KEYNOTE-SERVICE")
public interface KeynoteClient {

    @GetMapping("/api/keynotes/{id}")
    @CircuitBreaker(name = "keynoteClient", fallbackMethod = "getKeynoteByIdFallback")
    KeynoteDTO getKeynoteById(@PathVariable("id") Long id);

    @GetMapping("/api/keynotes")
    @CircuitBreaker(name = "keynoteClient", fallbackMethod = "getAllKeynotesFallback")
    List<KeynoteDTO> getAllKeynotes();

    // Fallback methods
    default KeynoteDTO getKeynoteByIdFallback(Long id, Exception e) {
        return KeynoteDTO.builder()
                .id(id)
                .nom("N/A")
                .prenom("N/A")
                .email("N/A")
                .fonction("N/A")
                .build();
    }

    default List<KeynoteDTO> getAllKeynotesFallback(Exception e) {
        return List.of();
    }
}
