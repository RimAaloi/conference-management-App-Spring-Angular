package ma.enset.conferenceservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.enset.conferenceservice.enums.ConferenceType;
import ma.enset.conferenceservice.models.KeynoteDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;

    @Enumerated(EnumType.STRING)
    private ConferenceType type;

    private LocalDate date;

    private Integer duree; // durée en minutes

    private Integer nombreInscrits;

    private Double score;

    private Long keynoteId; // FK vers keynote-service

    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private KeynoteDTO keynote; // Chargé via OpenFeign
}
