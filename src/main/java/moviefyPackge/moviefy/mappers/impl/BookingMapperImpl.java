package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.BookingEntity;
import moviefyPackge.moviefy.domain.dto.BookingDtos.BookingRequestDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Mapper implementation for converting between BookingEntity and BookingRequestDto.
 * This component handles the transformation of booking data between the persistence layer
 * and the API layer using ModelMapper.
 */
@Component
public class BookingMapperImpl implements Mapper<BookingEntity, BookingRequestDto> {
    private final ModelMapper modelMapper;
    public BookingMapperImpl(ModelMapper modelMapper){this.modelMapper = modelMapper;}
    @Override
    public BookingRequestDto mapTo(BookingEntity bookingEntity) {
        return modelMapper.map(bookingEntity,BookingRequestDto.class);
    }

    @Override
    public BookingEntity mapFrom(BookingRequestDto bookingRequestDto) {
        return modelMapper.map(bookingRequestDto, BookingEntity.class);
    }
}
