package com.company.socialnetwork.services;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.company.socialnetwork.exceptions.SocialNetworkAppException;
import com.company.socialnetwork.repositories.UserRepository;
import com.company.socialnetwork.dto.ProfileDto;
import com.company.socialnetwork.dto.SignUpDto;
import com.company.socialnetwork.dto.UserDto;
import com.company.socialnetwork.dto.UserSummaryDto;
import com.company.socialnetwork.entities.User;
import com.company.socialnetwork.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public ProfileDto getProfile(Long userId) {
        User user = getUser(userId);
        log.trace("Reading profile for user {}", userId);
        return userMapper.userToProfileDto(user);
    }

    public void addFriend(UserDto userDto, Long friendId) {
        User user = getUser(userDto.getId());

        User newFriend = getUser(friendId);

        if (user.getFriends() == null) {
            user.setFriends(new ArrayList<>());
        }

        user.getFriends().add(newFriend);

        log.info("Current user {} is now friend with {}", userDto.getId(), friendId);

        userRepository.save(user);
    }

    public List<UserSummaryDto> searchUsers(String term) {
        List<User> users = userRepository.search("%" + term + "%");
        List<UserSummaryDto> usersToBeReturned = new ArrayList<>();

        users.forEach(user ->
                usersToBeReturned.add(new UserSummaryDto(user.getId(), user.getFirstName(), user.getLastName()))
        );

        log.debug("Searching for user by '{}'. Found {} users", term, usersToBeReturned.size());

        return usersToBeReturned;
    }

    public UserDto signUp(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new SocialNetworkAppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));

        User savedUser = userRepository.save(user);

        log.info("Creating new user {}", userDto.getLogin());

        return userMapper.toUserDto(savedUser);
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new SocialNetworkAppException("User not found", HttpStatus.NOT_FOUND));
    }
}
