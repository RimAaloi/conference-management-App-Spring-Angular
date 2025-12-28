package ma.enset.conferenceservice.repositories;

import ma.enset.conferenceservice.entities.Conference;
import ma.enset.conferenceservice.enums.ConferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByType(ConferenceType type);

    List<Conference> findByTitreContainingIgnoreCase(String titre);

    List<Conference> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Conference> findByKeynoteId(Long keynoteId);
}
