package ma.enset.conferenceservice.dtos;

import lombok.*;
import ma.enset.conferenceservice.enums.ConferenceType;
import ma.enset.conferenceservice.models.KeynoteDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceDTO {
    private Long id;
    private String titre;
    private ConferenceType type;
    private LocalDate date;
    private Integer duree;
    private Integer nombreInscrits;
    private Double score;
    private Long keynoteId;
    private KeynoteDTO keynote;
    private List<ReviewDTO> reviews;
}
