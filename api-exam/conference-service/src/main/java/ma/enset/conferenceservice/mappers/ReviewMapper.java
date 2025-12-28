package ma.enset.conferenceservice.mappers;

import ma.enset.conferenceservice.dtos.ReviewDTO;
import ma.enset.conferenceservice.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "conferenceId", source = "conference.id")
    ReviewDTO toDTO(Review review);

    @Mapping(target = "conference", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);

    List<ReviewDTO> toDTOList(List<Review> reviews);
}
