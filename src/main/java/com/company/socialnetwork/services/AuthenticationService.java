package com.company.socialnetwork.services;

import java.nio.CharBuffer;
import javax.transaction.Transactional;

import com.company.socialnetwork.dto.CredentialsDto;
import com.company.socialnetwork.exceptions.SocialNetworkAppException;
import com.company.socialnetwork.repositories.UserRepository;
import com.company.socialnetwork.dto.UserDto;
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
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public UserDto authenticate(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new SocialNetworkAppException("User not found", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            log.debug("User {} authenticated correctly", credentialsDto.getLogin());
            return userMapper.toUserDto(user);
        }
        throw new SocialNetworkAppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new SocialNetworkAppException("Login not found", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
}
