package ma.enset.conferenceservice.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.conferenceservice.clients.KeynoteClient;
import ma.enset.conferenceservice.dtos.ConferenceDTO;
import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.enums.ConferenceType;
import ma.enset.conferenceservice.exceptions.ConferenceNotFoundException;
import ma.enset.conferenceservice.mappers.ConferenceMapper;
import ma.enset.conferenceservice.models.KeynoteDTO;
import ma.enset.conferenceservice.repositories.ConferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConferenceServiceImpl implements ConferenceService {

    private final ConferenceRepository conferenceRepository;
    private final ConferenceMapper conferenceMapper;
    private final KeynoteClient keynoteClient;

    @Override
    @CircuitBreaker(name = "conferenceService", fallbackMethod = "saveConferenceFallback")
    public ConferenceDTO saveConference(ConferenceDTO conferenceDTO) {
        log.info("Saving new conference: {}", conferenceDTO.getTitre());
        Conference conference = conferenceMapper.toEntity(conferenceDTO);
        Conference savedConference = conferenceRepository.save(conference);
        return conferenceMapper.toDTO(savedConference);
    }

    @Override
    @CircuitBreaker(name = "conferenceService", fallbackMethod = "updateConferenceFallback")
    public ConferenceDTO updateConference(Long id, ConferenceDTO conferenceDTO) {
        log.info("Updating conference with id: {}", id);
        Conference existingConference = conferenceRepository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + id));

        existingConference.setTitre(conferenceDTO.getTitre());
        existingConference.setType(conferenceDTO.getType());
        existingConference.setDate(conferenceDTO.getDate());
        existingConference.setDuree(conferenceDTO.getDuree());
        existingConference.setNombreInscrits(conferenceDTO.getNombreInscrits());
        existingConference.setScore(conferenceDTO.getScore());
        existingConference.setKeynoteId(conferenceDTO.getKeynoteId());

        Conference updatedConference = conferenceRepository.save(existingConference);
        return conferenceMapper.toDTO(updatedConference);
    }

    @Override
    public ConferenceDTO getConferenceById(Long id) {
        log.info("Getting conference with id: {}", id);
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + id));
        return conferenceMapper.toDTO(conference);
    }

    @Override
    @CircuitBreaker(name = "conferenceService", fallbackMethod = "getConferenceByIdWithKeynoteFallback")
    public ConferenceDTO getConferenceByIdWithKeynote(Long id) {
        log.info("Getting conference with keynote for id: {}", id);
        Conference conference = conferenceRepository.findById(id)
                .orElseThrow(() -> new ConferenceNotFoundException("Conference not found with id: " + id));

        ConferenceDTO conferenceDTO = conferenceMapper.toDTO(conference);

        if (conference.getKeynoteId() != null) {
            try {
                KeynoteDTO keynote = keynoteClient.getKeynoteById(conference.getKeynoteId());
                conferenceDTO.setKeynote(keynote);
            } catch (Exception e) {
                log.error("Error fetching keynote: {}", e.getMessage());
            }
        }

        return conferenceDTO;
    }

    @Override
    public List<ConferenceDTO> getAllConferences() {
        log.info("Getting all conferences");
        List<Conference> conferences = conferenceRepository.findAll();
        return conferenceMapper.toDTOList(conferences);
    }

    @Override
    @CircuitBreaker(name = "conferenceService", fallbackMethod = "getAllConferencesWithKeynotesFallback")
    public List<ConferenceDTO> getAllConferencesWithKeynotes() {
        log.info("Getting all conferences with keynotes");
        List<Conference> conferences = conferenceRepository.findAll();
        List<ConferenceDTO> conferenceDTOs = conferenceMapper.toDTOList(conferences);

        conferenceDTOs.forEach(dto -> {
            if (dto.getKeynoteId() != null) {
                try {
                    KeynoteDTO keynote = keynoteClient.getKeynoteById(dto.getKeynoteId());
                    dto.setKeynote(keynote);
                } catch (Exception e) {
                    log.error("Error fetching keynote for conference {}: {}", dto.getId(), e.getMessage());
                }
            }
        });

        return conferenceDTOs;
    }

    @Override
    public void deleteConference(Long id) {
        log.info("Deleting conference with id: {}", id);
        if (!conferenceRepository.existsById(id)) {
            throw new ConferenceNotFoundException("Conference not found with id: " + id);
        }
        conferenceRepository.deleteById(id);
    }

    @Override
    public List<ConferenceDTO> getConferencesByType(ConferenceType type) {
        log.info("Getting conferences by type: {}", type);
        List<Conference> conferences = conferenceRepository.findByType(type);
        return conferenceMapper.toDTOList(conferences);
    }

    @Override
    public List<ConferenceDTO> searchByTitre(String titre) {
        log.info("Searching conferences by titre: {}", titre);
        List<Conference> conferences = conferenceRepository.findByTitreContainingIgnoreCase(titre);
        return conferenceMapper.toDTOList(conferences);
    }

    @Override
    public List<ConferenceDTO> getConferencesByKeynoteId(Long keynoteId) {
        log.info("Getting conferences by keynote id: {}", keynoteId);
        List<Conference> conferences = conferenceRepository.findByKeynoteId(keynoteId);
        return conferenceMapper.toDTOList(conferences);
    }

    // Fallback methods
    public ConferenceDTO saveConferenceFallback(ConferenceDTO conferenceDTO, Exception e) {
        log.error("Fallback for saveConference: {}", e.getMessage());
        return null;
    }

    public ConferenceDTO updateConferenceFallback(Long id, ConferenceDTO conferenceDTO, Exception e) {
        log.error("Fallback for updateConference: {}", e.getMessage());
        return null;
    }

    public ConferenceDTO getConferenceByIdWithKeynoteFallback(Long id, Exception e) {
        log.error("Fallback for getConferenceByIdWithKeynote: {}", e.getMessage());
        return getConferenceById(id);
    }

    public List<ConferenceDTO> getAllConferencesWithKeynotesFallback(Exception e) {
        log.error("Fallback for getAllConferencesWithKeynotes: {}", e.getMessage());
        return getAllConferences();
    }
}
