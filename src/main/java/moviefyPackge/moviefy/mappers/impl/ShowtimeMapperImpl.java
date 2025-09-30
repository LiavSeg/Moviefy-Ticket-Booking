package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.ShowtimeEntity;
import moviefyPackge.moviefy.domain.dto.ShowtimeDto.ShowtimeDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
/**
 * Mapper implementation for converting between ShowtimeEntity and ShowtimeDto.
 * This component provides bidirectional conversion between the entity used in persistence
 * and the DTO used for external representation or transport.
 */
@Component
public class ShowtimeMapperImpl implements Mapper<ShowtimeEntity, ShowtimeDto> {

    private final ModelMapper modelMapper;


    public ShowtimeMapperImpl (ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    @Override
    public ShowtimeDto mapTo(ShowtimeEntity showtimeEntity) {
        return modelMapper.map(showtimeEntity,ShowtimeDto.class);
    }

    @Override
    public ShowtimeEntity mapFrom(ShowtimeDto showtimeDto) {
        return modelMapper.map(showtimeDto,ShowtimeEntity.class);
    }
}
