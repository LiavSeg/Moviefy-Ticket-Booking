package moviefyPackge.moviefy.mappers.impl;

import moviefyPackge.moviefy.domain.Entities.XmlEntity;
import moviefyPackge.moviefy.domain.dto.xmlModuleDto.XmlResponseDto;
import moviefyPackge.moviefy.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
/**
 * Mapper implementation for converting between XmlEntity and XmlResponseDto.
 * This component uses ModelMapper to handle the conversion logic.
 * Only mapping from entity to DTO is supported in this implementation.
 */
@Component
public class XmlMapperImpl implements Mapper<XmlEntity, XmlResponseDto> {
    private final ModelMapper modelMapper;

    public XmlMapperImpl(ModelMapper modelMapper) {this.modelMapper = modelMapper;}


    @Override
    public XmlResponseDto mapTo(XmlEntity xmlEntity) {
        return modelMapper.map(xmlEntity, XmlResponseDto.class);
    }

    @Override
    public XmlEntity mapFrom(XmlResponseDto xmlResponseDto) {
        throw new RuntimeException();

    }
}
