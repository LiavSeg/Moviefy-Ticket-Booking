package moviefyPackge.moviefy.mappers;

import moviefyPackge.moviefy.domain.Entities.UserEntity;
import moviefyPackge.moviefy.domain.dto.UserDto.UserLoginDto;
import moviefyPackge.moviefy.domain.dto.UserDto.UserRegistrationDto;
import moviefyPackge.moviefy.domain.dto.UserDto.UserResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
/**
 * Handles multiple DTO types for UserEntity,
 * including registration, base info, and response views
 */
@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public UserEntity fromRegistrationDto(UserRegistrationDto userRegistrationDto) {
        return modelMapper.map(userRegistrationDto, UserEntity.class);
    }

    public UserEntity fromLogInDto(UserLoginDto userLoginDto) {
        return modelMapper.map(userLoginDto, UserEntity.class);
    }
    // Maps User Entity -> UserDTO
    public UserLoginDto toDto(UserEntity entity) {
        return modelMapper.map(entity, UserLoginDto.class);
    }

    // Maps User DTO -> UserEntity
    public UserEntity fromUserDto(UserLoginDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    // Maps User Entity -> UserResponseDTO
    public UserResponseDto toResponseDto(UserEntity entity) {
        return modelMapper.map(entity, UserResponseDto.class);
    }
}
