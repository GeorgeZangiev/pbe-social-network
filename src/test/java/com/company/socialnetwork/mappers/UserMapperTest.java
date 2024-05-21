package com.company.socialnetwork.mappers;

import com.company.socialnetwork.dto.ImageDto;
import com.company.socialnetwork.dto.ProfileDto;
import com.company.socialnetwork.dto.SignUpDto;
import com.company.socialnetwork.dto.UserDto;
import com.company.socialnetwork.entities.Image;
import com.company.socialnetwork.entities.Message;
import com.company.socialnetwork.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private static UserMapper mapper;

    private static PodamFactory podamFactory;

    @BeforeAll
    public static void setUp() {
        podamFactory = new PodamFactoryImpl();

        // to avoid infinite loops creating random data
        DefaultClassInfoStrategy classInfoStrategy = DefaultClassInfoStrategy.getInstance();
        classInfoStrategy.addExcludedField(Image.class, "user");
        classInfoStrategy.addExcludedField(Message.class, "user");

        podamFactory.setClassStrategy(classInfoStrategy);


        mapper = new UserMapperImpl();
    }

    @Test
    void testUserMapper() {
        // given
        User user = podamFactory.manufacturePojo(User.class);

        // when
        UserDto userDto = mapper.toUserDto(user);

        // then
        assertAll(
                () -> {
                    assertEquals(user.getFirstName(), userDto.getFirstName());
                    assertEquals(user.getLastName(), userDto.getLastName());
                }
        );
    }

    @Test
    void testMapProfile() {
        // given
        User user = podamFactory.manufacturePojo(User.class);

        // when
        ProfileDto profile = mapper.userToProfileDto(user);

        // then
        assertAll(() -> {
            assertEquals(user.getFirstName(), profile.getUserDto().getFirstName());
            assertEquals(user.getLastName(), profile.getUserDto().getLastName());
        });
    }

    @Test
    void testMapSignUp() {
        // given
        SignUpDto signUpDto = podamFactory.manufacturePojo(SignUpDto.class);

        // when
        User user = mapper.signUpToUser(signUpDto);

        // then
        assertAll(() -> {
            assertEquals(signUpDto.getFirstName(), user.getFirstName());
            assertEquals(signUpDto.getLastName(), user.getLastName());
            assertEquals(signUpDto.getLogin(), user.getLogin());
        });
    }

    @Test
    void testMapImage() {
        // given
        Image image = podamFactory.manufacturePojo(Image.class);

        // when
        ImageDto imageDto = mapper.imageToImageDto(image);

        // then
        assertAll(() -> {
            assertEquals(image.getTitle(), imageDto.getTitle());
            assertEquals(image.getPath(), imageDto.getPath());
        });
    }
}
