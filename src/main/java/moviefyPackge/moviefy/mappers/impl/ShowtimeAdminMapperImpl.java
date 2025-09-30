package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.MovieEntity;
import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.MoviteDtos.MovieDto;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeAdminDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
/**
 * Mapper implementation for converting between ShowtimeEntity and ShowtimeAdminDto.
 * In addition to the basic field mapping handled by ModelMapper, this mapper explicitly
 * sets the nested MovieDto using a separate Mapper instance for movie mapping.
 */
@Component
public class ShowtimeAdminMapperImpl implements Mapper<ShowtimeEntity, ShowtimeAdminDto> {

    private final ModelMapper modelMapper;
    private final Mapper<MovieEntity, MovieDto> movieMapper;


    public ShowtimeAdminMapperImpl (ModelMapper modelMapper, Mapper<MovieEntity, MovieDto> movieMapper) {
        this.modelMapper = modelMapper;
        this.movieMapper = movieMapper;
    }

    @Override
    public ShowtimeAdminDto mapTo(ShowtimeEntity showtimeEntity) {
        ShowtimeAdminDto dto = modelMapper.map(showtimeEntity, ShowtimeAdminDto.class);
        dto.setMovie(movieMapper.mapTo(showtimeEntity.getMovie()));
        return modelMapper.map(showtimeEntity,ShowtimeAdminDto.class);
    }

    @Override
    public ShowtimeEntity mapFrom(ShowtimeAdminDto showtimeAdminDto) {
        return modelMapper.map(showtimeAdminDto,ShowtimeEntity.class);
    }
}
