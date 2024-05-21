package com.company.socialnetwork.mappers;

import java.util.List;

import com.company.socialnetwork.entities.Message;
import com.company.socialnetwork.dto.MessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
    uses = {UserMapper.class})
public interface MessageMapper {

    List<MessageDto> messagesToMessageDtos(List<Message> messages);

    @Mapping(target = "userDto", source = "user")
    MessageDto messageToMessageDto(Message message);

    Message messageDtoToMessage(MessageDto messageDto);
}
