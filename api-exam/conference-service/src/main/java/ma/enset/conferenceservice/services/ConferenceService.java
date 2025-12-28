package ma.enset.conferenceservice.services;

import ma.enset.conferenceservice.dtos.ConferenceDTO;
import ma.enset.conferenceservice.enums.ConferenceType;

import java.util.List;

public interface ConferenceService {
    ConferenceDTO saveConference(ConferenceDTO conferenceDTO);

    ConferenceDTO updateConference(Long id, ConferenceDTO conferenceDTO);

    ConferenceDTO getConferenceById(Long id);

    ConferenceDTO getConferenceByIdWithKeynote(Long id);

    List<ConferenceDTO> getAllConferences();

    List<ConferenceDTO> getAllConferencesWithKeynotes();

    void deleteConference(Long id);

    List<ConferenceDTO> getConferencesByType(ConferenceType type);

    List<ConferenceDTO> searchByTitre(String titre);

    List<ConferenceDTO> getConferencesByKeynoteId(Long keynoteId);
}
