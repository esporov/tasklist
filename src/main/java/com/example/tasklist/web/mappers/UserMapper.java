package com.example.tasklist.web.mappers;

import com.example.tasklist.domain.user.User;
import com.example.tasklist.web.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
