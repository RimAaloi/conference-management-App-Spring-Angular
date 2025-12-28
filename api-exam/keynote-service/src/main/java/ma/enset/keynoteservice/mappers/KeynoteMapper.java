package ma.enset.keynoteservice.mappers;

import ma.enset.keynoteservice.dtos.KeynoteDTO;
import ma.enset.keynoteservice.entities.Keynote;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KeynoteMapper {
    KeynoteDTO toDTO(Keynote keynote);

    Keynote toEntity(KeynoteDTO keynoteDTO);

    List<KeynoteDTO> toDTOList(List<Keynote> keynotes);

    List<Keynote> toEntityList(List<KeynoteDTO> keynoteDTOs);
}
