package moviefyPackge.moviefy.services.impl;
import moviefyPackge.moviefy.domain.Entities.UserEntity;
import moviefyPackge.moviefy.domain.dto.UserDto.UserProfileDto;
import moviefyPackge.moviefy.repositories.UserRepository;
import moviefyPackge.moviefy.services.ReviewService;
import moviefyPackge.moviefy.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewService reviewService;
    public UserServiceImpl
            (UserRepository userRepository, PasswordEncoder passwordEncoder,
         ReviewService reviewService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reviewService = reviewService;
    }

    @Override
    public UserEntity userRegister(UserEntity userEntity) {

        if (userRepository.existsByUserName(userEntity.getUserName()) || userRepository.existsByEmail(userEntity.getEmail()))
            throw new IllegalArgumentException("Invalid username or E-mail address");

        String encodedUserPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedUserPassword);
        userEntity.setAdmin(false);

        return userRepository.save(userEntity);
    }


    @Override
    public UserEntity userLogIn(UserEntity userEntity) {

        UserEntity loggedInUserFromDB = userRepository.getByUserName(userEntity.getUserName())
                .orElseThrow(()-> new IllegalArgumentException("Invalid Username or Password"));

        if(!passwordEncoder.matches(userEntity.getPassword(), loggedInUserFromDB.getPassword()))
            throw new IllegalArgumentException("Invalid Username or Password");
        return loggedInUserFromDB;
    }

    @Override
    public UserProfileDto getUserProfile(UUID userID) {
        UserEntity user = userRepository.findByUserId(userID)
                .orElseThrow(()-> new EntityNotFoundException("Could Not a user with ID - "+userID));

        return UserProfileDto.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .reviewsList(reviewService.findByUser(user))
                .build();
    }

    @Override
    public UserEntity getByUserId(UUID userId) {
        return userRepository.findByUserId(userId).orElseThrow(()->new NoSuchElementException("Can't find user with this uuid"));
    }

    @Override
    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }


    @Override
    public String updateUserNameOrEmail(UUID userId, String dataEmail,String dataName) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        String userEmail = user.getEmail();
        String userName = user.getUserName();
        String result = dataEmail;
        if (dataEmail.equals(userEmail) && dataName.equals(userName))
            throw new RuntimeException("Data is the same as before");
        else if (dataEmail.equals(userEmail)) {
            user.setUserName(dataName);
            result = dataName;
        }
        else
            user.setEmail(dataEmail);
        userRepository.save(user);
        return result;
    }

    @Override
    public String changeUserPassword(UUID userId, String currPassword, String newPassword) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(NoSuchElementException::new);
        if(!passwordEncoder.matches(currPassword, user.getPassword()))
            throw new IllegalArgumentException("Invalid Password - Can't change password");
        String newEncodedUserPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncodedUserPassword);
        userRepository.save(user);
        return "";
    }

    @Override
    public UserEntity changeAdminPerms(UUID userId, boolean isAdmin) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }
    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setUserName("Liav");
            admin.setEmail("admin@moviefy.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setAdmin(true);
            userRepository.save(admin);
        }
    }

}
