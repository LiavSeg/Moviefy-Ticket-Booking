package moviefyPackge.moviefy.services;

import moviefyPackge.moviefy.domain.Entities.UserEntity;
import moviefyPackge.moviefy.domain.dto.UserDto.UserProfileDto;

import java.util.List;
import java.util.UUID;

public interface UserService  {
    UserEntity userRegister(UserEntity userEntity );
    UserEntity userLogIn(UserEntity userEntity);
    UserProfileDto getUserProfile(UUID userID);
    UserEntity getByUserId(UUID userId);
    List<UserEntity> allUsers();
    String updateUserNameOrEmail(UUID userId,String dataEmail,String dataName);
    String changeUserPassword(UUID userId,String dataEmail,String dataName);
    UserEntity changeAdminPerms(UUID userId,boolean isAdmin);
}
