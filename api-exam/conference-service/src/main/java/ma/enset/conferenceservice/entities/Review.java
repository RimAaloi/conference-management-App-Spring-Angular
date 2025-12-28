package ma.enset.conferenceservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Column(length = 2000)
    private String texte;

    private Integer note; // 1 à 5 stars

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    @ToString.Exclude
    private Conference conference;
}
