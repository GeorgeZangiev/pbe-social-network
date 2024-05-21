package com.company.socialnetwork.services;

import java.util.Optional;

import com.company.socialnetwork.dto.CredentialsDto;
import com.company.socialnetwork.dto.UserDto;
import com.company.socialnetwork.entities.Image;
import com.company.socialnetwork.entities.Message;
import com.company.socialnetwork.entities.User;
import com.company.socialnetwork.mappers.UserMapper;
import com.company.socialnetwork.mappers.UserMapperImpl;
import com.company.socialnetwork.repositories.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUp() {
        podamFactory = new PodamFactoryImpl();

        // to avoid infinite loops creating random data
        DefaultClassInfoStrategy classInfoStrategy = DefaultClassInfoStrategy.getInstance();
        classInfoStrategy.addExcludedField(Image.class, "user");
        classInfoStrategy.addExcludedField(Message.class, "user");

        podamFactory.setClassStrategy(classInfoStrategy);
    }

    @Test
    public void testAuthentication() {
        // given
        CredentialsDto credentialsDto = podamFactory.manufacturePojo(CredentialsDto.class);
        User user = podamFactory.manufacturePojo(User.class);

        when(userRepository.findByLogin(credentialsDto.getLogin()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // when
        UserDto userDto = authenticationService.authenticate(credentialsDto);

        // then
        assertAll(() -> {
            assertEquals(user.getFirstName(), userDto.getFirstName());
            assertEquals(user.getLastName(), userDto.getLastName());
        });
        verify(userMapper).toUserDto(any());
        verify(passwordEncoder).matches(any(), any());
    }

    @Test
    void testFindByLogin() {
        // given
        String login = "login";
        User user = podamFactory.manufacturePojo(User.class);

        when(userRepository.findByLogin(login))
                .thenReturn(Optional.of(user));

        // when
        UserDto userDto = authenticationService.findByLogin(login);

        // then
        assertAll(() -> {
            assertEquals(user.getFirstName(), userDto.getFirstName());
            assertEquals(user.getLastName(), userDto.getLastName());
        });
    }
}
