package com.company.socialnetwork.mappers;

import java.util.List;

import com.company.socialnetwork.dto.ImageDto;
import com.company.socialnetwork.entities.Image;
import com.company.socialnetwork.entities.User;
import com.company.socialnetwork.dto.ProfileDto;
import com.company.socialnetwork.dto.SignUpDto;
import com.company.socialnetwork.dto.UserDto;
import com.company.socialnetwork.dto.UserSummaryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    UserSummaryDto toUserSummary(User user);

    List<UserSummaryDto> toUserSummaryDtos(List<User> users);

    @Mapping(target = "userDto.id", source = "id")
    @Mapping(target = "userDto.firstName", source = "firstName")
    @Mapping(target = "userDto.lastName", source = "lastName")
    ProfileDto userToProfileDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

    @Mapping(target = "userDto", source = "user")
    ImageDto imageToImageDto(Image image);

    List<ImageDto> imagesToImageDtos(List<Image> images);
}
