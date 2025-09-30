package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.ReviewEntity;
import moviefyPackge.moviefy.domain.dto.reviewsDto.ReviewRequestDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper implementation for converting between ReviewEntity and ReviewRequestDto.
 * This mapper handles transformation of review request data from clients into domain entities,
 * and vice versa, using ModelMapper for basic field mapping.
 */
@Component
public class ReviewRequestMapperImpl implements Mapper<ReviewEntity, ReviewRequestDto> {
    private final ModelMapper modelMapper;

    public ReviewRequestMapperImpl(ModelMapper modelMapper){this.modelMapper = modelMapper;

    }

    @Override
    public ReviewRequestDto mapTo(ReviewEntity reviewEntity) {
        return modelMapper.map(reviewEntity, ReviewRequestDto.class);
    }

    @Override
    public ReviewEntity mapFrom(ReviewRequestDto reviewRequestDto) {
        //movieEntity and userEntity will be assigned in Service Layer
        return modelMapper.map(reviewRequestDto, ReviewEntity.class);
    }
}
