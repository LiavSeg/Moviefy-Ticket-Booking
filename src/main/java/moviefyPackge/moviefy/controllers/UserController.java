package moviefyPackge.moviefy.controllers;

import moviefyPackge.moviefy.domain.Entities.UserEntity;
import moviefyPackge.moviefy.domain.dto.UserDto.*;
import moviefyPackge.moviefy.errors.ErrorHandler;
import moviefyPackge.moviefy.mappers.UserMapper;
import moviefyPackge.moviefy.security.MoviefyUserDetails;
import moviefyPackge.moviefy.security.SecurityContextHelper;
import moviefyPackge.moviefy.services.impl.UserServiceImpl;
import moviefyPackge.moviefy.utils.LoggerWrapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Exposes endpoints for user registration, authentication, profile access,
 * and admin access to user data.
 * Session-based security is used to manage and verify user identity.
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@EnableMethodSecurity
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final ErrorHandler errorHandler;
    private final LoggerWrapper<UserController> logger = new LoggerWrapper<>(UserController.class,"User");
    /**
     * Initializes the controller with required dependencies.
     * @param userService business logic for user operations
     * @param userMapper mapper for user entities and DTOs
     */
    public UserController(UserServiceImpl userService, UserMapper userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
        this.errorHandler = new ErrorHandler(UserController.class);
        logger.startLog();

    }

    /**
     * Registers a new user and creates a session for them.
     * @param dto registration details
     * @param session HTTP session for login persistence
     * @return HTTP 201 with UserResponseDto on success,
     *         or error response on failure
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegistrationDto dto, HttpSession session) {
        try {
            logger.LogInfo("New registration request");
            logger.fromDTO();
            UserEntity newUser = userMapper.fromRegistrationDto(dto);
            UserEntity registeredUser = userService.userRegister(newUser);
            logger.LogInfo("New User Registered successfully. UID:"+registeredUser.getUserId()+"UserName: "+registeredUser.getUserName());
            UserLogOrSIgnInToken(registeredUser);
            session.setAttribute("userId", registeredUser.getUserId());
            logger.fromEntity();
            UserResponseDto registrationDto =userMapper.toResponseDto(registeredUser);
            logger.success();
            return new ResponseEntity<>(registrationDto, HttpStatus.CREATED);//HttpStatus.CREATED = 201
        }

        catch (IllegalArgumentException e) {
        return errorHandler.conflict("",e,"users/register");
        }
        catch (Exception e) {
        return errorHandler.badRequest("",e,"users/register");
        }
    }

    /**
     * Logs in a user by validating their credentials.
     * @param loginDto login credentials
     * @param request HTTP request object
     * @param response HTTP response object
     * @param session HTTP session to store user info
     * @return HTTP 200 with UserResponseDto on success,
     *         or error response on failure
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginDto loginDto,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   HttpSession session) {
        try {
            logger.LogInfo("New log-in request");
            logger.fromDTO();
            UserEntity loggingUser = userMapper.fromLogInDto(loginDto);
            UserEntity loggedInUser = userService.userLogIn(loggingUser);
            logger.LogInfo(String.format(
                    "User %s logged-in successfully. UID: %s",
                    loggedInUser.getUserName(),
                    loggedInUser.getUserId()
            ));
            logger.LogInfo("Validating user's password");
            UserLogOrSIgnInToken(loggedInUser);
            logger.LogInfo("Password is valid");
            session.setAttribute("userId", loggedInUser.getUserId());
            session.setAttribute("isAdmin", loggedInUser.isAdmin());
            SecurityContextHelper.establishSessionAuth(
                    loggedInUser.getUserName(),
                    loggedInUser.isAdmin(),
                    request, response
            );
            logger.fromEntity();
            UserResponseDto responseDto = userMapper.toResponseDto(loggedInUser);
            logger.success();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return errorHandler.conflict("",e,"users/login");
        }
        catch (NoSuchElementException | EntityNotFoundException e){
        return errorHandler.notFound("",e,"users/login");
}
        catch (Exception e) {
            return errorHandler.badRequest("",e,"users/login");
        }
    }

    /**
     * Retrieves a user's profile if the session matches the user ID.
     * @param userId the user ID being requested
     * @param session the current session
     * @return HTTP 200 with UserProfileDto on success,
     *         HTTP 403 if unauthorized,
     *         or error response on failure
     */
    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable UUID userId, HttpSession session) {
        UUID sessionUserId = (UUID) session.getAttribute("userId");
        logger.LogInfo("New profile details request");
        if (sessionUserId == null || !sessionUserId.equals(userId)) {
            logger.warn("Invalid user trying to get profile details");
            //TODO CHANGE TO ERROR HANDLER, IMPLEMENT FORBIDDEN STATUS CODE
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            UserProfileDto userProfileDto = userService.getUserProfile(userId);
            logger.success();
            return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
        }
        catch (EntityNotFoundException e) {
            return errorHandler.notFound(""+userId, e, "users/profile");
        }
        catch (Exception e) {
            return errorHandler.badRequest(""+userId, e, "users/profile");
        }
    }

    /**
     * Retrieves a list of all users (admin only)
     * @return HTTP 200 with list of users,
     *         or error response on failure
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> allUsers(){
        try {
            logger.LogInfo("Fetching all users data - initiated by an admin");
            List<UserEntity> all = userService.allUsers();
            int size = all.size();
            if (size==0)
                logger.warn("No users were found");
            else
                logger.LogInfo(size+" Users were found");

            return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
        }
        catch (Exception e){
            return errorHandler.badRequest("",e,"users/all");
}
    }


    /**
     * Logs out the user and clears the session.
     * @param session the HTTP session to invalidate
     * @param request the HTTP request
     * @param response the HTTP response
     * @return HTTP 200 on successful logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session,HttpServletRequest request,HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        session.invalidate();
        SecurityContextHelper.clearSessionAuth(request, response);
        logger.LogInfo("Session was cleared successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Returns the currently authenticated user from the session.
     * @param session the current session
     * @return HTTP 200 with UserResponseDto on success,
     *         HTTP 401 if user is not logged in,
     *         or error response on failure
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        try {
            UUID userId = (UUID) session.getAttribute("userId");
            logger.LogInfo("New validation request received from user " + userId);
            if (userId == null) {
                //TODO IMPLEMENT UNAUTHORIZED IN ERROR HANDLER
                logger.warn("Unauthorized user tried to access");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            logger.LogInfo("Fetching user "+userId);
            UserEntity user = userService.getByUserId(userId);
            logger.fromEntity();
            UserResponseDto responseDto = userMapper.toResponseDto(user);
            logger.success();
            return new ResponseEntity<>(responseDto, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return errorHandler.notFound("" + (UUID) session.getAttribute("userId"), e, "users/profile");
        } catch (Exception e) {
            return errorHandler.badRequest("" + (UUID) session.getAttribute("userId"), e, "users/profile");
        }
    }

    /**
     * Establishes an authentication token in the Spring security context
     * based on the given user's identity and roles.
     * @param user the authenticated user
     */
    private void UserLogOrSIgnInToken(UserEntity user){
        MoviefyUserDetails userDetails = new MoviefyUserDetails(user);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @PatchMapping(path = "/edit-profile/{userId}")
    public ResponseEntity<?> patchUserDetails(@Valid @PathVariable UUID userId,
                                              @RequestBody @Valid UserEditProfileDto profileDetails) {
        logger.LogInfo(profileDetails.getEmail()+" "+profileDetails.getUserName());
        String patch = userService.updateUserNameOrEmail(userId,profileDetails.getEmail(), profileDetails.getUserName());
        String logInfoPrefix = "user "+userId+" Changed his ";
        if (patch.equals(profileDetails.getEmail()))
            logger.LogInfo(logInfoPrefix+"Email address");
        else
            logger.LogInfo(logInfoPrefix+"Username to "+patch);
        return new ResponseEntity<>(profileDetails,HttpStatus.OK);
    }
    @PatchMapping(path = "/{userId}/change-password")
    public ResponseEntity<?> changePassword(@Valid @PathVariable UUID userId,
                                              @RequestBody @Valid UserPassChangeDto passwordData) {
        try {
            logger.LogInfo("User " + userId + "Initiated password change request");
            String patch = userService.changeUserPassword(userId, passwordData.getCurrentPassword(), passwordData.getNewPassword());
            String logInfoPrefix = "user " + userId + " Changed his ";
            logger.LogInfo(logInfoPrefix + "Email address");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            logger.warn("Could not change password:" + e.getMessage());
            return errorHandler.badRequest("",e,"users/"+userId+"/change-password");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/{userId}/admin-perms")
    public ResponseEntity<?> changeAdminPerms(@Valid @PathVariable UUID userId,
                                            @RequestBody Boolean isAdmin) {
        try {
            logger.LogInfo("Admin initiated Permission change (+"+isAdmin+") for user " + userId);
            UserEntity changedAdmin = userService.changeAdminPerms(userId,isAdmin);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            logger.warn("Could not change Admin permissions:" + e.getMessage());
            return errorHandler.badRequest("",e,"users/"+userId+"/admin-perms");
        }
    }


}
