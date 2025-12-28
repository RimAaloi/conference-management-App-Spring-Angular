package ma.enset.keynoteservice.services;

import ma.enset.keynoteservice.dtos.KeynoteDTO;

import java.util.List;

public interface KeynoteService {
    KeynoteDTO saveKeynote(KeynoteDTO keynoteDTO);

    KeynoteDTO updateKeynote(Long id, KeynoteDTO keynoteDTO);

    KeynoteDTO getKeynoteById(Long id);

    List<KeynoteDTO> getAllKeynotes();

    void deleteKeynote(Long id);

    List<KeynoteDTO> searchByNom(String nom);

    List<KeynoteDTO> searchByFonction(String fonction);
}
