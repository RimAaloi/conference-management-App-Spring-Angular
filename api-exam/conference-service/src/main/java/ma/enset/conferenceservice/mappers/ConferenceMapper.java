package ma.enset.conferenceservice.mappers;

import ma.enset.conferenceservice.dtos.ConferenceDTO;
import ma.enset.conferenceservice.entities.Conference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ReviewMapper.class })
public interface ConferenceMapper {

    @Mapping(target = "reviews", source = "reviews")
    ConferenceDTO toDTO(Conference conference);

    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "keynote", ignore = true)
    Conference toEntity(ConferenceDTO conferenceDTO);

    List<ConferenceDTO> toDTOList(List<Conference> conferences);
}
