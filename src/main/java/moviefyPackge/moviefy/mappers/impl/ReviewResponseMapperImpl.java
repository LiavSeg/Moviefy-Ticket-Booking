package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.ReviewEntity;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewResponseDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
/**
 * Mapper implementation for converting between ReviewEntity and ReviewResponseDto.
 * This mapper handles transformation of review data for output to clients,
 * extracting the associated user's name and the movie title explicitly during the mapping.
 */
@Component
public class ReviewResponseMapperImpl implements Mapper<ReviewEntity, ReviewResponseDto> {

    private final ModelMapper modelMapper;

    public ReviewResponseMapperImpl(ModelMapper modelMapper){this.modelMapper = modelMapper;}
    @Override
    public ReviewResponseDto mapTo(ReviewEntity reviewEntity) {
        return ReviewResponseDto.builder()
                .username(reviewEntity.getUser().getUserName())
                .movieTitle(reviewEntity.getMovie().getTitle())
                .rating(reviewEntity.getRating())
                .comment(reviewEntity.getComment())
                .createdAt(reviewEntity.getCreatedAt())
                .build();
    }

    @Override
    public ReviewEntity mapFrom(ReviewResponseDto reviewResponseDto) {
        return modelMapper.map(reviewResponseDto, ReviewEntity.class);
    }
}
