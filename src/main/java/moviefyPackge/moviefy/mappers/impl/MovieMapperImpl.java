package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.dto.MoviteDtos.MovieDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
/**
 * Mapper implementation for converting between MovieEntity and MovieDto.
 * This mapper uses ModelMapper to perform automatic property mapping
 * between the domain entity and its corresponding DTO.
 */
@Component
public class MovieMapperImpl implements Mapper<MovieEntity, MovieDto> {

    private final ModelMapper modelMapper;

    public MovieMapperImpl(ModelMapper modelMapper) {
        this.modelMapper =modelMapper;

    }


    @Override
    public MovieDto mapTo(MovieEntity movieEntity) {
        return modelMapper.map(movieEntity, MovieDto.class);

    }

    @Override
    public MovieEntity mapFrom(MovieDto movieDto) {
        return modelMapper.map(movieDto, MovieEntity.class);
    }
}
