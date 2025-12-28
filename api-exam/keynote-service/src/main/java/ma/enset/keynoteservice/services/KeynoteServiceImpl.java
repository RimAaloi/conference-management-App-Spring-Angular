package ma.enset.keynoteservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.keynoteservice.dtos.KeynoteDTO;
import ma.enset.keynoteservice.entities.Keynote;
import ma.enset.keynoteservice.exceptions.KeynoteNotFoundException;
import ma.enset.keynoteservice.mappers.KeynoteMapper;
import ma.enset.keynoteservice.repositories.KeynoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class KeynoteServiceImpl implements KeynoteService {

    private final KeynoteRepository keynoteRepository;
    private final KeynoteMapper keynoteMapper;

    @Override
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "saveKeynoteFallback")
    public KeynoteDTO saveKeynote(KeynoteDTO keynoteDTO) {
        log.info("Saving new keynote: {} {}", keynoteDTO.getNom(), keynoteDTO.getPrenom());
        Keynote keynote = keynoteMapper.toEntity(keynoteDTO);
        Keynote savedKeynote = keynoteRepository.save(keynote);
        return keynoteMapper.toDTO(savedKeynote);
    }

    @Override
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "updateKeynoteFallback")
    public KeynoteDTO updateKeynote(Long id, KeynoteDTO keynoteDTO) {
        log.info("Updating keynote with id: {}", id);
        Keynote existingKeynote = keynoteRepository.findById(id)
                .orElseThrow(() -> new KeynoteNotFoundException("Keynote not found with id: " + id));

        existingKeynote.setNom(keynoteDTO.getNom());
        existingKeynote.setPrenom(keynoteDTO.getPrenom());
        existingKeynote.setEmail(keynoteDTO.getEmail());
        existingKeynote.setFonction(keynoteDTO.getFonction());

        Keynote updatedKeynote = keynoteRepository.save(existingKeynote);
        return keynoteMapper.toDTO(updatedKeynote);
    }

    @Override
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "getKeynoteByIdFallback")
    public KeynoteDTO getKeynoteById(Long id) {
        log.info("Getting keynote with id: {}", id);
        Keynote keynote = keynoteRepository.findById(id)
                .orElseThrow(() -> new KeynoteNotFoundException("Keynote not found with id: " + id));
        return keynoteMapper.toDTO(keynote);
    }

    @Override
    @CircuitBreaker(name = "keynoteService", fallbackMethod = "getAllKeynotesFallback")
    public List<KeynoteDTO> getAllKeynotes() {
        log.info("Getting all keynotes");
        List<Keynote> keynotes = keynoteRepository.findAll();
        return keynoteMapper.toDTOList(keynotes);
    }

    @Override
    public void deleteKeynote(Long id) {
        log.info("Deleting keynote with id: {}", id);
        if (!keynoteRepository.existsById(id)) {
            throw new KeynoteNotFoundException("Keynote not found with id: " + id);
        }
        keynoteRepository.deleteById(id);
    }

    @Override
    public List<KeynoteDTO> searchByNom(String nom) {
        log.info("Searching keynotes by nom: {}", nom);
        List<Keynote> keynotes = keynoteRepository.findByNomContainingIgnoreCase(nom);
        return keynoteMapper.toDTOList(keynotes);
    }

    @Override
    public List<KeynoteDTO> searchByFonction(String fonction) {
        log.info("Searching keynotes by fonction: {}", fonction);
        List<Keynote> keynotes = keynoteRepository.findByFonctionContainingIgnoreCase(fonction);
        return keynoteMapper.toDTOList(keynotes);
    }

    // Fallback methods
    public KeynoteDTO saveKeynoteFallback(KeynoteDTO keynoteDTO, Exception e) {
        log.error("Fallback for saveKeynote: {}", e.getMessage());
        return null;
    }

    public KeynoteDTO updateKeynoteFallback(Long id, KeynoteDTO keynoteDTO, Exception e) {
        log.error("Fallback for updateKeynote: {}", e.getMessage());
        return null;
    }

    public KeynoteDTO getKeynoteByIdFallback(Long id, Exception e) {
        log.error("Fallback for getKeynoteById: {}", e.getMessage());
        return null;
    }

    public List<KeynoteDTO> getAllKeynotesFallback(Exception e) {
        log.error("Fallback for getAllKeynotes: {}", e.getMessage());
        return List.of();
    }
}
